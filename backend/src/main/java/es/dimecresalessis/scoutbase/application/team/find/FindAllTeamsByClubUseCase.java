package es.dimecresalessis.scoutbase.application.team.find;

import es.dimecresalessis.scoutbase.application.club.find.FindClubByIdUseCase;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Use case for finding all {@link Team} of a {@link Club}.
 */
@Service
@AllArgsConstructor
public class FindAllTeamsByClubUseCase {

    private final TeamRepository teamRepository;
    private final FindClubByIdUseCase findClubByIdUseCase;

    /**
     * Executes the retrieval of all {@link Team} entities associated with a specific {@link Club}.
     *
     * @param clubId The {@link UUID} of the club whose teams are to be retrieved.
     * @return A {@link List} of {@link Team} objects belonging to the club. If a team ID
     * referenced by the club does not exist in the repository, it is excluded from the list.
     */
    public List<Team> execute(UUID clubId) {
        List<Team> teams = new ArrayList<>();
        Club club = findClubByIdUseCase.execute(clubId);
        for (UUID teamId: club.getTeams()) {
            Team team = teamRepository.findById(teamId).orElse(null);
            if (team != null) {
                teams.add(team);
            }
        }
        return teams;
    }
}
