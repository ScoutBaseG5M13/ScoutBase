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

    @Override
    public List<Team> findAllByPlayerId(UUID userId) {
        return jpaTeamRepository.findAll()
                .stream()
                .filter(s -> s.getTrainers().contains(userId) || s.getScouters().contains(userId))
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Team save(Team team) {
        TeamEntity teamEntity = jpaTeamRepository.findById(team.getId())
                .orElseGet(TeamEntity::new);
        mapper.updateEntityFromDomain(team, teamEntity);
        jpaTeamRepository.save(teamEntity);
        return team;
    }

    @Override
    public void deleteById(UUID id) {
        jpaTeamRepository.deleteById(id);
    }
}
