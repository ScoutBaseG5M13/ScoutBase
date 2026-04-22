package es.dimecresalessis.scoutbase.application.team.delete;

import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Use case for deleting a {@link Team}.
 */
@Service
@AllArgsConstructor
public class DeleteTeamUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeleteTeamUseCase.class);
    private final TeamRepository teamRepository;
    private final ClubRepository clubRepository;

    /**
     * Executes the deletion of a Team and updates the Club to maintain integrity.
     *
     * @param id The unique identifier ({@link UUID}) of the Team to be deleted.
     * @return {@code true} if the deletion and Club update were successful.
     * @throws NoSuchElementException if the team or its associated club cannot be found.
     */
    public boolean execute(UUID id) {
        Team team = teamRepository.findById(id).orElseThrow();
        teamRepository.deleteById(id);
        logger.info("[DELETE] Deleted Team '{}'", id);

        clubRepository.findClubByTeam(id).ifPresent(club -> {
            club.getTeams().remove(id);
            clubRepository.save(club);
            logger.info("[DELETE] Removed Team '{}' from Club '{}'", id, club.getName());
        });

        return true;
    }
}
