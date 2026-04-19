package es.dimecresalessis.scoutbase.application.user;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
        user = User.builder()
                .id(userId)
                .username("scout_master")
                .name("Alex")
                .build();
    }

    @Test
    void execute_ShouldReturnUser_WhenIdExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = findUserByIdUseCase.execute(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("scout_master", result.getUsername());
        verify(userRepository).findById(userId);
    }

    @Test
    void execute_ShouldThrowNoSuchElementException_WhenIdDoesNotExist() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                findUserByIdUseCase.execute(userId)
        );

        verify(userRepository).findById(userId);
    }
}