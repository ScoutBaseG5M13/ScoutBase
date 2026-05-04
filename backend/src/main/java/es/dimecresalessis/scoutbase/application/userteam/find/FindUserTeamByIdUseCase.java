package es.dimecresalessis.scoutbase.application.userteam.find;

import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for finding a {@link UserTeam} by {@link UUID}.
 */
@Service
@AllArgsConstructor
public class FindUserTeamByIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindUserTeamByIdUseCase.class);
    private final UserTeamRepository userTeamRepository;

    /**
     * Retrieves a userteam by their unique ID.
     *
     * @param id The ID of the userteam.
     * @return The {@link UserTeam} entity corresponding to the provided ID.
     */
    public UserTeam execute(UUID id)  {
        UserTeam userTeam = userTeamRepository.findById(id).orElse(null);
        logger.info("[FIND] Found Team with id '{}'", id);
        return userTeam;
    }
}