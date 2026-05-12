package es.dimecresalessis.scoutbase.application.stat.service;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.player.exception.PlayerException;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.model.StatEnum;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.PlayerAverageStatScoreDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculateAverageScoreTest {

    @Mock
    private StatRepository statRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private CalculateAverageScore calculateAverageScore;

    private UUID playerId;
    private Player player;

    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
        player = Player.builder()
                .id(playerId)
                .name("John")
                .surname("Doe")
                .position(PositionEnum.DEFENSA_CENTRAL)
                .build();
    }

    @Test
    void execute_ShouldCalculateAveragesCorrectly() {
        Stat offensiveStat = Stat.builder().code("DR1").value(4).playerId(playerId).build();
        Stat defensiveStat = Stat.builder().code("MAR").value(2).playerId(playerId).build();
        Stat mentalStat = Stat.builder().code("TDE").value(5).playerId(playerId).build();
        Stat physicalStat = Stat.builder().code("FUE").value(3).playerId(playerId).build();

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(statRepository.findAllByPlayerId(playerId)).thenReturn(List.of(offensiveStat, defensiveStat, mentalStat, physicalStat));

        PlayerAverageStatScoreDTO result = calculateAverageScore.execute(playerId);

        assertEquals(4.0f, result.getOffensiveAverageScore());
        assertEquals(2.0f, result.getDefensiveAverageScore());
        assertEquals(5.0f, result.getMentalAverageScore());
        assertEquals(3.0f, result.getPhysicalAverageScore());
        assertEquals(3.5f, result.getGlobalAverageScore());
        verify(playerRepository).findById(playerId);
        verify(statRepository).findAllByPlayerId(playerId);
    }

    @Test
    void execute_ShouldReturnZeroAverages_WhenNoStatsFound() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(statRepository.findAllByPlayerId(playerId)).thenReturn(List.of());

        PlayerAverageStatScoreDTO result = calculateAverageScore.execute(playerId);

        assertEquals(0.0f, result.getGlobalAverageScore());
        assertEquals(0.0f, result.getOffensiveAverageScore());
        assertEquals(0.0f, result.getDefensiveAverageScore());
        verify(statRepository).findAllByPlayerId(playerId);
    }

    @Test
    void execute_ShouldThrowPlayerException_WhenPlayerNotFound() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        PlayerException exception = assertThrows(PlayerException.class, () ->
                calculateAverageScore.execute(playerId)
        );

        assertEquals(ErrorEnum.PLAYER_NOT_FOUND, exception.getErrorEnum());
        verifyNoInteractions(statRepository);
    }

    @Test
    void execute_ShouldHandleMultipleStatsOfSameType() {
        Stat off1 = Stat.builder().code(StatEnum.CSL.statCode).value(5).playerId(playerId).build();
        Stat off2 = Stat.builder().code(StatEnum.TAP.statCode).value(3).playerId(playerId).build();

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(statRepository.findAllByPlayerId(playerId)).thenReturn(List.of(off1, off2));

        PlayerAverageStatScoreDTO result = calculateAverageScore.execute(playerId);

        assertEquals(4.0f, result.getOffensiveAverageScore());
        assertEquals(2.0f, result.getGlobalAverageScore());
    }
}