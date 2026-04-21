package es.dimecresalessis.scoutbase.application.user;

import es.dimecresalessis.scoutbase.application.user.find.FindUserByUsernameUseCase;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserByUsernameUseCaseTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private FindUserByUsernameUseCase findUserByUsernameUseCase;

    @Test
    void execute_ShouldReturnUser_WhenUsernameExists() {
        String username = "admin";
        User user = User.builder().username(username).build();
        when(userRepository.findFirstByUsername(username)).thenReturn(Optional.of(user));

        User result = findUserByUsernameUseCase.execute(username);

        assertEquals(username, result.getUsername());
    }

    @Test
    void execute_ShouldThrowException_WhenNotFound() {
        when(userRepository.findFirstByUsername("none")).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> findUserByUsernameUseCase.execute("none"));
    }
}