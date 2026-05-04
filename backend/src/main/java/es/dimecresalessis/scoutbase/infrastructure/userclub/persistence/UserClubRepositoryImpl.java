package es.dimecresalessis.scoutbase.infrastructure.userclub.persistence;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import es.dimecresalessis.scoutbase.infrastructure.userclub.persistence.mapper.UserClubEntityMapper;
import es.dimecresalessis.scoutbase.infrastructure.userteam.persistence.JpaUserTeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Infrastructure implementation of the {@link UserClubRepository} interface.
 */
@Repository
@RequiredArgsConstructor
public class UserClubRepositoryImpl implements UserClubRepository {

    /**
     * The Spring Data JPA repository providing low-level database access.
     */
    private final JpaUserClubRepository jpaUserClubRepository;
    private final JpaUserTeamRepository jpaUserTeamRepository;
    private final UserClubEntityMapper clubMapper;

    @Override
    public List<UserClub> findAll() {
        return jpaUserClubRepository.findAll()
                .stream()
                .map(clubMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<UserClub> findUserClubById(UUID id) {
        return jpaUserClubRepository.findById(id).map(clubMapper::toDomain);
    }

    @Override
    public Optional<UserClub> findUserClubByTeam(UUID teamId) {
        List<UserClubEntity> clubs = jpaUserClubRepository.findAll();
        for (UserClubEntity club : clubs) {
            if (club.getUserTeams().contains(teamId)) {
                return Optional.of(clubMapper.toDomain(club));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<UserClub> findAllUserClubsByUserId(UUID userId) {
        ArrayList<UserClub> userClubs = new ArrayList<>();
        List<UserClub> allUserClubEntities = jpaUserClubRepository.findAll().stream()
                .map(clubMapper::toDomain).toList();

        for (UserClub club : allUserClubEntities.stream().toList()) {
            //ADMIN
            if (club.getAdminUserIds().contains(userId)) {
                userClubs.add(club);
                continue;
            }

            //TRAINER, SECONDTRAINER, SCOUTER...
            List<UUID> clubTeamIds = club.getUserTeams();
            AtomicBoolean isAdded = new AtomicBoolean(false);
            for (UUID teamId : clubTeamIds) {
                jpaUserTeamRepository.findById(teamId).ifPresent(teamEntity -> {
                    if (teamEntity.getTrainer().equals(userId) || teamEntity.getSecondTrainer().equals(userId) || teamEntity.getScouters().contains(userId)) {
                        isAdded.set(true);
                    }
                });
                if (isAdded.get()) {
                    userClubs.add(club);
                    break;
                }
            }
        }
        return userClubs;
    }

    @Override
    public Optional<UserClub> findUserClubByClubId(UUID clubId) {
        UserClubEntity userClubEntity = jpaUserClubRepository.findAll()
                .stream()
                .filter(uc -> uc.getManagedClubs().contains(clubId))
                .findFirst()
                .orElse(null);

        if (userClubEntity != null) {
            return Optional.of(clubMapper.toDomain(userClubEntity));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public UserClub save(UserClub userClub) {
        UserClubEntity userClubEntity = jpaUserClubRepository.findById(userClub.getId())
                .orElseGet(UserClubEntity::new);
        clubMapper.updateEntityFromDomain(userClub, userClubEntity);
        jpaUserClubRepository.saveAndFlush(userClubEntity);
        return userClub;
    }

    @Override
    public void deleteById(UUID id) {
        jpaUserClubRepository.deleteById(id);
    }
}
