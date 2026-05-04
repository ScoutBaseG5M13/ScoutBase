package es.dimecresalessis.scoutbase.application.userteam;

import es.dimecresalessis.scoutbase.application.userteam.create.CreateUserTeamUseCase;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.userteam.exception.UserTeamException;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
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
class CreateUserUserTeamUseCaseTest {

    @Mock
    private UserTeamRepository userTeamRepository;

    @Mock
    private UserClubRepository userClubRepository;

    @InjectMocks
    private CreateUserTeamUseCase createUserTeamUseCase;

    private UserTeam userTeam;
    private UUID teamId;
    private UserClub userClub;
    private UUID clubId;

    @BeforeEach
    void setUp() {
        teamId = UUID.randomUUID();
        clubId = UUID.randomUUID(); // Assign a value

        userTeam = UserTeam.builder()
                .id(teamId)
                .name("Team A")
                .build();

        userClub = UserClub.builder()
                .id(clubId)
                .name("Club A")
                .teams(new ArrayList<>()) // Ensure list is present
                .build();
    }

    @Test
    void execute_ShouldCreateTeamSuccessfully() {
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.empty());
        when(userTeamRepository.save(userTeam)).thenReturn(userTeam);

        UserTeam result = createUserTeamUseCase.execute(userTeam, userClub);

        assertNotNull(result);
        assertEquals(teamId, result.getId());
        verify(userTeamRepository).save(userTeam);
    }

    @Test
    void execute_ShouldThrowException_WhenTeamIsNull() {
        UserTeamException exception = assertThrows(UserTeamException.class, () ->
                createUserTeamUseCase.execute(null, null)
        );
        assertEquals(ErrorEnum.TEAM_IS_NULL, exception.getErrorEnum());
    }

    @Test
    void execute_ShouldThrowException_WhenTeamIdIsNull() {
        UserTeam userTeamNoId = new UserTeam();
        userTeamNoId.setId(null);

        UserTeamException exception = assertThrows(UserTeamException.class, () ->
                createUserTeamUseCase.execute(userTeamNoId, null)
        );
        assertEquals(ErrorEnum.TEAM_ID_IS_NULL, exception.getErrorEnum());
    }

    @Test
    void execute_ShouldThrowException_WhenTeamAlreadyExists() {
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.of(userTeam));

        UserTeamException exception = assertThrows(UserTeamException.class, () ->
                createUserTeamUseCase.execute(userTeam, null)
        );
        assertEquals(ErrorEnum.TEAM_ALREADY_EXISTS, exception.getErrorEnum());
        verify(userTeamRepository, never()).save(any());
    }
}