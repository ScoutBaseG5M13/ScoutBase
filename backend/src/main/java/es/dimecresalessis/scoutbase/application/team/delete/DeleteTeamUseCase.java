package es.dimecresalessis.scoutbase.application.team.delete;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DeleteTeamUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeleteTeamUseCase.class);
    private final TeamRepository teamRepository;
    private final ClubRepository clubRepository;

    public boolean execute(UUID id) {
        teamRepository.findById(id).orElseThrow();
        teamRepository.deleteById(id);
        logger.info("[DELETE] Deleted Team '{}'", id);

        Club club = clubRepository.findClubByTeam(id).orElseThrow();
        club.getTeams().remove(id);
        clubRepository.save(club);
        logger.info("[DELETE] Removed Team '{}' from Club {}", id, club.getId());

        return true;
    }
}
