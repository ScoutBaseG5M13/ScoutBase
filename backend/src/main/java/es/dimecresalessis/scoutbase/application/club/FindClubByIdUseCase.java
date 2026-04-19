package es.dimecresalessis.scoutbase.application.club;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for finding a {@link Club} entity by their unique ID.
 */
@Service
@AllArgsConstructor
public class FindClubByIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindClubByIdUseCase.class);
    private final ClubRepository clubRepository;

    /**
     * Retrieves a club by their unique ID.
     *
     * @param id The ID of the club.
     * @return The {@link Club} entity corresponding to the provided ID.
     */
    public Club execute(UUID id)  {
        Club club = clubRepository.findById(id).orElseThrow();
        logger.info("[FIND] Found Club with id '{}'", id);
        return club;
    }
}