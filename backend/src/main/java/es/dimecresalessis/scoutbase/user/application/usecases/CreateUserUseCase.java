package es.dimecresalessis.scoutbase.user.application.usecases;

import es.dimecresalessis.scoutbase.shared.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.user.domain.exception.UserException;
import es.dimecresalessis.scoutbase.user.domain.model.User;
import es.dimecresalessis.scoutbase.user.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateUserUseCase {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CreateUserUseCase.class);
    private final UserRepository userRepository;

    public User execute(User user) {
        if (user == null) {
            throw new UserException(ErrorEnum.USER_IS_NULL);
        }
        userRepository.save(user);
        logger.info("[CREATE] Created User with id '{}'", user.getId());
        return user;
    }
}
