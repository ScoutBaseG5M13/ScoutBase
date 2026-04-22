package es.dimecresalessis.scoutbase.application.team.update;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.team.exception.TeamException;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for updating a {@link Team}.
 */
@Service
@AllArgsConstructor
public class UpdateTeamUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UpdateTeamUseCase.class);
    private final TeamRepository teamRepository;

    /**
     * Executes the updating process for a specific Team.
     *
     * @param team The {@link Team} entity containing the updated information.
     * @param teamId The unique identifier ({@link UUID}) of the team being targeted.
     * @return The updated and persisted {@link Team} entity.
     * @throws TeamException if the team is not found in the repository.
     * @throws IllegalArgumentException if the ID within the team object does not match the path ID.
     */
    public Team execute(Team team, UUID teamId) {
        validateAndRetrieveTeam(team, teamId);
        teamRepository.save(team);
        logger.info("[UPDATE] Updated Player '{}'", team.getId());
        return team;
    }

    /**
     * Validates that both the ID from the request body and the ID from the
     * path exist in the database and match each other.
     *
     * @param team The team object from the request.
     * @param id The ID provided in the application context/path.
     * @throws TeamException if either ID does not correspond to an existing team.
     * @throws IllegalArgumentException if there is a mismatch between the IDs.
     */
    private void validateAndRetrieveTeam(Team team, UUID id) {
        Team bodyTeam = teamRepository.findById(team.getId()).orElseThrow(
                () -> new TeamException(ErrorEnum.TEAM_NOT_FOUND, team.getId().toString())
        );

        Team idTeam = teamRepository.findById(id).orElseThrow(
                () -> new TeamException(ErrorEnum.TEAM_NOT_FOUND, id.toString())
        );

        if (!bodyTeam.getId().toString().equals(idTeam.getId().toString())) {
            throw new IllegalArgumentException("Team id " + bodyTeam.getId() + " does not match " + idTeam.getId());
        }
    }
}
