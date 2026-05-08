package es.dimecresalessis.scoutbase.application.club.delete;

import es.dimecresalessis.scoutbase.application.userteam.delete.RemoveClubUseCase;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Use case for deleting a {@link Club}.
 */
@Service
@AllArgsConstructor
public class DeleteClubUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeleteClubUseCase.class);
    private final ClubRepository clubRepository;
    private final UserClubRepository userClubRepository;
    private final RemoveClubUseCase removeClubUseCase;
    private final TeamRepository teamRepository;

    /**
     * Executes the operation to delete a {@link Club} from the repository.
     *
     * @param id The ID of the club to be deleted.
     * @return {@code true} if the club was successfully deleted, {@code false} otherwise.
     */
    public boolean execute(UUID id) {
        Club club = clubRepository.findById(id).orElseThrow();

        if (club.getTeams() != null) {
            for (UUID teamId : club.getTeams()) {
                teamRepository.deleteById(teamId);
                logger.info("[DELETE] Deleted Team with id '{}'", id);
            }
        }

        clubRepository.deleteById(id);
        logger.info("[DELETE] Deleted Club with id '{}'", id);

        Optional<UserClub> userClub = userClubRepository.findUserClubById(club.getId());
        if (userClub.isPresent()) {
            removeClubUseCase.execute(userClub.get().getId(), club.getId());
        }

        return true;
    }
}