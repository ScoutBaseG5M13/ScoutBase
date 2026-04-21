package es.dimecresalessis.scoutbase.application.club.delete;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for deleting a {@link Club} entity from the system.
 */
@Service
@AllArgsConstructor
public class DeleteClubUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeleteClubUseCase.class);
    private final ClubRepository clubRepository;

    /**
     * Executes the operation to delete a {@link Club} from the repository.
     *
     * @param id The ID of the club to be deleted.
     * @return {@code true} if the club was successfully deleted, {@code false} otherwise.
     */
    public boolean execute(UUID id) {
        clubRepository.findById(id).orElseThrow();
        clubRepository.deleteById(id);
        logger.info("[DELETE] Deleted Club with id '{}'", id);
        return true;
    }
}