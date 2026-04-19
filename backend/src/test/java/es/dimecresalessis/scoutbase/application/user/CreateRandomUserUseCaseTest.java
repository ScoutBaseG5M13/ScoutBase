package es.dimecresalessis.scoutbase.application.user;

import es.dimecresalessis.scoutbase.application.user.create.CreateRandomUserUseCase;
import es.dimecresalessis.scoutbase.application.user.create.SaveUserUseCase;
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
class CreateRandomUserUseCaseTest {

    @Mock
    private SaveUserUseCase saveUserUseCase;

    @Mock
    private GetEncodedUserPasswordUseCase getEncodedUserPasswordUseCase;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateRandomUserUseCase createRandomUserUseCase;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder()
                .id(userId)
                .username("random_user")
                .password("raw_pass")
                .build();
    }

    @Test
    void execute_ShouldCreateRandomUserSuccessfully() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(userRepository.findByUsername("random_user")).thenReturn(Optional.empty());
        when(getEncodedUserPasswordUseCase.execute(user)).thenReturn(user);

        User result = createRandomUserUseCase.execute(user);

        assertNotNull(result);
        verify(getEncodedUserPasswordUseCase).execute(user);
        verify(saveUserUseCase).execute(user);
    }

    @Test
    void execute_ShouldThrowException_WhenUserIdAlreadyExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserException exception = assertThrows(UserException.class, () ->
                createRandomUserUseCase.execute(user)
        );

        assertEquals(ErrorEnum.USER_ID_ALREADY_EXISTS, exception.getErrorEnum());
        verifyNoInteractions(getEncodedUserPasswordUseCase);
        verifyNoInteractions(saveUserUseCase);
    }

    @Test
    void execute_ShouldThrowException_WhenUsernameAlreadyExists() {
        User userNoId = User.builder().username("existing_user").build();
        userNoId.setId(null);

        when(userRepository.findByUsername("existing_user")).thenReturn(Optional.of(new User()));

        UserException exception = assertThrows(UserException.class, () ->
                createRandomUserUseCase.execute(userNoId)
        );

        assertEquals(ErrorEnum.USERNAME_ALREADY_EXISTS, exception.getErrorEnum());
    }
}