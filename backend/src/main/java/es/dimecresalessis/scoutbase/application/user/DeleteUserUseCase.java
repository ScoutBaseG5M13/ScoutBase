package es.dimecresalessis.scoutbase.application.user;

import es.dimecresalessis.scoutbase.application.player.DeletePlayerUseCase;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import es.dimecresalessis.scoutbase.domain.user.model.User;

import java.util.UUID;

/**
 * Use case for deleting a {@link User} entity from the system.
 */
@Service
@AllArgsConstructor
public class DeleteUserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeletePlayerUseCase.class);
    private final UserRepository userRepository;

    /**
     * Executes the operation to delete a {@link User} from the repository.
     *
     * @param id The ID of the user to be deleted.
     * @return {@code true} if the user was successfully deleted, {@code false} otherwise.
     */
    public boolean execute(UUID id) {
        userRepository.findById(id).orElseThrow();
        userRepository.deleteById(id);
        logger.info("[DELETE] Deleted User with id '{}'", id);
        return true;
    }
}
