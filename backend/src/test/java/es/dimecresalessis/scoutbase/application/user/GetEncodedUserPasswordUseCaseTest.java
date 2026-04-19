package es.dimecresalessis.scoutbase.application.user;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetEncodedUserPasswordUseCaseTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private GetEncodedUserPasswordUseCase getEncodedUserPasswordUseCase;

    @Test
    void execute_ShouldReturnUserWithEncodedPassword() {
        User user = User.builder().username("testuser").password("rawPassword").build();
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");

        User result = getEncodedUserPasswordUseCase.execute(user);

        assertEquals("encodedPassword", result.getPassword());
        assertEquals("testuser", result.getUsername());
        verify(passwordEncoder).encode("rawPassword");
    }
}