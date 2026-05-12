package es.dimecresalessis.scoutbase.application.security;

import es.dimecresalessis.scoutbase.application.user.find.FindUserByUsernameUseCase;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private FindUserByUsernameUseCase findUserByUsernameUseCase;

    @InjectMocks
    private AuthService authService;

    private final String USERNAME = "scout_user";
    private final String PASSWORD = "password123";
    private final String TOKEN = "mocked-jwt-token";

    @Test
    void shouldReturnTokenWhenAuthenticationIsSuccessful() {
        User mockUser = mock(User.class);
        when(findUserByUsernameUseCase.execute(USERNAME)).thenReturn(mockUser);
        when(jwtService.createToken(mockUser)).thenReturn(TOKEN);

        String resultToken = authService.authenticateAndGenerateToken(USERNAME, PASSWORD);

        assertEquals(TOKEN, resultToken);
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(findUserByUsernameUseCase).execute(USERNAME);
        verify(jwtService).createToken(mockUser);
    }

    @Test
    void shouldThrowExceptionWhenAuthenticationFails() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () ->
                authService.authenticateAndGenerateToken(USERNAME, PASSWORD));

        verifyNoInteractions(findUserByUsernameUseCase);
        verifyNoInteractions(jwtService);
    }
}