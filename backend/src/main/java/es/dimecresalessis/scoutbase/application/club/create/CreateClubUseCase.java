package es.dimecresalessis.scoutbase.application.club.create;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.club.exception.ClubException;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.userclub.exception.UserClubException;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

/**
 * Use case for creating {@link Club}.
 */
@Service
@AllArgsConstructor
public class CreateClubUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateClubUseCase.class);
    private final ClubRepository clubRepository;
    private final UserClubRepository userClubRepository;

    /**
     * Executes the operation to save a new {@link Club} entity in the DB.
     *
     * @param club The {@link Club} object containing the club's details.
     * @return The created {@link Club} object after being persisted.
     * @throws ClubException If the provided club object is null.
     */
    public Club execute(Club club, UUID userClubId) throws ClubException {
        if (club == null) {
            throw new ClubException(ErrorEnum.CLUB_IS_NULL);
        }
        if (club.getId() == null) {
            throw new ClubException(ErrorEnum.CLUB_ID_IS_NULL);
        }
        if (clubRepository.findById(club.getId()).isPresent()) {
            throw new ClubException(ErrorEnum.CLUB_ALREADY_EXISTS, club.getId().toString());
        }
        Optional<UserClub> userClub = userClubRepository.findUserClubById(userClubId);
        if (userClub.isEmpty()) {
            throw new UserClubException(ErrorEnum.CLUB_NOT_FOUND, userClubId.toString());
        }

        clubRepository.save(club);
        logger.info("[CREATE] Created Club with id '{}'", club.getId());

        if (userClub.get().getManagedClubs() == null) {
            userClub.get().setManagedClubs(new ArrayList<>());
        }
        userClub.get().getManagedClubs().add(club.getId());
        userClubRepository.save(userClub.get());
        logger.info("[UPDATE] Updated UserClub '{}' and added the Club {}", userClubId, club.getId());

        return club;
    }
}
