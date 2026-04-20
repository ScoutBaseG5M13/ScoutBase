package es.dimecresalessis.scoutbase.infrastructure.security;

import es.dimecresalessis.scoutbase.application.user.find.FindUserByUsernameUseCase;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DomainUserDetailsServiceTest {

    @Mock
    private FindUserByUsernameUseCase findUserByUsernameUseCase;

    @InjectMocks
    private DomainUserDetailsService domainUserDetailsService;

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        String username = "tester";
        String password = "encodedPassword";
        User domainUser = mock(User.class);

        when(domainUser.getUsername()).thenReturn(username);
        when(domainUser.getPassword()).thenReturn(password);
        when(findUserByUsernameUseCase.execute(username)).thenReturn(domainUser);

        UserDetails userDetails = domainUserDetailsService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        verify(findUserByUsernameUseCase).execute(username);
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUseCaseFails() {
        String username = "unknown";
        when(findUserByUsernameUseCase.execute(username)).thenThrow(new RuntimeException("Database down"));

        assertThrows(UsernameNotFoundException.class, () ->
                domainUserDetailsService.loadUserByUsername(username));

        verify(findUserByUsernameUseCase).execute(username);
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserIsNull() {
        String username = "nullUser";
        when(findUserByUsernameUseCase.execute(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () ->
                domainUserDetailsService.loadUserByUsername(username));
    }
}