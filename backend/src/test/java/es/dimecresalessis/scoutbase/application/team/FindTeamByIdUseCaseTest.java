package es.dimecresalessis.scoutbase.application.team;

import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindTeamByIdUseCaseTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private FindTeamByIdUseCase findTeamByIdUseCase;

    @Test
    void execute_ShouldReturnTeam_WhenIdExists() {
        UUID id = UUID.randomUUID();
        Team team = Team.builder().id(id).build();
        when(teamRepository.findById(id)).thenReturn(Optional.of(team));

        Team result = findTeamByIdUseCase.execute(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }
}
