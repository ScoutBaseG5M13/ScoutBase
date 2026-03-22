package es.dimecresalessis.scoutbase.application.user;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUserUseCase updateUserUseCase;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User(userId, "scout_master", "password123", "ADMIN");
    }

    @Test
    @DisplayName("Should update user successfully when IDs match and exist")
    void shouldUpdateUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = updateUserUseCase.execute(user, userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(2)).findById(userId);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when path ID and body ID do not match")
    void shouldThrowException_WhenIdsDoNotMatch() {
        UUID differentId = UUID.randomUUID();
        User differentUser = new User(differentId, "other_user", "pass", "USER");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(differentId)).thenReturn(Optional.of(differentUser));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            updateUserUseCase.execute(differentUser, userId);
        });

        assertEquals("User id does not match", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when user does not exist")
    void shouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            updateUserUseCase.execute(user, userId);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any());
    }
}