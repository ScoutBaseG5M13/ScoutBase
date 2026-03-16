package es.dimecresalessis.scoutbase.application.player;

import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DeletePlayerUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeletePlayerUseCase.class);
    private final PlayerRepository playerRepository;

    public boolean execute(UUID id) {
        playerRepository.findById(id).orElseThrow();
        playerRepository.deleteById(id);
        logger.info("[DELETE] Deleted Player with id '{}'", id);
        return true;
    }
}
