package es.dimecresalessis.scoutbase.infrastructure.user.web;

import es.dimecresalessis.scoutbase.application.security.AuthService;
import es.dimecresalessis.scoutbase.application.security.LoginRequest;
import es.dimecresalessis.scoutbase.domain.user.model.Role;
import es.dimecresalessis.scoutbase.infrastructure.web.annotation.ApiCommonResponses;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.application.user.CreateUserUseCase;
import es.dimecresalessis.scoutbase.application.user.FindUserByIdUseCase;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDto;
import es.dimecresalessis.scoutbase.infrastructure.user.web.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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

    /**
     * Finds a user by their ID.
     *
     * @param id The ID of the user.
     * @return {@link ApiResponse} containing the user's information.
     */
    @GetMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Find user by id", description = "Returns a user through an path variable 'id'.")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ApiResponse<UserDto> findById(@PathVariable UUID id) {
        return handleResponse(
                userMapper.toDto(
                        findUserByIdUseCase.execute(id)
                )
        ).ok();
    }

    /**
     * Creates a random user with a specified role. **Testing purposes**.
     *
     * @param role The role of the user to create (ROLE_USER, ROLE_ADMIN...).
     * @return {@link ApiResponse} containing the created user's details.
     * @throws IllegalAccessException If the role parameter is invalid or missing.
     */
    @GetMapping("/new")
    @Operation(summary = "Create random user", description = "Creates a user with a specific role through the role query parameter ('ROLE_USER' or 'ROLE_ADMIN')")
    public ApiResponse<UserDto> newUser(@RequestParam(value = "role") String role) throws IllegalAccessException {
        if (role == null) {
            throw new IllegalAccessException("Must send a 'role' as path parameter for the request.");
        }
        if (Role.fromName(role) == null) {
            throw new IllegalAccessException("Only roles ROLE_USER and ROLE_ADMIN are available.");
        }
        UserDto userDto = UserDto.getRandomInstance(role.toUpperCase());
        User user = createUserUseCase.execute(userMapper.toDomain(userDto));
        return handleResponse(userMapper.toDto(user)).ok();
    }

    /**
     * Creates a new user.
     *
     * @param userDto The details of the user to create.
     * @return {@link ApiResponse} containing the user's information.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a user", description = "Creates a user through a UserDto body.")
    @SecurityRequirement(name = "bearerAuth")
    public ApiResponse<Boolean> create(@RequestBody UserDto userDto) {
        createUserUseCase.execute(userMapper.toDomain(userDto));
        return handleResponse(true).ok();
    }

    /**
     * Authenticates a user and generates a token.
     *
     * @param loginRequest The login credentials of the user.
     * @return {@link ApiResponse} containing an authentication token.
     */
    @PostMapping(Routes.AUTH_LOGIN)
    @Operation(summary = "Login", description = "Returns an authentification token if a user and its password exists in the database.")
    public ApiResponse<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.authenticateAndGenerateToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        return handleResponse(Map.of("token", token)).ok();
    }

}