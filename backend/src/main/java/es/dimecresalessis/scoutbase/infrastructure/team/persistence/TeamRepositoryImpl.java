package es.dimecresalessis.scoutbase.infrastructure.team.persistence;

import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import es.dimecresalessis.scoutbase.infrastructure.team.persistence.mapper.TeamEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepository {

    private final JpaTeamRepository jpaTeamRepository;
    private final TeamEntityMapper mapper;

    @Override
    public List<Team> findAll() {
        return jpaTeamRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Team> findById(UUID id) {
        return jpaTeamRepository.findById(id).map(mapper::toDomain);
    }

    public List<Team> findAllByUserId(UUID userId) {
        List<TeamEntity> teamEntities = jpaTeamRepository.findAllByUserId(userId);
        return teamEntities.stream()
                .map(mapper::toDomain)
                .toList();
    }

    public Optional<Team> findByPlayerId(UUID userId) {
        return jpaTeamRepository.findByPlayerId(userId)
                .map(mapper::toDomain);
    }

    @Override
    public Team save(Team team) {
        TeamEntity teamEntity = jpaTeamRepository.findById(team.getId())
                .orElseGet(TeamEntity::new);
        mapper.updateEntityFromDomain(team, teamEntity);
        jpaTeamRepository.saveAndFlush(teamEntity);
        return team;
    }

    @Override
    public void deleteById(UUID id) {
        jpaTeamRepository.deleteById(id);
    }
}
