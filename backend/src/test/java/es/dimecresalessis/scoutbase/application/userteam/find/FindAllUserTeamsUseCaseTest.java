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
class FindAllUserTeamsUseCaseTest {

    @Mock
    private UserTeamRepository userTeamRepository;

    @InjectMocks
    private FindAllUserTeamsUseCase findAllUserTeamsUseCase;

    @Test
    void execute_ShouldReturnAllUserTeams() {
        UserTeam team1 = UserTeam.builder().id(UUID.randomUUID()).name("Team 1").build();
        UserTeam team2 = UserTeam.builder().id(UUID.randomUUID()).name("Team 2").build();
        List<UserTeam> expectedTeams = List.of(team1, team2);

        when(userTeamRepository.findAll()).thenReturn(expectedTeams);

        List<UserTeam> result = findAllUserTeamsUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedTeams, result);
        verify(userTeamRepository).findAll();
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenNoTeamsExist() {
        when(userTeamRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserTeam> result = findAllUserTeamsUseCase.execute();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(userTeamRepository).findAll();
    }
}