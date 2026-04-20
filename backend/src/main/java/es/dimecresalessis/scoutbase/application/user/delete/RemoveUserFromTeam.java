package es.dimecresalessis.scoutbase.application.user.delete;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.team.exception.TeamException;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RemoveUserFromTeam {

    private static final Logger logger = LoggerFactory.getLogger(RemoveUserFromTeam.class);
    private final TeamRepository teamRepository;

    public boolean execute(UUID userId, UUID teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(ErrorEnum.TEAM_NOT_FOUND, teamId.toString()));
        if (team.getTrainer().equals(userId)) {
            team.setTrainer(null);
        }
        if (team.getSecondTrainer().equals(userId)) {
            team.setSecondTrainer(null);
        }
        if (team.getPlayers().contains(userId)) {
            team.getPlayers().remove(userId);
        }
        teamRepository.save(team);
        logger.info("[UPDATE] Removed user '{}' from team '{}'", userId, teamId);
        return true;
    }
}
