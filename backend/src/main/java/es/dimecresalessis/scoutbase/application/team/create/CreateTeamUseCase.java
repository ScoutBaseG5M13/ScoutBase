package es.dimecresalessis.scoutbase.application.team.create;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.team.exception.TeamException;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import es.dimecresalessis.scoutbase.domain.userclub.exception.UserClubException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Use case for creating a {@link Team}.
 */
@Service
@AllArgsConstructor
public class CreateTeamUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateTeamUseCase.class);
    private final TeamRepository teamRepository;
    private final ClubRepository clubRepository;

    /**
     * Executes the creation of a Team and updates the corresponding club.
     * @param team The {@link Team} entity to be created.
     * @param clubId The {@link Club} entity that will own the new team.
     * @return The persisted {@link Team} entity.
     * @throws TeamException if the team is null, the ID is missing, or if a team with
     * the same ID already exists.
     */
    public Team execute(Team team, UUID clubId) {
        if (team == null) {
            throw new TeamException(ErrorEnum.TEAM_IS_NULL);
        }

        if (team.getId() == null) {
            throw new TeamException(ErrorEnum.TEAM_ID_IS_NULL);
        }

        if (teamRepository.findById(team.getId()).isPresent()) {
            throw new TeamException(ErrorEnum.TEAM_ALREADY_EXISTS, team.getId().toString());
        }

        Optional<Club> club = clubRepository.findById(clubId);
        if (club.isEmpty()) {
            throw new UserClubException(ErrorEnum.CLUB_NOT_FOUND, club.toString());
        }
        team.setClubId(club.get().getId());
        teamRepository.save(team);
        logger.info("[CREATE] Created Team '{}'", team.getId());

        club.get().getTeams().add(team.getId());
        clubRepository.save(club.get());
        logger.info("[UPDATE] Updated Club '{}' and added the Team {}", team.getId(), club.get().getId());

        return team;
    }
}
