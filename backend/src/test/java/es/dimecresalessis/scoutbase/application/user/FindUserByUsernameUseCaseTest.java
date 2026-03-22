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
class FindUserByUsernameUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindUserByUsernameUseCase findUserByUsernameUseCase;

    private User user;
    private String username;

    @BeforeEach
    void setUp() {
        username = "scout_master";
        user = new User(UUID.randomUUID(), username, "password123", "ADMIN");
    }

    @Test
    @DisplayName("Should return user when username exists")
    void shouldFindUserByUsername() {
        when(userRepository.findFirstByUsername(username)).thenReturn(Optional.of(user));

        User result = findUserByUsernameUseCase.execute(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository, times(1)).findFirstByUsername(username);
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when username does not exist")
    void shouldThrowException_WhenUsernameNotFound() {
        when(userRepository.findFirstByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            findUserByUsernameUseCase.execute(username);
        });

        verify(userRepository, times(1)).findFirstByUsername(username);
    }
}