package es.dimecresalessis.scoutbase.application.userteam;

import es.dimecresalessis.scoutbase.application.userteam.find.FindAllUserTeamsByUserUseCase;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllUserTeamsByUserUseCaseTest {

    @Mock
    private UserTeamRepository userTeamRepository;

    @InjectMocks
    private FindAllUserTeamsByUserUseCase findAllUserTeamsByUserUseCase;

    @Test
    void execute_ShouldReturnTeamsForUser() {
        UUID userId = UUID.randomUUID();
        when(userTeamRepository.findAllByUserId(userId)).thenReturn(List.of(new UserTeam()));

        List<UserTeam> result = findAllUserTeamsByUserUseCase.execute(userId);

        assertEquals(1, result.size());
        verify(userTeamRepository).findAllByUserId(userId);
    }
}