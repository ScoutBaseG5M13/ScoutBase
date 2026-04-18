package es.dimecresalessis.scoutbase.application.team;

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

    public boolean execute(UUID id) {
        teamRepository.findById(id).orElseThrow();
        teamRepository.deleteById(id);
        logger.info("[DELETE] Deleted Team with id '{}'", id);
        return true;
    }
}
