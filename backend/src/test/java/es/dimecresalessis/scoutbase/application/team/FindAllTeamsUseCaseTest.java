package es.dimecresalessis.scoutbase.application.team;

import es.dimecresalessis.scoutbase.application.team.find.FindAllTeamsUseCase;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllTeamsUseCaseTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private FindAllTeamsUseCase findAllTeamsUseCase;

    @Test
    void execute_ShouldReturnAllTeams() {
        when(teamRepository.findAll()).thenReturn(List.of(new Team(), new Team()));
        List<Team> result = findAllTeamsUseCase.execute();
        assertEquals(2, result.size());
    }
}