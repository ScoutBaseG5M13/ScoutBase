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

    public boolean execute(UUID userClubId, UUID clubId) {
        UserClub userClub = userClubRepository.findUserClubById(userClubId).orElseThrow();
        userClub.getManagedClubs().remove(clubId);
        userClubRepository.save(userClub);
        logger.info("[DELETE] Removed Club '{}' from UserClub '{}'", clubId, userClubId);
        return true;
    }
}
