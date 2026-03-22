package es.dimecresalessis.scoutbase.application.user;

import es.dimecresalessis.scoutbase.application.security.RegistrationService;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User(userId, "scout_master", "password123", "ADMIN");
    }

    @Test
    @DisplayName("Should create user successfully when username and ID are unique")
    void shouldCreateUser() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(registrationService.createUser(user)).thenReturn(user);

        User result = createUserUseCase.execute(user);

        assertNotNull(result);
        assertEquals("scout_master", result.getUsername());
        verify(userRepository).findByUsername("scout_master");
        verify(registrationService).createUser(user);
    }

    @Test
    @DisplayName("Should throw UserException when user object is null")
    void shouldThrowException_WhenUserIsNull() {
        UserException exception = assertThrows(UserException.class, () -> {
            createUserUseCase.execute(null);
        });

        assertEquals(ErrorEnum.USER_IS_NULL, exception.getErrorEnum());
        verifyNoInteractions(userRepository, registrationService);
    }

    @Test
    @DisplayName("Should throw UserException when username already exists")
    void shouldThrowException_WhenUsernameExists() {
        when(userRepository.findByUsername("scout_master")).thenReturn(Optional.of(user));

        UserException exception = assertThrows(UserException.class, () -> {
            createUserUseCase.execute(user);
        });

        assertEquals(ErrorEnum.USERNAME_ALREADY_EXISTS, exception.getErrorEnum());
        verify(registrationService, never()).createUser(any());
    }

    @Test
    @DisplayName("Should throw UserException when ID already exists")
    void shouldThrowException_WhenIdExists() {
        when(userRepository.findByUsername("scout_master")).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserException exception = assertThrows(UserException.class, () -> {
            createUserUseCase.execute(user);
        });

        assertEquals(ErrorEnum.USER_ID_ALREADY_EXISTS, exception.getErrorEnum());
        verify(registrationService, never()).createUser(any());
    }
}