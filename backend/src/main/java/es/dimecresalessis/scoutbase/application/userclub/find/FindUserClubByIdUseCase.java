package es.dimecresalessis.scoutbase.application.userclub.find;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for finding a {@link UserClub} by their unique {@link UUID}.
 */
@Service
@AllArgsConstructor
public class FindUserClubByIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindUserClubByIdUseCase.class);
    private final UserClubRepository userClubRepository;

    /**
     * Retrieves a userclub by their unique ID.
     *
     * @param id The ID of the userclub.
     * @return The {@link UserClub} entity corresponding to the provided ID.
     */
    public UserClub execute(UUID id)  {
        UserClub userClub = userClubRepository.findUserClubById(id).orElseThrow();
        logger.info("[FIND] Found Club with id '{}'", id);
        return userClub;
    }
}