package es.dimecresalessis.scoutbase.application.userclub.update;

import es.dimecresalessis.scoutbase.application.user.find.FindUserByIdUseCase;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.userclub.exception.UserClubException;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Use case for updating a {@link UserClub}.
 */
@Service
@AllArgsConstructor
public class UpdateUserClubUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UpdateUserClubUseCase.class);
    private final UserClubRepository userClubRepository;
    private final FindUserByIdUseCase findUserByIdUseCase;

    /**
     * Updates the details of a {@link UserClub} identified by their unique ID.
     *
     * @param userClub The updated {@link UserClub} object with the new details.
     * @param id The ID of the userclub to be updated.
     * @return The updated {@link UserClub} object after being persisted.
     */
    public UserClub execute(UserClub userClub, UUID id) {
        validateAndRetrieveClub(userClub, id);
        userClubRepository.save(userClub);
        logger.info("[UPDATE] Updated Club '{}'", userClub.getId());
        return userClub;
    }

    private void validateAndRetrieveClub(UserClub userClub, UUID id) {
        UserClub bodyUserClub = userClubRepository.findUserClubById(userClub.getId()).orElseThrow(
                () -> new UserClubException(ErrorEnum.CLUB_NOT_FOUND, userClub.getId().toString())
        );

        UserClub idUserClub = userClubRepository.findUserClubById(id).orElseThrow(
                () -> new UserClubException(ErrorEnum.CLUB_NOT_FOUND, id.toString())
        );


            for (UUID user : userClub.getAdminUserIds()) {
                try {
                    findUserByIdUseCase.execute(user);
                } catch (NoSuchElementException e) {
                    throw new UserClubException(ErrorEnum.USER_NOT_FOUND, user.toString());
                }
            }


        if (!bodyUserClub.getId().toString().equals(idUserClub.getId().toString())) {
            throw new IllegalArgumentException("Club id " + bodyUserClub.getId() + " does not match " + idUserClub.getId());
        }
    }
}
