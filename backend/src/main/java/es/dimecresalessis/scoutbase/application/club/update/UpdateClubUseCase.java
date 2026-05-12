package es.dimecresalessis.scoutbase.application.club.update;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.club.exception.ClubException;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for updating a {@link Club}.
 */
@Service
@AllArgsConstructor
public class UpdateClubUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UpdateClubUseCase.class);
    private final ClubRepository clubRepository;

    /**
     * Updates the details of a {@link Club} identified by their unique ID.
     *
     * @param club The updated {@link Club} object with the new details.
     * @param clubId The ID of the club to be updated.
     * @return The updated {@link Club} object after being persisted.
     */
    public Club execute(Club club, UUID clubId) {
        validateAndRetrieveClub(club, clubId);
        Club matchedClub = matchWithSavedInfo(club, clubId);
        clubRepository.save(matchedClub);
        logger.info("[UPDATE] Updated Club '{}'", matchedClub.getId());
        return matchedClub;
    }

    private void validateAndRetrieveClub(Club club, UUID id) {
        if (!club.getId().equals(id)) {
            throw new IllegalArgumentException("Club id '" + club.getId() + "' does not match the path variable '" + id + "'");
        }

        Club bodyClub = clubRepository.findById(club.getId()).orElseThrow(
                () -> new ClubException(ErrorEnum.CLUB_NOT_FOUND, club.getId().toString())
        );

        Club idClub = clubRepository.findById(id).orElseThrow(
                () -> new ClubException(ErrorEnum.CLUB_NOT_FOUND, id.toString())
        );

        if (!bodyClub.getId().toString().equals(idClub.getId().toString())) {
            throw new IllegalArgumentException("Body club id '" + bodyClub.getId() + "' does not match '" + idClub.getId() + "'");
        }
    }

    private Club matchWithSavedInfo(Club club, UUID clubId) {
        Club newClub = club;
        Club savedClub = clubRepository.findById(clubId).orElse(null);
        if (savedClub == null) {
            return newClub;
        }
        newClub.matchWithObject(savedClub);
        return newClub;
    }
}
