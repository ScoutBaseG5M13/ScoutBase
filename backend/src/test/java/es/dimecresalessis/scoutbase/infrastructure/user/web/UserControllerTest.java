package es.dimecresalessis.scoutbase.infrastructure.user.web;

import es.dimecresalessis.scoutbase.application.security.AuthService;
import es.dimecresalessis.scoutbase.application.security.LoginRequest;
import es.dimecresalessis.scoutbase.application.user.*;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDto;
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
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User(userId, "admin", "pass", "ROLE_ADMIN");
        userDto = new UserDto(userId, "admin", "pass", "ROLE_ADMIN");
    }

    @Test
    @DisplayName("findById - Should return user")
    void findById_ShouldReturnUser() {
        when(findUserByIdUseCase.execute(userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<ApiResponse<UserDto>> response = userController.findById(userId);

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

        ResponseEntity<ApiResponse<UserDto>> response = userController.findByUsername(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(username, response.getBody().data().getUsername());
    }

    @Test
    @DisplayName("newUser - Should return random user")
    void newUser_ShouldReturnRandomUser() throws IllegalAccessException {
        String role = "ROLE_ADMIN";
        when(userMapper.toDomain(any())).thenReturn(user);
        when(createUserUseCase.execute(any())).thenReturn(user);
        when(userMapper.toDto(any())).thenReturn(userDto);

        ResponseEntity<ApiResponse<UserDto>> response = userController.newUser(role);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().data());
    }

    @Test
    @DisplayName("newUser - Should throw IllegalAccessException if role is invalid")
    void newUser_ShouldThrowException_WhenRoleInvalid() {
        assertThrows(IllegalAccessException.class, () -> userController.newUser("INVALID_ROLE"));
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

        ResponseEntity<ApiResponse<UserDto>> response = userController.update(userDto, userId);

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
}