package es.dimecresalessis.scoutbase.application.team;

import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for finding a {@link Team} entity by their unique ID.
 */
@Service
@AllArgsConstructor
public class FindTeamByIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindTeamByIdUseCase.class);
    private final TeamRepository teamRepository;

    /**
     * Retrieves a team by their unique ID.
     *
     * @param id The ID of the team.
     * @return The {@link Team} entity corresponding to the provided ID.
     */
    public Team execute(UUID id)  {
        Team team = teamRepository.findById(id).orElseThrow();
        logger.info("[FIND] Found Team with id '{}'", id);
        return team;
    }
}