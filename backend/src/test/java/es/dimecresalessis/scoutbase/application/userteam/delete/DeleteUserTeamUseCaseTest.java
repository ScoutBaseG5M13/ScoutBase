package es.dimecresalessis.scoutbase.application.userteam.delete;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserTeamUseCaseTest {

    @Mock
    private UserTeamRepository userTeamRepository;

    @Mock
    private UserClubRepository userClubRepository;

    @InjectMocks
    private DeleteUserTeamUseCase deleteUserTeamUseCase;

    @Test
    void execute_ShouldDeleteTeamAndRemoveFromClub_WhenTeamExists() {
        UUID teamId = UUID.randomUUID();
        UserTeam userTeam = UserTeam.builder().id(teamId).build();

        List<UUID> teamsInClub = new ArrayList<>();
        teamsInClub.add(teamId);
        UserClub club = UserClub.builder()
                .id(UUID.randomUUID())
                .name("Test Club")
                .userTeams(teamsInClub)
                .build();

        when(userTeamRepository.findById(teamId)).thenReturn(Optional.of(userTeam));
        when(userClubRepository.findUserClubByTeam(teamId)).thenReturn(Optional.of(club));

        boolean result = deleteUserTeamUseCase.execute(teamId);

        assertTrue(result);
        assertFalse(club.getUserTeams().contains(teamId));
        verify(userTeamRepository).deleteById(teamId);
        verify(userClubRepository).save(club);
    }

    @Test
    void execute_ShouldDeleteTeamEvenIfNoClubIsAssociated() {
        UUID teamId = UUID.randomUUID();
        UserTeam userTeam = UserTeam.builder().id(teamId).build();

        when(userTeamRepository.findById(teamId)).thenReturn(Optional.of(userTeam));
        when(userClubRepository.findUserClubByTeam(teamId)).thenReturn(Optional.empty());

        boolean result = deleteUserTeamUseCase.execute(teamId);

        assertTrue(result);
        verify(userTeamRepository).deleteById(teamId);
        verify(userClubRepository, never()).save(any());
    }

    @Test
    void execute_ShouldThrowException_WhenTeamDoesNotExist() {
        UUID teamId = UUID.randomUUID();
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> deleteUserTeamUseCase.execute(teamId));

        verify(userTeamRepository, never()).deleteById(any());
        verifyNoInteractions(userClubRepository);
    }
}