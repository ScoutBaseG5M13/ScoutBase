package es.dimecresalessis.scoutbase.application.team;

import es.dimecresalessis.scoutbase.application.team.create.CreateTeamUseCase;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.team.exception.TeamException;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTeamUseCaseTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private CreateTeamUseCase createTeamUseCase;

    private Team team;
    private UUID teamId;
    private Club club;
    private UUID clubId;

    @BeforeEach
    void setUp() {
        teamId = UUID.randomUUID();
        clubId = UUID.randomUUID(); // Assign a value

        team = Team.builder()
                .id(teamId)
                .name("Team A")
                .build();

        club = Club.builder()
                .id(clubId)
                .name("Club A")
                .teams(new ArrayList<>()) // Ensure list is present
                .build();
    }

    @Test
    void execute_ShouldCreateTeamSuccessfully() {
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());
        when(teamRepository.save(team)).thenReturn(team);

        Team result = createTeamUseCase.execute(team, club);

        assertNotNull(result);
        assertEquals(teamId, result.getId());
        verify(teamRepository).save(team);
    }

    @Test
    void execute_ShouldThrowException_WhenTeamIsNull() {
        TeamException exception = assertThrows(TeamException.class, () ->
                createTeamUseCase.execute(null, null)
        );
        assertEquals(ErrorEnum.TEAM_IS_NULL, exception.getErrorEnum());
    }

    @Test
    void execute_ShouldThrowException_WhenTeamIdIsNull() {
        Team teamNoId = new Team();
        teamNoId.setId(null);

        TeamException exception = assertThrows(TeamException.class, () ->
                createTeamUseCase.execute(teamNoId, null)
        );
        assertEquals(ErrorEnum.TEAM_ID_IS_NULL, exception.getErrorEnum());
    }

    @Test
    void execute_ShouldThrowException_WhenTeamAlreadyExists() {
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        TeamException exception = assertThrows(TeamException.class, () ->
                createTeamUseCase.execute(team, null)
        );
        assertEquals(ErrorEnum.TEAM_ALREADY_EXISTS, exception.getErrorEnum());
        verify(teamRepository, never()).save(any());
    }
}