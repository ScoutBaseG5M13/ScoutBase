package es.dimecresalessis.scoutbase.infrastructure.club.persistence;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import es.dimecresalessis.scoutbase.infrastructure.club.persistence.mapper.ClubEntityMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Infrastructure implementation of the {@link ClubRepository} interface.
 */
@Repository
@RequiredArgsConstructor
public class ClubRepositoryImpl implements ClubRepository {

    /**
     * The Spring Data JPA repository providing low-level database access.
     */
    private final JpaClubRepository jpaClubRepository;

    private final ClubEntityMapper clubMapper;
    private final TeamRepository teamRepository;

    @Override
    public List<Club> findAll() {
        return jpaClubRepository.findAll()
                .stream()
                .map(clubMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Club> findById(UUID id) {
        return jpaClubRepository.findById(id).map(clubMapper::toDomain);
    }

    @Override
    public Optional<Club> findClubByPlayerId(UUID playerId) {
        Optional<Team> team = teamRepository.findById(playerId);
        if (team.isPresent()) {
            ClubEntity clubEntity = jpaClubRepository.findAll().stream()
                    .filter(club -> club.getTeams().contains(team.get().getId()))
                    .findFirst()
                    .orElse(null);
            if (clubEntity != null) {
                return Optional.of(clubMapper.toDomain(clubEntity));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Club> findClubByTeam(UUID teamId) {
        List<ClubEntity> clubs = jpaClubRepository.findAll();
        for (ClubEntity club : clubs) {
            if (club.getTeams().contains(teamId)) {
                return Optional.of(clubMapper.toDomain(club));
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Club save(Club club) {
        ClubEntity clubEntity = jpaClubRepository.findById(club.getId())
                .orElseGet(ClubEntity::new);
        clubMapper.updateEntityFromDomain(club, clubEntity);
        jpaClubRepository.saveAndFlush(clubEntity);
        return club;
    }

    @Override
    public void deleteById(UUID id) {
        jpaClubRepository.deleteById(id);
    }
}
