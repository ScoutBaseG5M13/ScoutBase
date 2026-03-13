package es.dimecresalessis.scoutbase.player.application.usecases;

import es.dimecresalessis.scoutbase.player.domain.model.Player;
import es.dimecresalessis.scoutbase.player.domain.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class FindPlayerByIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindPlayerByIdUseCase.class);
    private final PlayerRepository playerRepository;

    public Player execute(UUID id)  {
        Player player = playerRepository.findById(id).orElseThrow();
        logger.info("[FIND] Found Player with id '{}'", id);
        return player;
    }
}