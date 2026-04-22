package es.dimecresalessis.scoutbase.application.user;

import es.dimecresalessis.scoutbase.application.user.create.SaveUserUseCase;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SaveUserUseCase saveUserUseCase;

    @Test
    void execute_ShouldCallRepositorySaveAndReturnUser() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("persisted_user")
                .build();

        when(userRepository.save(user)).thenReturn(user);

        User result = saveUserUseCase.execute(user);

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        verify(userRepository, times(1)).save(user);
    }
}