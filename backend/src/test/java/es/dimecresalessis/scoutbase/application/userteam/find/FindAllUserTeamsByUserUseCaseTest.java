package es.dimecresalessis.scoutbase.application.userteam.find;

import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllUserTeamsByUserUseCaseTest {

    @Mock
    private UserTeamRepository userTeamRepository;

    @InjectMocks
    private FindAllUserTeamsByUserUseCase findAllUserTeamsByUserUseCase;

    @Test
    void execute_ShouldReturnTeams_WhenUserIsFoundInTeams() {
        UUID userId = UUID.randomUUID();
        UserTeam team1 = UserTeam.builder().id(UUID.randomUUID()).name("Team A").build();
        UserTeam team2 = UserTeam.builder().id(UUID.randomUUID()).name("Team B").build();
        List<UserTeam> expectedTeams = List.of(team1, team2);

        when(userTeamRepository.findAllByUserId(userId)).thenReturn(expectedTeams);

        List<UserTeam> result = findAllUserTeamsByUserUseCase.execute(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedTeams, result);
        verify(userTeamRepository).findAllByUserId(userId);
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenUserIsNotInAnyTeam() {
        UUID userId = UUID.randomUUID();
        when(userTeamRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

        List<UserTeam> result = findAllUserTeamsByUserUseCase.execute(userId);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(userTeamRepository).findAllByUserId(userId);
    }
}