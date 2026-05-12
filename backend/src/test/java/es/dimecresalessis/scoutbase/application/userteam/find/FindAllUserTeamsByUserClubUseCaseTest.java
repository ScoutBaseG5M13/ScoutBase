package es.dimecresalessis.scoutbase.application.userteam.find;

import es.dimecresalessis.scoutbase.application.userclub.find.FindUserClubByIdUseCase;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllUserTeamsByUserClubUseCaseTest {

    @Mock
    private UserTeamRepository userTeamRepository;

    @Mock
    private FindUserClubByIdUseCase findUserClubByIdUseCase;

    @InjectMocks
    private FindAllUserTeamsByUserClubUseCase findAllUserTeamsByUserClubUseCase;

    @Test
    void execute_ShouldReturnTeams_WhenClubHasTeams() {
        UUID clubId = UUID.randomUUID();
        UUID teamId1 = UUID.randomUUID();
        UUID teamId2 = UUID.randomUUID();
        UserClub userClub = UserClub.builder()
                .id(clubId)
                .userTeams(List.of(teamId1, teamId2))
                .build();
        UserTeam team1 = UserTeam.builder().id(teamId1).name("Team 1").build();
        UserTeam team2 = UserTeam.builder().id(teamId2).name("Team 2").build();

        when(findUserClubByIdUseCase.execute(clubId)).thenReturn(userClub);
        when(userTeamRepository.findById(teamId1)).thenReturn(Optional.of(team1));
        when(userTeamRepository.findById(teamId2)).thenReturn(Optional.of(team2));

        List<UserTeam> result = findAllUserTeamsByUserClubUseCase.execute(clubId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Team 1", result.get(0).getName());
        assertEquals("Team 2", result.get(1).getName());
    }

    @Test
    void execute_ShouldSkipNullTeams_WhenRepositoryReturnsEmpty() {
        UUID clubId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        UserClub userClub = UserClub.builder()
                .id(clubId)
                .userTeams(List.of(teamId))
                .build();

        when(findUserClubByIdUseCase.execute(clubId)).thenReturn(userClub);
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.empty());

        List<UserTeam> result = findAllUserTeamsByUserClubUseCase.execute(clubId);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenClubHasNoTeams() {
        UUID clubId = UUID.randomUUID();
        UserClub userClub = UserClub.builder()
                .id(clubId)
                .userTeams(null)
                .build();

        when(findUserClubByIdUseCase.execute(clubId)).thenReturn(userClub);

        List<UserTeam> result = findAllUserTeamsByUserClubUseCase.execute(clubId);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(userTeamRepository, never()).findById(any());
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenTeamListIsEmpty() {
        UUID clubId = UUID.randomUUID();
        UserClub userClub = UserClub.builder()
                .id(clubId)
                .userTeams(Collections.emptyList())
                .build();

        when(findUserClubByIdUseCase.execute(clubId)).thenReturn(userClub);

        List<UserTeam> result = findAllUserTeamsByUserClubUseCase.execute(clubId);

        assertNotNull(result);
        assertEquals(0, result.size());
    }
}