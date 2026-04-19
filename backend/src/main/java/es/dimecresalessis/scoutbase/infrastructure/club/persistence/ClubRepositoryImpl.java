package es.dimecresalessis.scoutbase.infrastructure.club.persistence;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
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

    private final ClubEntityMapper mapper;

    @Override
    public List<Club> findAll() {
        return jpaClubRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Club> findById(UUID id) {
        return jpaClubRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Club> findAllByUserId(UUID userId) {
        return jpaClubRepository.findAll()
                .stream()
                .filter(s -> s.getAdminUserIds().contains(userId))
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public Club save(Club club) {
        ClubEntity clubEntity = jpaClubRepository.findById(club.getId())
                .orElseGet(ClubEntity::new);
        mapper.updateEntityFromDomain(club, clubEntity);
        jpaClubRepository.save(clubEntity);
        return club;
    }

    @Override
    public void deleteById(UUID id) {
        jpaClubRepository.deleteById(id);
    }
}
