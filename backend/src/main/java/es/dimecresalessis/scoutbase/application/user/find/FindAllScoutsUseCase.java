package es.dimecresalessis.scoutbase.application.user.find;

import es.dimecresalessis.scoutbase.application.userclub.find.FindAllUserClubsByUserUseCase;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Use case for finding all {@link User} scouters.
 */
@Service
@AllArgsConstructor
public class FindAllScoutsUseCase {

    private final UserTeamRepository userTeamRepository;
    private final FindUserByIdUseCase findUserByIdUseCase;

    public List<User> execute() {
        List<UserTeam> userTeams = userTeamRepository.findAll();
        List<User> scouters = new ArrayList<>();
        for (UserTeam userTeam : userTeams) {
            for (UUID scoutId : userTeam.getScouters()) {
                scouters.add(findUserByIdUseCase.execute(scoutId));
            }
        }
        return scouters;
    }
}
