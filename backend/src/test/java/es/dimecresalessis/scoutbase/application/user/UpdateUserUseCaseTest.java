package es.dimecresalessis.scoutbase.application.user;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
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
class UpdateUserUseCaseTest {

    @Mock private UserRepository userRepository;
    @Mock private GetEncodedUserPasswordUseCase getEncodedUserPasswordUseCase;

    @InjectMocks private UpdateUserUseCase updateUserUseCase;

    @Test
    void execute_ShouldUpdateSuccessfully() {
        UUID id = UUID.randomUUID();
        User user = User.builder().id(id).username("updated").build();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(getEncodedUserPasswordUseCase.execute(user)).thenReturn(user);

        User result = updateUserUseCase.execute(user, id);

        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void execute_ShouldThrowException_WhenIdsDoNotMatch() {
        UUID bodyId = UUID.randomUUID();
        UUID pathId = UUID.randomUUID();
        User user = User.builder().id(bodyId).username("user").build();

        UserException ex = assertThrows(UserException.class, () -> updateUserUseCase.execute(user, pathId));
        assertEquals(ErrorEnum.USER_ID_DOES_NOT_MATCH, ex.getErrorEnum());
    }
}