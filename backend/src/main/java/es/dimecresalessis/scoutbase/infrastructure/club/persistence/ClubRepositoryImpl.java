package es.dimecresalessis.scoutbase.infrastructure.club.persistence;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.infrastructure.club.persistence.mapper.ClubEntityMapper;
import es.dimecresalessis.scoutbase.infrastructure.team.persistence.JpaTeamRepository;
import es.dimecresalessis.scoutbase.infrastructure.team.persistence.mapper.TeamEntityMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

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
    private final TeamEntityMapper teamMapper;
    private final JpaTeamRepository jpaTeamRepository;

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
    public List<Club> findAllByUserId(UUID userId) {
        return jpaClubRepository.findAll()
                .stream()
                .filter(s -> s.getAdminUserIds().contains(userId))
                .map(clubMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Club> findClubByTeam(UUID teamId) {
        return jpaClubRepository.findAllByTeam(teamId)
                .stream()
                .map(clubMapper::toDomain)
                .findFirst();
    }

    @Override
    public List<Club> findAllClubsByUserId(UUID userId) {
        //ADMIN
        ArrayList<Club> clubs = new ArrayList<>(jpaClubRepository.findAllByUserId(userId)
                .stream()
                .map(clubMapper::toDomain)
                .toList());

        //TRAINER, SUBTRAINER...
        List<Team> teams = jpaTeamRepository.findAllByUserId(userId)
                .stream()
                .map(teamMapper::toDomain)
                .toList();
        List<Club> teamClubs = teams.stream()
                .filter(t -> findClubByTeam(t.getId()).isPresent())
                .map(t -> findClubByTeam(t.getId()).get())
                .toList();

        return Stream.concat(clubs.stream(), teamClubs.stream())
                .distinct()
                .toList();
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
