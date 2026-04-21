package es.dimecresalessis.scoutbase.application.team.find;

import es.dimecresalessis.scoutbase.application.club.find.FindClubByIdUseCase;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(FindAllTeamsByClubUseCase.class);
    private final TeamRepository teamRepository;
    private final FindClubByIdUseCase findClubByIdUseCase;

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
