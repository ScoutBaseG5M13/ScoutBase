package es.dimecresalessis.scoutbase.application.userteam.delete;

import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Use case for deleting a {@link UserTeam}.
 */
@Service
@AllArgsConstructor
public class DeleteUserTeamUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeleteUserTeamUseCase.class);
    private final UserTeamRepository userTeamRepository;
    private final UserClubRepository userClubRepository;

    /**
     * Executes the deletion of a Team and updates the Club to maintain integrity.
     *
     * @param id The unique identifier ({@link UUID}) of the Team to be deleted.
     * @return {@code true} if the deletion and Club update were successful.
     * @throws NoSuchElementException if the userteam or its associated userclub cannot be found.
     */
    public boolean execute(UUID id) {
        UserTeam userTeam = userTeamRepository.findById(id).orElseThrow();
        userTeamRepository.deleteById(id);
        logger.info("[DELETE] Deleted Team '{}'", id);

        userClubRepository.findUserClubByTeam(id).ifPresent(club -> {
            club.getUserTeams().remove(id);
            userClubRepository.save(club);
            logger.info("[DELETE] Removed Team '{}' from Club '{}'", id, club.getName());
        });

        return true;
    }
}
