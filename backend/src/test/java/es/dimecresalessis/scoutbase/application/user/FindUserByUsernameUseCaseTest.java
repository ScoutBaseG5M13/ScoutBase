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

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .username("scout_master")
                .password("encoded_password")
                .role("ADMIN")
                .name("Alex")
                .surname("Scout")
                .email("alex@scoutbase.com")
                .build();
    }

    @Test
    @DisplayName("Should return user when username exists")
    void shouldFindUserByUsername() {
        when(userRepository.findFirstByUsername(user.getUsername())).thenReturn(Optional.of(user));

        User result = findUserByUsernameUseCase.execute(user.getUsername());

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        verify(userRepository, times(1)).findFirstByUsername(user.getUsername());
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when username does not exist")
    void shouldThrowException_WhenUsernameNotFound() {
        when(userRepository.findFirstByUsername(user.getUsername())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            findUserByUsernameUseCase.execute(user.getUsername());
        });

        verify(userRepository, times(1)).findFirstByUsername(user.getUsername());
    }
}