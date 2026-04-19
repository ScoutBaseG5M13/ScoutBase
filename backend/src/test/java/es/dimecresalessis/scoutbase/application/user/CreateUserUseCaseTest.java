package es.dimecresalessis.scoutbase.application.user;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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

    @Mock private SaveUserUseCase saveUserUseCase;
    @Mock private GetEncodedUserPasswordUseCase getEncodedUserPasswordUseCase;
    @Mock private UserRepository userRepository;

    @InjectMocks private CreateUserUseCase createUserUseCase;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder().id(userId).username("scout_master").password("1234").build();
    }

    @Test
    void execute_ShouldCreateUserSuccessfully() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(userRepository.findByUsername("scout_master")).thenReturn(Optional.empty());
        when(getEncodedUserPasswordUseCase.execute(user)).thenReturn(user);
        when(saveUserUseCase.execute(user)).thenReturn(user);

        User result = createUserUseCase.execute(user);

        assertNotNull(result);
        verify(saveUserUseCase).execute(user);
    }

    @Test
    void execute_ShouldThrowException_WhenUsernameExists() {
        when(userRepository.findByUsername("scout_master")).thenReturn(Optional.of(user));

        UserException ex = assertThrows(UserException.class, () -> createUserUseCase.execute(user));
        assertEquals(ErrorEnum.USERNAME_ALREADY_EXISTS, ex.getErrorEnum());
    }

    @Test
    void execute_ShouldThrowException_WhenIdAlreadyExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserException ex = assertThrows(UserException.class, () -> createUserUseCase.execute(user));
        assertEquals(ErrorEnum.USER_ID_ALREADY_EXISTS, ex.getErrorEnum());
    }
}