package es.dimecresalessis.scoutbase.infrastructure.user.web;

import es.dimecresalessis.scoutbase.application.club.find.FindAllClubsByUserUseCase;
import es.dimecresalessis.scoutbase.application.security.AuthService;
import es.dimecresalessis.scoutbase.application.security.LoginRequest;
import es.dimecresalessis.scoutbase.application.security.UserAuthService;
import es.dimecresalessis.scoutbase.application.user.create.CreateUserUseCase;
import es.dimecresalessis.scoutbase.application.user.delete.DeleteUserUseCase;
import es.dimecresalessis.scoutbase.application.user.find.FindUserByIdUseCase;
import es.dimecresalessis.scoutbase.application.user.find.FindUserByUsernameUseCase;
import es.dimecresalessis.scoutbase.application.user.update.UpdateUserUseCase;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
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
    private final FindAllClubsByUserUseCase findAllClubsByUserUseCase;

    /**
     * Finds a user by their ID.
     *
     * @param userId The ID of the user.
     * @return {@link ApiResponse} containing the user's information.
     */
    @GetMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Find user by ID [Auth ADMIN]", description = "Finds a user by ID")
    public ResponseEntity<ApiResponse<UserDTO>> findUserById(@PathVariable(value = "id") UUID userId) {
        // ¿En qué Clubs están ambos Users?
        List<Club> clubsOfLookedUpUser = findAllClubsByUserUseCase.execute(userId);
        List<Club> clubsOfCurrentUser = findAllClubsByUserUseCase.execute(Session.getSessionUser().getId());
        List<Club> sameClubs = new ArrayList<>();
        for (Club club : clubsOfLookedUpUser) {
            for (Club club2 : clubsOfCurrentUser) {
                if (club.getId().equals(club2.getId()) && !sameClubs.contains(club)) {
                    sameClubs.add(club);
                    break;
                }
            }
        }

        // ¿En cualquiera de esos Club el usuario autentificado es ADMIN para proseguir con la acción?
        for (Club club : sameClubs) {
            if (userAuthService.isAuthorizedByClub(Session.getSessionUser(), club.getId(), RoleEnum.ADMIN)) {
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

    @GetMapping(Routes.ME_PATH)
    @Operation(summary = "Find my user", description = "Finds own User")
    public ResponseEntity<ApiResponse<UserDTO>> findUserById() {
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
    @Operation(summary = "Find user by username", description = "Returns a User through a 'username'.")
    public ResponseEntity<ApiResponse<UserDTO>> findByUsername(@PathVariable String username) {
        // ¿En qué Clubs están ambos Users?
        try {
            User user = findUserByUsernameUseCase.execute(username);
            List<Club> clubsOfLookedUpUser = findAllClubsByUserUseCase.execute(user.getId());
            List<Club> clubsOfCurrentUser = findAllClubsByUserUseCase.execute(Session.getSessionUser().getId());
            List<Club> sameClubs = new ArrayList<>();
            for (Club club : clubsOfLookedUpUser) {
                for (Club club2 : clubsOfCurrentUser) {
                    if (club.getId().equals(club2.getId()) && !sameClubs.contains(club)) {
                        sameClubs.add(club);
                        break;
                    }
                }
            }

            // ¿En cualquiera de esos Club el usuario autentificado es ADMIN para proseguir con la acción?
            for (Club club : sameClubs) {
                if (userAuthService.isAuthorizedByClub(Session.getSessionUser(), club.getId(), RoleEnum.ADMIN)) {
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
    @Operation(summary = "Create a user", description = "Creates a user")
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
    @Operation(summary = "Updates a user", description = "Creates a user through a UserDto body.")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@RequestBody UserDTO userDto, @PathVariable UUID id) {
        try {
            User updatedUser = updateUserUseCase.execute(userMapper.toDomain(userDto), id);
            UserDTO updatedUserDTO = userMapper.toDto(updatedUser);
            return handleResponse(updatedUserDTO).ok();
        } catch (DataIntegrityViolationException ex) {
            throw new UserException(ErrorEnum.USER_NOT_VALID, ex.getMessage());
        }
    }

    /**
     * Deletes a {@link User}.
     *
     * @param id The ID of the user to delete.
     * @return {@link ApiResponse} containing the user's information.
     */
    @DeleteMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Deletes a user by ID", description = "Deletes a user through an ID path parameter.")
    public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable UUID id) {
        try {
            boolean isDeleted = deleteUserUseCase.execute(id);
            return handleResponse(isDeleted).ok();
        } catch (NoSuchElementException ex) {
            throw new UserException(ErrorEnum.USER_NOT_FOUND, id.toString());
        }
    }

    /**
     * Authenticates a user and generates a token.
     *
     * @param loginRequest The login credentials of the user.
     * @return {@link ApiResponse} containing an authentication token.
     */
    @PostMapping(value = Routes.AUTH_LOGIN)
    @Operation(summary = "Login", description = "Returns an authentification token if a user and its password exists in the database")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.authenticateAndGenerateToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        return handleResponse(Map.of("token", token)).ok();
    }

    /**
     * Returns the role of the currently authenticated user inside the team.
     *
     * @param teamId the team from the consumer wants to check the role of the user.
     * @return {@link ApiResponse} containing the user's role.
     */
    @GetMapping(value =  Routes.TEAMS + Routes.ID_PATHVAR + Routes.ROLE_PATH)
    @Operation(summary = "Get user role in the team", description = "Returns the role in the team of the user currently logged")
    public ResponseEntity<ApiResponse<String>> getTeamRole(@PathVariable(value = "id") UUID teamId) {
        RoleEnum role = userAuthService.findTeamUserRole(Session.getSessionUser(), teamId);
        return handleResponse(role != null ? role.getRoleName() : "No role found").ok();
    }

    /**
     * Returns the role of the currently authenticated user inside the club.
     *
     * @param clubId the club from the consumer wants to check the role of the user.
     * @return {@link ApiResponse} containing the user's role.
     */
    @GetMapping(value =  Routes.CLUBS + Routes.ID_PATHVAR + Routes.ROLE_PATH)
    @Operation(summary = "Get user role in the club", description = "Returns the role in the club of the user currently logged")
    public ResponseEntity<ApiResponse<String>> getClubRole(@PathVariable(value = "id") UUID clubId) {
        RoleEnum role = userAuthService.findClubUserRole(Session.getSessionUser(), clubId);
        return handleResponse(role != null ? role.getRoleName() : "No role found").ok();
    }
}