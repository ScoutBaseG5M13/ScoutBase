package es.dimecresalessis.scoutbase.application.userteam;

import es.dimecresalessis.scoutbase.application.userteam.find.FindUserTeamByIdUseCase;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserUserTeamByIdUseCaseTest {

    @Mock
    private UserTeamRepository userTeamRepository;

    @InjectMocks
    private FindUserTeamByIdUseCase findUserTeamByIdUseCase;

    @Test
    void execute_ShouldReturnTeam_WhenIdExists() {
        UUID id = UUID.randomUUID();
        UserTeam userTeam = UserTeam.builder().id(id).build();
        when(userTeamRepository.findById(id)).thenReturn(Optional.of(userTeam));

        UserTeam result = findUserTeamByIdUseCase.execute(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }
}
