package es.dimecresalessis.scoutbase.application.userclub.delete;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for deleting a {@link UserClub}.
 */
@Service
@AllArgsConstructor
public class DeleteUserClubUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeleteUserClubUseCase.class);
    private final UserClubRepository userClubRepository;

    /**
     * Executes the operation to delete a {@link UserClub} from the repository.
     *
     * @param id The ID of the userclub to be deleted.
     * @return {@code true} if the userclub was successfully deleted, {@code false} otherwise.
     */
    public boolean execute(UUID id) {
        userClubRepository.findUserClubById(id).orElseThrow();
        userClubRepository.deleteById(id);
        logger.info("[DELETE] Deleted Club with id '{}'", id);
        return true;
    }
}