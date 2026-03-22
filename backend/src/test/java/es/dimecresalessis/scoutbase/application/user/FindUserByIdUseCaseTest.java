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
class FindUserByIdUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindUserByIdUseCase findUserByIdUseCase;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User(userId, "scout_master", "password123", "ADMIN");
    }

    @Test
    @DisplayName("Should return user when ID exists")
    void shouldFindUserById() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = findUserByIdUseCase.execute(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("scout_master", result.getUsername());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when user ID does not exist")
    void shouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            findUserByIdUseCase.execute(userId);
        });

        verify(userRepository, times(1)).findById(userId);
    }
}