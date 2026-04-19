package es.dimecresalessis.scoutbase.application.club.find;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Use case for retrieving all {@link Club} entities from the system.
 */
@Service
@AllArgsConstructor
public class FindAllClubsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindAllClubsUseCase.class);
    private final ClubRepository clubRepository;

    /**
     * Executes the operation for getting all clubs from the repository.
     *
     * @return A list of all {@link Club} entities.
     */
    public List<Club> execute() {
        List<Club> clubs = clubRepository.findAll();
        logger.info("[FIND] Found {} clubs", clubs.size());
        return clubs;
    }
}
