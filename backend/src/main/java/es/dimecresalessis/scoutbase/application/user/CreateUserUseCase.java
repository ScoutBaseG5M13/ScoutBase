package es.dimecresalessis.scoutbase.application.user;

import es.dimecresalessis.scoutbase.application.service.RegistrationService;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateUserUseCase {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CreateUserUseCase.class);
    private final RegistrationService registrationService;

    public User execute(User user) {
        if (user == null) {
            throw new UserException(ErrorEnum.USER_IS_NULL);
        }
        User savedUser = registrationService.createUser(user);
        logger.info("[CREATE] Created User with id '{}'", savedUser.getId());
        return savedUser;
    }
}
