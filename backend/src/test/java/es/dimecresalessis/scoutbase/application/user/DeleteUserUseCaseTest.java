package es.dimecresalessis.scoutbase.application.user;

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
class DeleteUserUseCaseTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private DeleteUserUseCase deleteUserUseCase;

    @Test
    void execute_ShouldDeleteUser_WhenExists() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(new User()));

        boolean result = deleteUserUseCase.execute(id);

        assertTrue(result);
        verify(userRepository).deleteById(id);
    }
}