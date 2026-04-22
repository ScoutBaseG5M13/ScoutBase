package es.dimecresalessis.scoutbase.application.user.find;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Use case for finding all {@link User} in the system. For testing.
 */
@Service
@AllArgsConstructor
public class FindAllUsersUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindAllUsersUseCase.class);
    private final UserRepository userRepository;

    public List<User> execute() {
        List<User> users = userRepository.findAll();
        logger.info("[FIND] Found {} users", users.size());
        return users;
    }
}
