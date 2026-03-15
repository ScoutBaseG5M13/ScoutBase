package es.dimecresalessis.scoutbase.user.application.usecases;

import es.dimecresalessis.scoutbase.user.domain.model.User;
import es.dimecresalessis.scoutbase.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class FindUserByIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindUserByIdUseCase.class);
    private final UserRepository userRepository;

    public User execute(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        logger.info("[FIND] Found User with id '{}'", id);
        return user;
    }
}
