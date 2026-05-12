package es.dimecresalessis.scoutbase.application.team.find;

import es.dimecresalessis.scoutbase.application.club.find.FindClubByIdUseCase;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllTeamsByClubUseCaseTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private FindClubByIdUseCase findClubByIdUseCase;

    @InjectMocks
    private FindAllTeamsByClubUseCase findAllTeamsByClubUseCase;

    private UUID clubId;
    private UUID teamId;
    private Club club;
    private Team team;

    @BeforeEach
    void setUp() {
        clubId = UUID.randomUUID();
        teamId = UUID.randomUUID();
        club = Club.builder()
                .id(clubId)
                .teams(List.of(teamId))
                .build();
        team = Team.builder()
                .id(teamId)
                .clubId(clubId)
                .name("Test Team")
                .build();
    }

    @Test
    void execute_ShouldReturnTeams_WhenClubAndTeamsExist() {
        when(findClubByIdUseCase.execute(clubId)).thenReturn(club);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        List<Team> result = findAllTeamsByClubUseCase.execute(clubId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(teamId, result.get(0).getId());
        verify(findClubByIdUseCase).execute(clubId);
        verify(teamRepository).findById(teamId);
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenTeamsReferencedDoNotExist() {
        when(findClubByIdUseCase.execute(clubId)).thenReturn(club);
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        List<Team> result = findAllTeamsByClubUseCase.execute(clubId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(teamRepository).findById(teamId);
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenClubHasNoTeams() {
        Club clubWithoutTeams = Club.builder()
                .id(clubId)
                .teams(List.of())
                .build();

        when(findClubByIdUseCase.execute(clubId)).thenReturn(clubWithoutTeams);

        List<Team> result = findAllTeamsByClubUseCase.execute(clubId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(teamRepository);
    }

    @Test
    void execute_ShouldThrowException_WhenClubUseCaseThrowsException() {
        when(findClubByIdUseCase.execute(clubId)).thenThrow(new RuntimeException("Club not found"));

        assertThrows(RuntimeException.class, () -> findAllTeamsByClubUseCase.execute(clubId));
        verifyNoInteractions(teamRepository);
    }
}