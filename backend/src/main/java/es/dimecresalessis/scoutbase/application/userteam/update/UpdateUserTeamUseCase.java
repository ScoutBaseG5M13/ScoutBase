package es.dimecresalessis.scoutbase.application.userteam.update;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.userteam.exception.UserTeamException;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for updating a {@link UserTeam}.
 */
@Service
@AllArgsConstructor
public class UpdateUserTeamUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UpdateUserTeamUseCase.class);
    private final UserTeamRepository userTeamRepository;

    /**
     * Executes the updating process for a specific Team.
     *
     * @param userTeam The {@link UserTeam} entity containing the updated information.
     * @param teamId The unique identifier ({@link UUID}) of the userteam being targeted.
     * @return The updated and persisted {@link UserTeam} entity.
     * @throws UserTeamException if the userteam is not found in the repository.
     * @throws IllegalArgumentException if the ID within the userteam object does not match the path ID.
     */
    public UserTeam execute(UserTeam userTeam, UUID teamId) {
        validateAndRetrieveTeam(userTeam, teamId);
        userTeamRepository.save(userTeam);
        logger.info("[UPDATE] Updated Player '{}'", userTeam.getId());
        return userTeam;
    }

    /**
     * Validates that both the ID from the request body and the ID from the
     * path exist in the database and match each other.
     *
     * @param userTeam The userteam object from the request.
     * @param id The ID provided in the application context/path.
     * @throws UserTeamException if either ID does not correspond to an existing userteam.
     * @throws IllegalArgumentException if there is a mismatch between the IDs.
     */
    private void validateAndRetrieveTeam(UserTeam userTeam, UUID id) {
        UserTeam bodyUserTeam = userTeamRepository.findById(userTeam.getId()).orElseThrow(
                () -> new UserTeamException(ErrorEnum.TEAM_NOT_FOUND, userTeam.getId().toString())
        );

        UserTeam idUserTeam = userTeamRepository.findById(id).orElseThrow(
                () -> new UserTeamException(ErrorEnum.TEAM_NOT_FOUND, id.toString())
        );

        if (!bodyUserTeam.getId().toString().equals(idUserTeam.getId().toString())) {
            throw new IllegalArgumentException("Team id " + bodyUserTeam.getId() + " does not match " + idUserTeam.getId());
        }
    }
}
