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
     * @param clubId The ID of the userclub to be updated.
     * @return The updated {@link UserClub} object after being persisted.
     */
    public UserClub execute(UserClub userClub, UUID clubId) {
        validateAndRetrieveClub(userClub, clubId);
        UserClub matchedUserClub = matchWithSavedInfo(userClub, clubId);
        userClubRepository.save(matchedUserClub);
        logger.info("[UPDATE] Updated Club '{}'", matchedUserClub.getId());
        return matchedUserClub;
    }

    private void validateAndRetrieveClub(UserClub userClub, UUID id) {
        if (!userClub.getId().equals(id)) {
            throw new IllegalArgumentException("Club id '" + userClub.getId() + "' does not match the path variable '" + id + "'");
        }

        UserClub bodyUserClub = userClubRepository.findUserClubById(userClub.getId()).orElseThrow(
                () -> new UserClubException(ErrorEnum.USER_CLUB_NOT_FOUND, userClub.getId().toString())
        );

        UserClub idUserClub = userClubRepository.findUserClubById(id).orElseThrow(
                () -> new UserClubException(ErrorEnum.USER_CLUB_NOT_FOUND, id.toString())
        );


            for (UUID user : userClub.getAdminUserIds()) {
                try {
                    findUserByIdUseCase.execute(user);
                } catch (NoSuchElementException e) {
                    throw new UserClubException(ErrorEnum.USER_NOT_FOUND, user.toString());
                }
            }


        if (!bodyUserClub.getId().toString().equals(idUserClub.getId().toString())) {
            throw new IllegalArgumentException("Body user club id '" + bodyUserClub.getId() + "' does not match '" + idUserClub.getId() + "'");
        }
    }

    private UserClub matchWithSavedInfo(UserClub userClub, UUID clubId) {
        UserClub newUserClub = userClub;
        UserClub savedUserClub = userClubRepository.findById(clubId).orElse(null);
        if (savedUserClub == null) {
            return newUserClub;
        }
        newUserClub.matchWithObject(savedUserClub);
        return newUserClub;
    }
}
