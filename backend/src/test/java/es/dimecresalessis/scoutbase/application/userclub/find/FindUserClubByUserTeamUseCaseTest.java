package es.dimecresalessis.scoutbase.application.userclub.find;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.userclub.exception.UserClubException;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserClubByUserTeamUseCaseTest {

    @Mock
    private UserClubRepository userClubRepository;

    @InjectMocks
    private FindUserClubByUserTeamUseCase findUserClubByUserTeamUseCase;

    @Test
    void execute_ShouldReturnUserClub_WhenTeamIdIsFound() {
        UUID teamId = UUID.randomUUID();
        UserClub club = UserClub.builder()
                .id(UUID.randomUUID())
                .userTeams(List.of(teamId))
                .build();

        when(userClubRepository.findAll()).thenReturn(List.of(club));

        UserClub result = findUserClubByUserTeamUseCase.execute(teamId);

        assertNotNull(result);
        assertEquals(club.getId(), result.getId());
        verify(userClubRepository).findAll();
    }

    @Test
    void execute_ShouldThrowException_WhenTeamIdIsNotFound() {
        UUID teamId = UUID.randomUUID();
        UserClub club = UserClub.builder()
                .id(UUID.randomUUID())
                .userTeams(List.of(UUID.randomUUID()))
                .build();

        when(userClubRepository.findAll()).thenReturn(List.of(club));

        UserClubException exception = assertThrows(UserClubException.class, () ->
                findUserClubByUserTeamUseCase.execute(teamId)
        );

        assertEquals(ErrorEnum.NO_USER_CLUB_HAS_BEEN_FOUND, exception.getErrorEnum());
    }

    @Test
    void execute_ShouldThrowException_WhenUserTeamsIsNull() {
        UUID teamId = UUID.randomUUID();
        UserClub club = UserClub.builder()
                .id(UUID.randomUUID())
                .userTeams(null)
                .build();

        when(userClubRepository.findAll()).thenReturn(List.of(club));

        assertThrows(UserClubException.class, () -> findUserClubByUserTeamUseCase.execute(teamId));
    }

    @Test
    void execute_ShouldThrowException_WhenRepositoryIsEmpty() {
        UUID teamId = UUID.randomUUID();
        when(userClubRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(UserClubException.class, () -> findUserClubByUserTeamUseCase.execute(teamId));
    }
}