package es.dimecresalessis.scoutbase.infrastructure.user.web;

import es.dimecresalessis.scoutbase.application.userclub.find.FindAllUserClubsByUserUseCase;
import es.dimecresalessis.scoutbase.application.userclub.find.FindUserClubByIdUseCase;
import es.dimecresalessis.scoutbase.application.security.AuthService;
import es.dimecresalessis.scoutbase.application.security.LoginRequest;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.infrastructure.security.UserAuthService;
import es.dimecresalessis.scoutbase.application.userteam.find.FindAllUserTeamsByUserClubUseCase;
import es.dimecresalessis.scoutbase.application.userteam.find.FindUserTeamByIdUseCase;
import es.dimecresalessis.scoutbase.application.user.create.CreateUserUseCase;
import es.dimecresalessis.scoutbase.application.user.delete.DeleteUserUseCase;
import es.dimecresalessis.scoutbase.application.user.find.*;
import es.dimecresalessis.scoutbase.application.user.update.UpdateUserUseCase;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.infrastructure.security.Session;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDTO;
import es.dimecresalessis.scoutbase.infrastructure.web.annotation.ApiCommonResponses;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.web.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static es.dimecresalessis.scoutbase.infrastructure.web.dto.ResponseFactory.handleResponse;

/**
 * REST Controller for User Management.
 */
@RestController
@AllArgsConstructor
@ApiCommonResponses
@Tag(name = "User", description = "User management endpoints")
@RequestMapping(Routes.API_ROOT + Routes.USERS)
public class UserController {

    private final UserMapper userMapper;
    private final CreateUserUseCase createUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final AuthService authService;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final FindUserByUsernameUseCase findUserByUsernameUseCase;
    private final UserAuthService userAuthService;
    private final FindAllUserClubsByUserUseCase findAllUserClubsByUserUseCase;
    private final FindAllUserTeamsByUserClubUseCase findAllUserTeamsByUserClubUseCase;
    private final FindUserClubByIdUseCase findUserClubByIdUseCase;
    private final FindUserTeamByIdUseCase findUserTeamByIdUseCase;
    private final FindAllUsersUseCase findAllUsersUseCase;
    private final FindAllScoutsUseCase findAllScoutsUseCase;
    private final FindUserRoleInTeamUseCase findUserRoleInTeamUseCase;
    private final FindUserRoleInClubUseCase findUserRoleInClubUseCase;

    /**
     * Finds all Users. For Superadmin pursposes. Or testing now.
     *
     * @return {@link ApiResponse} with the current user's {@link UserDTO}.
     */
    @GetMapping
    @Operation(summary = "Find all Users User [Auth SUPERADMIN]", description = "Finds all Users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> findAllUsers() {
        userAuthService.hasSuperadminAuthorization();
        List<User> users = findAllUsersUseCase.execute();
        List<UserDTO> usersDTO = users.stream().map(userMapper::toDto).toList();
        return handleResponse(usersDTO).ok();
    }

    /**
     * Finds all User Scouters.
     *
     * @return {@link ApiResponse} with the current user's {@link User}.
     */
    @GetMapping("/scouters")
    @Operation(summary = "Find all Scouter User [Auth SUPERADMIN]", description = "Finds all Scouter Users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> findAllScouters() {
        userAuthService.hasSuperadminAuthorization();
        List<User> scouters = findAllScoutsUseCase.execute();
        List<UserDTO> scoutersDTO = scouters.stream().map(userMapper::toDto).toList();
        return handleResponse(scoutersDTO).ok();
    }

    /**
     * Finds a user by their ID.
     *
     * @param userId The ID of the user.
     * @return {@link ApiResponse} containing the user's information.
     */
    @GetMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Find User by ID [Auth ADMIN]", description = "Finds a User")
    public ResponseEntity<ApiResponse<UserDTO>> findUserById(@PathVariable("id") UUID userId) {
        // ¿En qué Clubs están ambos Users?
        List<UserClub> clubsOfLookedUpUser = findAllUserClubsByUserUseCase.execute(userId);
        List<UserClub> clubsOfCurrentUser = findAllUserClubsByUserUseCase.execute(Session.getSessionUser().getId());
        List<UserClub> sameUserClubs = new ArrayList<>();
        for (UserClub userClub : clubsOfLookedUpUser) {
            for (UserClub userClub2 : clubsOfCurrentUser) {
                if (userClub.getId().equals(userClub2.getId()) && !sameUserClubs.contains(userClub)) {
                    sameUserClubs.add(userClub);
                    break;
                }
            }
        }

        // ¿En cualquiera de esos Club el usuario autentificado es ADMIN para proseguir con la acción?
        for (UserClub userClub : sameUserClubs) {
            if (userAuthService.isAuthorizedByClub(userClub.getId(), RoleEnum.ADMIN)) {
                try {
                    User user = findUserByIdUseCase.execute(userId);
                    UserDTO userDto = userMapper.toDto(user);
                    return handleResponse(userDto).ok();
                } catch (NoSuchElementException ex) {
                    throw new UserException(ErrorEnum.USER_NOT_FOUND, userId.toString());
                }
            }
        }
        throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, RoleEnum.ADMIN.name());
    }

    /**
     * Finds all users in the Club.
     *
     * @param clubId The ID of the userclub.
     * @return {@link ApiResponse} containing the user's information.
     */
    @GetMapping(Routes.CLUBS + Routes.ID_PATHVAR)
    @Operation(summary = "Finds all Users by Club ID [Auth ADMIN]", description = "Finds all Users by Club")
    public ResponseEntity<ApiResponse<List<UserDTO>>> findAllUsersByClub(@PathVariable("id") UUID clubId) {
        userAuthService.isAuthorizedByClub(clubId, RoleEnum.ADMIN);
        List<UUID> userIds = new ArrayList<>();
        UserClub clubs = findUserClubByIdUseCase.execute(clubId);
        userIds.addAll(clubs.getAdminUserIds());
        List<UserTeam> userTeams = findAllUserTeamsByUserClubUseCase.execute(clubs.getId());
        for (UserTeam userTeam : userTeams) {
            if (userTeam.getTrainer() != null) {
                userIds.add(userTeam.getTrainer());
            }
            if (userTeam.getSecondTrainer() != null) {
                userIds.add(userTeam.getSecondTrainer());
            }
            if (!userTeam.getScouters().isEmpty()) {
                userIds.addAll(userTeam.getScouters());
            }
        }
        List<UserDTO> usersDTO = new ArrayList<>();
        for (UUID userId : userIds) {
            User user = findUserByIdUseCase.execute(userId);
            usersDTO.add(userMapper.toDto(user));
        }
        return handleResponse(usersDTO).ok();
    }

    /**
     * Finds all users in the Team.
     *
     * @param teamId The ID of the userclub.
     * @return {@link ApiResponse} containing the user's information.
     */
    @GetMapping(Routes.TEAMS + Routes.ID_PATHVAR)
    @Operation(summary = "Finds all Users by Team ID [Auth ADMIN]", description = "Finds all Users by Team")
    public ResponseEntity<ApiResponse<List<UserDTO>>> findAllUsersByTeam(@PathVariable("id") UUID teamId) {
        userAuthService.isAuthorizedByTeam(teamId, RoleEnum.ADMIN);
            List<UUID> userIds = new ArrayList<>();
            UserTeam userTeam = findUserTeamByIdUseCase.execute(teamId);
                if (userTeam.getTrainer() != null) {
                    userIds.add(userTeam.getTrainer());
                }
                if (userTeam.getSecondTrainer() != null) {
                    userIds.add(userTeam.getSecondTrainer());
                }
                if (!userTeam.getScouters().isEmpty()) {
                    userIds.addAll(userTeam.getScouters());
                }
            List<UserDTO> usersDTO = new ArrayList<>();
            for (UUID userId : userIds) {
                User user = findUserByIdUseCase.execute(userId);
                usersDTO.add(userMapper.toDto(user));
            }
            return handleResponse(usersDTO).ok();
    }

    /**
     * Retrieves the profile of the currently authenticated user.
     *
     * @return {@link ApiResponse} with the current user's {@link UserDTO}.
     */
    @GetMapping(Routes.ME_PATH)
    @Operation(summary = "Find authenticated User [Auth OWN]", description = "Finds own authenticated User")
    public ResponseEntity<ApiResponse<UserDTO>> findMyUser() {
        User user = findUserByIdUseCase.execute(Session.getSessionUser().getId());
        return handleResponse(userMapper.toDto(user)).ok();
    }

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user.
     * @return {@link ApiResponse} containing the user's information.
     */
    @GetMapping(Routes.USERNAME_PATH + Routes.USERNAME_PATHVAR)
    @Operation(summary = "Find User by username [Auth ADMIN]", description = "Finds a User by 'username'")
    public ResponseEntity<ApiResponse<UserDTO>> findUserByUsername(@PathVariable String username) {
        // ¿En qué Clubs están ambos Users?
        try {
            User user = findUserByUsernameUseCase.execute(username);
            List<UserClub> clubsOfLookedUpUser = findAllUserClubsByUserUseCase.execute(user.getId());
            List<UserClub> clubsOfCurrentUser = findAllUserClubsByUserUseCase.execute(Session.getSessionUser().getId());
            List<UserClub> sameUserClubs = new ArrayList<>();
            for (UserClub userClub : clubsOfLookedUpUser) {
                for (UserClub userClub2 : clubsOfCurrentUser) {
                    if (userClub.getId().equals(userClub2.getId()) && !sameUserClubs.contains(userClub)) {
                        sameUserClubs.add(userClub);
                        break;
                    }
                }
            }

            // ¿En cualquiera de esos Club el usuario autentificado es ADMIN para proseguir con la acción?
            for (UserClub userClub : sameUserClubs) {
                if (userAuthService.isAuthorizedByClub(userClub.getId(), RoleEnum.ADMIN)) {
                        UserDTO userDto = userMapper.toDto(user);
                        return handleResponse(userDto).ok();
                }
            }
            throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, RoleEnum.ADMIN.name());
        } catch (NoSuchElementException ex) {
            throw new UserException(ErrorEnum.USER_NOT_FOUND, username);
        }
    }

    /**
     * Creates a new {@link User}.
     *
     * @param createRequest The details of the user to create.
     * @return {@link ApiResponse} containing the user's information.
     */
    @PostMapping
    @Operation(summary = "Create User", description = "Creates a user")
    public ResponseEntity<ApiResponse<Boolean>> createUser(@RequestBody UserCreateRequest createRequest) {
        createUserUseCase.execute(userMapper.createToDomain(createRequest));
        return handleResponse(true).created();
    }

    /**
     * Updates a {@link User}.
     *
     * @param userDto The details of the user to create.
     * @return {@link ApiResponse} containing the user's information.
     */
    @PutMapping(value = Routes.ID_PATHVAR)
    @Operation(summary = "Update User [Auth OWN]", description = "Updates a User")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@RequestBody UserDTO userDto, @PathVariable(value = "id") UUID userId) {
        if (!userAuthService.isOwnUser(userId)) {
            throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, "BE SAME USER");
        }
        try {
            User updatedUser = updateUserUseCase.execute(userMapper.toDomain(userDto), userId);
            UserDTO updatedUserDTO = userMapper.toDto(updatedUser);
            return handleResponse(updatedUserDTO).ok();
        } catch (DataIntegrityViolationException ex) {
            throw new UserException(ErrorEnum.USER_NOT_VALID, ex.getMessage());
        }
    }

    /**
     * Deletes a {@link User}.
     *
     * @param userId The ID of the user to delete.
     * @return {@link ApiResponse} containing the user's information.
     */
    @DeleteMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Delete User by ID [Auth OWN]", description = "Deletes a User")
    public ResponseEntity<ApiResponse<Boolean>> deleteUser(@PathVariable(value = "id") UUID userId) {
        userAuthService.isOwnUser(userId);
        try {
            boolean isDeleted = deleteUserUseCase.execute(userId);
            return handleResponse(isDeleted).ok();
        } catch (NoSuchElementException ex) {
            throw new UserException(ErrorEnum.USER_NOT_FOUND, userId.toString());
        }
    }

    /**
     * Authenticates a user and generates a token.
     *
     * @param loginRequest The login credentials of the user.
     * @return {@link ApiResponse} containing an authentication token.
     */
    @PostMapping(Routes.AUTH_LOGIN)
    @Operation(summary = "Login [Auth OWN]", description = "Authenticates himself and returns a JWT Auth token")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.authenticateAndGenerateToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        return handleResponse(Map.of("token", token)).ok();
    }

    /**
     * Returns the role of the currently authenticated user inside the userteam.
     *
     * @param teamId the userteam from the consumer wants to check the role of the user.
     * @return {@link ApiResponse} containing the user's role.
     */
    @GetMapping(value =  Routes.TEAMS + Routes.ID_PATHVAR + Routes.ROLE_PATH)
    @Operation(summary = "Get User role inside Team [Auth ADMIN]", description = "Find the role in the Team of the User currently logged in")
    public ResponseEntity<ApiResponse<String>> getTeamRole(@PathVariable(value = "id") UUID teamId) {
        userAuthService.hasMinimumTeamAuthorization(teamId,  RoleEnum.ADMIN);
        RoleEnum role = findUserRoleInTeamUseCase.execute(Session.getSessionUser(), teamId);
        return handleResponse(role != null ? role.getRoleName() : "No role found").ok();
    }

    /**
     * Returns the role of the currently authenticated user inside the userclub.
     *
     * @param clubId the userclub from the consumer wants to check the role of the user.
     * @return {@link ApiResponse} containing the user's role.
     */
    @GetMapping(value =  Routes.CLUBS + Routes.ID_PATHVAR + Routes.ROLE_PATH)
    @Operation(summary = "Get User role inside Club [Auth ADMIN]", description = "Find the role in the Club of the User currently logged in")
    public ResponseEntity<ApiResponse<String>> getClubRole(@PathVariable(value = "id") UUID clubId) {
        userAuthService.hasMinimumClubAuthorization(clubId,  RoleEnum.ADMIN);
        RoleEnum role = findUserRoleInClubUseCase.execute(Session.getSessionUser(), clubId);
        return handleResponse(role != null ? role.getRoleName() : "No role found").ok();
    }
}