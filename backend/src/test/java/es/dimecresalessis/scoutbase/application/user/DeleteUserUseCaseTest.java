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
class DeleteUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeleteUserUseCase deleteUserUseCase;

    private UUID userId;
    private User user;

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
    }

    @Test
    @DisplayName("Should delete user successfully when it exists")
    void shouldDeleteUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        boolean result = deleteUserUseCase.execute(userId);

        assertTrue(result);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when user does not exist")
    void shouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            deleteUserUseCase.execute(userId);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).deleteById(any());
    }
}