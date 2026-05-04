package es.dimecresalessis.scoutbase.application.userclub.create;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.userclub.exception.UserClubException;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Use case for creating {@link UserClub}.
 */
@Service
@AllArgsConstructor
public class CreateUserClubUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateUserClubUseCase.class);
    private final UserClubRepository userClubRepository;

    /**
     * Executes the operation to save a new {@link UserClub} entity in the DB.
     *
     * @param userClub The {@link UserClub} object containing the userclub's details.
     * @return The created {@link UserClub} object after being persisted.
     * @throws UserClubException If the provided userclub object is null.
     */
    public UserClub execute(UserClub userClub) throws UserClubException {
        if (userClub == null) {
            throw new UserClubException(ErrorEnum.CLUB_IS_NULL);
        }
        if (userClub.getId() == null) {
            throw new UserClubException(ErrorEnum.CLUB_ID_IS_NULL);
        }
        if (userClubRepository.findUserClubById(userClub.getId()).isPresent()) {
            throw new UserClubException(ErrorEnum.CLUB_ALREADY_EXISTS, userClub.getId().toString());
        }
        userClubRepository.save(userClub);
        logger.info("[CREATE] Created Club with id '{}'", userClub.getId());
        return userClub;
    }
}
