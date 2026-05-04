package es.dimecresalessis.scoutbase.application.userteam;

import es.dimecresalessis.scoutbase.application.userteam.find.FindAllUserTeamsUseCase;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllUserTeamsUseCaseTest {

    @Mock
    private UserTeamRepository userTeamRepository;

    @InjectMocks
    private FindAllUserTeamsUseCase findAllUserTeamsUseCase;

    @Test
    void execute_ShouldReturnAllTeams() {
        when(userTeamRepository.findAll()).thenReturn(List.of(new UserTeam(), new UserTeam()));
        List<UserTeam> result = findAllUserTeamsUseCase.execute();
        assertEquals(2, result.size());
    }
}