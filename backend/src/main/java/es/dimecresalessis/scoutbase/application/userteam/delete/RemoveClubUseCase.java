package es.dimecresalessis.scoutbase.application.userteam.delete;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for removing a {@link Club} from the {@link UserClub}.
 */
@Service
@AllArgsConstructor
public class RemoveClubUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RemoveClubUseCase.class);
    private final UserClubRepository userClubRepository;

    /**
     * Executes the removal of a specific club association from a {@link UserClub} record.
     *
     * @param userClubId The {@link UUID} of the user-club aggregate to be modified.
     * @param clubId The {@link UUID} of the club to be removed from the managed list.
     * @return {@code true} if the operation completes successfully.
     * @throws java.util.NoSuchElementException If the {@link UserClub} with the given ID does not exist.
     */
    public boolean execute(UUID userClubId, UUID clubId) {
        UserClub userClub = userClubRepository.findUserClubById(userClubId).orElseThrow();
        userClub.getManagedClubs().remove(clubId);
        userClubRepository.save(userClub);
        logger.info("[DELETE] Removed Club '{}' from UserClub '{}'", clubId, userClubId);
        return true;
    }
}
