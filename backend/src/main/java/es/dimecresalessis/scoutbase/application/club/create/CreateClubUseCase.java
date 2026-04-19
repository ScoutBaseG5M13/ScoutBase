package es.dimecresalessis.scoutbase.application.club.create;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.club.exception.ClubException;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Use case for creating {@link Club} entities in the system.
 */
@Service
@AllArgsConstructor
public class CreateClubUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateClubUseCase.class);
    private final ClubRepository clubRepository;

    /**
     * Executes the operation to save a new {@link Club} entity in the DB.
     *
     * @param club The {@link Club} object containing the club's details.
     * @return The created {@link Club} object after being persisted.
     * @throws ClubException If the provided club object is null.
     */
    public Club execute(Club club) throws ClubException {
        if (club == null) {
            throw new ClubException(ErrorEnum.CLUB_IS_NULL);
        }
        if (club.getId() == null) {
            throw new ClubException(ErrorEnum.CLUB_ID_IS_NULL);
        }
        if (clubRepository.findById(club.getId()).isPresent()) {
            throw new ClubException(ErrorEnum.CLUB_ALREADY_EXISTS, club.getId().toString());
        }
        clubRepository.save(club);
        logger.info("[CREATE] Created Club with id '{}'", club.getId());
        return club;
    }
}
