package es.dimecresalessis.scoutbase.application.team;

import es.dimecresalessis.scoutbase.application.team.find.FindAllTeamsByUserUseCase;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllTeamsByUserUseCaseTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private FindAllTeamsByUserUseCase findAllTeamsByUserUseCase;

    @Test
    void execute_ShouldReturnTeamsForUser() {
        UUID userId = UUID.randomUUID();
        when(teamRepository.findAllByUserId(userId)).thenReturn(List.of(new Team()));

        List<Team> result = findAllTeamsByUserUseCase.execute(userId);

        assertEquals(1, result.size());
        verify(teamRepository).findAllByUserId(userId);
    }
}