package es.dimecresalessis.scoutbase.application.team.find;

import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindTeamByIdUseCaseTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private FindTeamByIdUseCase findTeamByIdUseCase;

    private UUID teamId;
    private Team team;

    @BeforeEach
    void setUp() {
        teamId = UUID.randomUUID();
        team = Team.builder()
                .id(teamId)
                .name("Test Team")
                .build();
    }

    @Test
    void execute_ShouldReturnTeam_WhenIdExists() {
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        Team result = findTeamByIdUseCase.execute(teamId);

        assertNotNull(result);
        assertEquals(teamId, result.getId());
        verify(teamRepository).findById(teamId);
    }

    @Test
    void execute_ShouldReturnNull_WhenIdDoesNotExist() {
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        Team result = findTeamByIdUseCase.execute(teamId);

        assertNull(result);
        verify(teamRepository).findById(teamId);
    }
}