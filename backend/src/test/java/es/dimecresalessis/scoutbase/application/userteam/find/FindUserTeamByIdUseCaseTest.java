package es.dimecresalessis.scoutbase.application.userteam.find;

import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
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
class FindUserTeamByIdUseCaseTest {

    @Mock
    private UserTeamRepository userTeamRepository;

    @InjectMocks
    private FindUserTeamByIdUseCase findUserTeamByIdUseCase;

    private UUID teamId;
    private UserTeam userTeam;

    @BeforeEach
    void setUp() {
        teamId = UUID.randomUUID();
        userTeam = UserTeam.builder()
                .id(teamId)
                .name("Scout Team")
                .build();
    }

    @Test
    void execute_ShouldReturnUserTeam_WhenIdExists() {
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.of(userTeam));

        UserTeam result = findUserTeamByIdUseCase.execute(teamId);

        assertNotNull(result);
        assertEquals(teamId, result.getId());
        assertEquals("Scout Team", result.getName());
        verify(userTeamRepository).findById(teamId);
    }

    @Test
    void execute_ShouldReturnNull_WhenIdDoesNotExist() {
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.empty());

        UserTeam result = findUserTeamByIdUseCase.execute(teamId);

        assertNull(result);
        verify(userTeamRepository).findById(teamId);
    }
}