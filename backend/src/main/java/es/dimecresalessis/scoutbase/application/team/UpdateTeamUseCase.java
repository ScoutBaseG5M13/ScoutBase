package es.dimecresalessis.scoutbase.application.team;

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
public class UpdateTeamUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UpdateTeamUseCase.class);
    private final TeamRepository teamRepository;

    public Team execute(Team team, UUID id) {
        validateAndRetrieveTeam(team, id);
        teamRepository.save(team);
        logger.info("[UPDATE] Updated Player with id '{}'", team.getId());
        return team;
    }

    private void validateAndRetrieveTeam(Team team, UUID id) {
        Team bodyTeam = teamRepository.findById(team.getId()).orElseThrow(
                () -> new TeamException(ErrorEnum.TEAM_NOT_FOUND, team.getId().toString())
        );

        Team idTeam = teamRepository.findById(id).orElseThrow(
                () -> new TeamException(ErrorEnum.TEAM_NOT_FOUND, id.toString())
        );

        if (!bodyTeam.getId().toString().equals(idTeam.getId().toString())) {
            throw new IllegalArgumentException("Team id " + bodyTeam.getId() + " does not match " + idTeam.getId());
        }
    }
}
