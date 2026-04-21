package es.dimecresalessis.scoutbase.application.user.find;

import es.dimecresalessis.scoutbase.application.club.find.FindAllClubsByUserUseCase;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
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

    private static final Logger logger = LoggerFactory.getLogger(FindAllClubsByUserUseCase.class);
    private final TeamRepository teamRepository;
    private final FindUserByIdUseCase findUserByIdUseCase;

    public List<User> execute() {
        List<Team> teams = teamRepository.findAll();
        List<User> scouters = new ArrayList<>();
        for (Team team : teams) {
            for (UUID scoutId : team.getScouters()) {
                scouters.add(findUserByIdUseCase.execute(scoutId));
            }
        }
        return scouters;
    }
}
