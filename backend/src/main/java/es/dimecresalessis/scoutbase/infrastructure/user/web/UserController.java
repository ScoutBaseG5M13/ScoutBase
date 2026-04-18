package es.dimecresalessis.scoutbase.infrastructure.user.web;

import es.dimecresalessis.scoutbase.application.security.AuthService;
import es.dimecresalessis.scoutbase.application.security.LoginRequest;
import es.dimecresalessis.scoutbase.application.user.*;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.infrastructure.security.JwtService;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDTO;
import es.dimecresalessis.scoutbase.infrastructure.web.annotation.ApiCommonResponses;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.web.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import static es.dimecresalessis.scoutbase.infrastructure.web.dto.ResponseFactory.handleResponse;

/**
 * REST Controller for User Management.
 */
@RestController
@AllArgsConstructor
@ApiCommonResponses
@Tag(name = "Users", description = "User management endpoints")
@RequestMapping(Routes.API_ROOT + Routes.USERS)
public class UserController {

    private final UserMapper userMapper;
    private final CreateUserUseCase createUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final AuthService authService;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final FindUserByUsernameUseCase findUserByUsernameUseCase;
    private final JwtService jwtService;
    private final CreateRandomUserUseCase createRandomUserUseCase;

    /**
     * Finds a user by their ID.
     *
     * @param id The ID of the user.
     * @return {@link ApiResponse} containing the user's information.
     */
    @GetMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Find user by id", description = "Returns a user through a path variable 'id'.")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<UserDTO>> findById(@PathVariable UUID id) {
        try {
            User user = findUserByIdUseCase.execute(id);
            UserDTO userDto = userMapper.toDto(user);
            return handleResponse(userDto).ok();
        } catch (NoSuchElementException ex) {
            throw new UserException(ErrorEnum.USER_NOT_FOUND, id.toString());
        }
    }

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user.
     * @return {@link ApiResponse} containing the user's information.
     */
    @GetMapping(Routes.USERNAME_PATH + Routes.USERNAME_PATHVAR)
    @Operation(summary = "Find user by username", description = "Returns a user through a path variable 'username'.")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<UserDTO>> findByUsername(@PathVariable String username) {
        try {
            User user = findUserByUsernameUseCase.execute(username);
            UserDTO userDto = userMapper.toDto(user);
            return handleResponse(userDto).ok();
        } catch (NoSuchElementException ex) {
            throw new UserException(ErrorEnum.USER_NOT_FOUND, username);
        }
    }

    /**
     * Creates a random user with a specified role. **Testing purposes**.
     *
     * @param role The role of the user to create (ROLE_USER, ROLE_ADMIN...).
     * @return {@link ApiResponse} containing the created user's deatails.
     * @throws IllegalAccessException If the role parameter is invalid or missing.
     */
    @GetMapping(Routes.NEW_PATH)
    @Operation(summary = "Create random user", description = "Creates a user with a specific role through the role query parameter ('ROLE_USER' or 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> newRandomUser(@RequestParam(value = "role") String role) throws IllegalAccessException {
        if (role == null) {
            throw new IllegalAccessException("Must send a 'role' as path parameter for the request.");
        }

        UserDTO userDto = UserDTO.getRandomInstance(role.toUpperCase());
        User user = createRandomUserUseCase.execute(userMapper.toDomain(userDto));
        return handleResponse(userMapper.toDto(user)).ok();
    }

    /**
     * Creates a new {@link User}.
     *
     * @param userDto The details of the user to create.
     * @return {@link ApiResponse} containing the user's information.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a user", description = "Creates a user through a UserDto body.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<Boolean>> create(@RequestBody UserDTO userDto) {
        createUserUseCase.execute(userMapper.toDomain(userDto));
        return handleResponse(true).created();
    }

    /**
     * Updates a {@link User}.
     *
     * @param userDto The details of the user to create.
     * @return {@link ApiResponse} containing the user's information.
     */
    @PutMapping(value = Routes.ID_PATHVAR, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Updates a user", description = "Creates a user through a UserDto body.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<UserDTO>> update(@RequestBody UserDTO userDto, @PathVariable UUID id) {
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
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletes a user by ID", description = "Deletes a user through an ID path parameter.")
    @SecurityRequirement(name = "bearerAuth")
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
    @PostMapping(value = Routes.AUTH_LOGIN, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Login", description = "Returns an authentification token if a user and its password exists in the database.")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.authenticateAndGenerateToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        return handleResponse(Map.of("token", token)).ok();
    }

    /**
     * Returns the role of the currently authenticated user.
     *
     * @param authHeader The current security context, passed as the header "Authentication".
     * @return {@link ApiResponse} containing the user's role.
     */
    @GetMapping(value = Routes.ROLE_PATH)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get user role", description = "Returns the role of the user currently authorized.")
    public ResponseEntity<ApiResponse<String>> getRole(@RequestHeader(value = "Authorization") String authHeader) {
        String jwtKey = authHeader.substring("Bearer ".length());
        return handleResponse(jwtService.extractRole(jwtKey)).ok();
    }
}