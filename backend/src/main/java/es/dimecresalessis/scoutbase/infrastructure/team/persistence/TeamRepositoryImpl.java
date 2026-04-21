package es.dimecresalessis.scoutbase.infrastructure.team.persistence;

import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import es.dimecresalessis.scoutbase.infrastructure.team.persistence.mapper.TeamEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Infrastructure implementation of the {@link TeamRepository} interface.
 */
@Repository
@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepository {

    private final JpaTeamRepository jpaTeamRepository;
    private final TeamEntityMapper mapper;

    /**
     * Finds all Teams present in the system.
     *
     * @return A {@link List} of all {@link Team} domain entities.
     */
    @Override
    public List<Team> findAll() {
        return jpaTeamRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    /**
     * Finds a Team by its unique identifier.
     *
     * @param id The {@link UUID} of the Team.
     * @return An {@link Optional} containing the {@link Team} if found, or empty otherwise.
     */
    @Override
    public Optional<Team> findById(UUID id) {
        return jpaTeamRepository.findById(id).map(mapper::toDomain);
    }

    /**
     * Finds all Teams associated with a specific User.
     *
     * @param userId The {@link UUID} of the User.
     * @return A {@link List} of {@link Team} entities linked to the User.
     */
    @Override
    public List<Team> findAllByUserId(UUID userId) {
        List<TeamEntity> teamEntities = jpaTeamRepository.findAll();

        return teamEntities.stream()
                .filter(t -> t.getTrainer().equals(userId))
                .filter(t -> t.getSecondTrainer().equals(userId))
                .filter(t -> t.getScouters().contains(userId))
                .map(mapper::toDomain)
                .toList();
    }

    /**
     * Finds a Team that contains a specific Player in its roster.
     *
     * @param playerId The {@link UUID} of the Player.
     * @return An {@link Optional} containing the {@link Team} the Player belongs to.
     */
    public Optional<Team> findByPlayerId(UUID playerId) {
        List<TeamEntity> teamEntities = jpaTeamRepository.findAll();

        return teamEntities.stream()
                .filter(t -> t.getPlayers().contains(playerId))
                .map(mapper::toDomain)
                .findFirst();
    }

    /**
     * Persists or updates a Team.
     *
     * @param team The {@link Team} domain object to be saved.
     * @return The saved {@link Team} entity.
     */
    @Override
    public Team save(Team team) {
        TeamEntity teamEntity = jpaTeamRepository.findById(team.getId())
                .orElseGet(TeamEntity::new);
        mapper.updateEntityFromDomain(team, teamEntity);
        jpaTeamRepository.saveAndFlush(teamEntity);
        return team;
    }

    /**
     * Deletes a Team.
     *
     * @param id The {@link UUID} of the team to delete.
     */
    @Override
    public void deleteById(UUID id) {
        jpaTeamRepository.deleteById(id);
    }
}
