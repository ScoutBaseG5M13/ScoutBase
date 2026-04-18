package es.dimecresalessis.scoutbase.infrastructure.user.web;

import es.dimecresalessis.scoutbase.application.security.AuthService;
import es.dimecresalessis.scoutbase.application.security.LoginRequest;
import es.dimecresalessis.scoutbase.application.user.*;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.security.JwtService;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDTO;
import es.dimecresalessis.scoutbase.infrastructure.user.web.mapper.UserMapper;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private CreateUserUseCase createUserUseCase;
    @Mock
    private FindUserByIdUseCase findUserByIdUseCase;
    @Mock
    private AuthService authService;
    @Mock
    private DeleteUserUseCase deleteUserUseCase;
    @Mock
    private UpdateUserUseCase updateUserUseCase;
    @Mock
    private FindUserByUsernameUseCase findUserByUsernameUseCase;

    @InjectMocks
    private UserController userController;

    private UUID userId;
    private User user;
    private UserDTO userDto;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder()
                .id(userId)
                .username("scout_master")
                .password("encoded_password")
                .role("ADMIN")
                .name("Alex")
                .surname("Scout")
                .email("alex@scoutbase.com")
                .build();

        userDto = new UserDTO(
                userId,
                "scout_user",
                "raw_password",
                "USER",
                "John",
                "Doe",
                "john@doe.com"
        );
    }

    @Test
    @DisplayName("findById - Should return user")
    void findById_ShouldReturnUser() {
        when(findUserByIdUseCase.execute(userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<ApiResponse<UserDTO>> response = userController.findById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userId, response.getBody().data().getId());
    }

    @Test
    @DisplayName("findById - Should throw UserException when user does not exist")
    void findById_ShouldThrowException() {
        when(findUserByIdUseCase.execute(userId)).thenThrow(new NoSuchElementException());

        assertThrows(UserException.class, () -> userController.findById(userId));
    }

    @Test
    @DisplayName("findByUsername - Should return user")
    void findByUsername_ShouldReturnUser() {
        String username = "admin";
        when(findUserByUsernameUseCase.execute(username)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<ApiResponse<UserDTO>> response = userController.findByUsername(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(username, response.getBody().data().getUsername());
    }

    @Test
    @DisplayName("newUser - Should return random user")
    void newUser_ShouldReturnRandomRandomUser() throws IllegalAccessException {
        String role = "ROLE_ADMIN";
        when(userMapper.toDomain(any())).thenReturn(user);
        when(createUserUseCase.execute(any())).thenReturn(user);
        when(userMapper.toDto(any())).thenReturn(userDto);

        ResponseEntity<ApiResponse<UserDTO>> response = userController.newRandomUser(role);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().data());
    }

    @Test
    @DisplayName("newUser - Should throw IllegalAccessException if role is invalid")
    void newRandomUser_ShouldThrowException_WhenRoleInvalid() {
        assertThrows(IllegalAccessException.class, () -> userController.newRandomUser("INVALID_ROLE"));
    }

    @Test
    @DisplayName("create - Should return true on success")
    void create_ShouldReturnCreated() {
        when(userMapper.toDomain(userDto)).thenReturn(user);

        ResponseEntity<ApiResponse<Boolean>> response = userController.create(userDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().data());
        verify(createUserUseCase).execute(user);
    }

    @Test
    @DisplayName("update - Should return updated user")
    void update_ShouldReturnOk() {
        when(findUserByIdUseCase.execute(userId)).thenReturn(user);
        when(userMapper.toDomain(userDto)).thenReturn(user);
        when(updateUserUseCase.execute(user, userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<ApiResponse<UserDTO>> response = userController.update(userDto, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(updateUserUseCase).execute(user, userId);
    }

    @Test
    @DisplayName("delete - Should return true on success")
    void delete_ShouldReturnOk() {
        when(deleteUserUseCase.execute(userId)).thenReturn(true);

        ResponseEntity<ApiResponse<Boolean>> response = userController.delete(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().data());
    }

    @Test
    @DisplayName("login - Should return token")
    void login_ShouldReturnToken() {
        LoginRequest loginRequest = new LoginRequest("admin", "pass");
        String token = "mocked-jwt-token";
        when(authService.authenticateAndGenerateToken("admin", "pass")).thenReturn(token);

        ResponseEntity<ApiResponse<Map<String, String>>> response = userController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token, response.getBody().data().get("token"));
    }

    @Test
    @DisplayName("getRole - Should return extracted role from token")
    void getRole_ShouldReturnRole() {
        String authHeader = "Bearer mysuperhipermegasecretjwttoken";
        String token = "mysuperhipermegasecretjwttoken";
        String expectedRole = "ROLE_ADMIN";

        when(jwtService.extractRole(token)).thenReturn(expectedRole);

        ResponseEntity<ApiResponse<String>> response = userController.getRole(authHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedRole, response.getBody().data());

        verify(jwtService).extractRole(token);
    }
}