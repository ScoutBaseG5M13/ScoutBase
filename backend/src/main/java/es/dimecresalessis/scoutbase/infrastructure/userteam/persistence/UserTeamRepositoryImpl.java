package es.dimecresalessis.scoutbase.infrastructure.userteam.persistence;

import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import es.dimecresalessis.scoutbase.infrastructure.userclub.persistence.UserClubEntity;
import es.dimecresalessis.scoutbase.infrastructure.userclub.persistence.JpaUserClubRepository;
import es.dimecresalessis.scoutbase.infrastructure.userteam.persistence.mapper.UserTeamEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Infrastructure implementation of the {@link UserTeamRepository} interface.
 */
@Repository
@RequiredArgsConstructor
public class UserTeamRepositoryImpl implements UserTeamRepository {

    private final JpaUserTeamRepository jpaUserTeamRepository;
    private final UserTeamEntityMapper mapper;
    private final JpaUserClubRepository jpaUserClubRepository;

    /**
     * Finds all Teams present in the system.
     *
     * @return A {@link List} of all {@link UserTeam} domain entities.
     */
    @Override
    public List<UserTeam> findAll() {
        return jpaUserTeamRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    /**
     * Finds a Team by its unique identifier.
     *
     * @param id The {@link UUID} of the Team.
     * @return An {@link Optional} containing the {@link UserTeam} if found, or empty otherwise.
     */
    @Override
    public Optional<UserTeam> findById(UUID id) {
        return jpaUserTeamRepository.findById(id).map(mapper::toDomain);
    }

    /**
     * Finds all Teams associated with a specific User.
     *
     * @param userId The {@link UUID} of the User.
     * @return A {@link List} of {@link UserTeam} entities linked to the User.
     */
    @Override
    public List<UserTeam> findAllByUserId(UUID userId) {
        List<UserTeamEntity> allTeams = jpaUserTeamRepository.findAll();
        List<UserClubEntity> allClubs = jpaUserClubRepository.findAll();

        Set<UUID> adminTeamIds = allClubs.stream()
                .filter(club -> club.getAdminUserIds() != null && club.getAdminUserIds().contains(userId))
                .flatMap(club -> club.getUserTeams().stream())
                .collect(Collectors.toSet());

        return allTeams.stream()
                .filter(t ->
                        Objects.equals(t.getTrainer(), userId) ||
                                Objects.equals(t.getSecondTrainer(), userId) ||
                                (t.getScouters() != null && t.getScouters().contains(userId)) ||
                                adminTeamIds.contains(t.getId())
                )
                .map(mapper::toDomain)
                .toList();
    }

    /**
     * Persists or updates a Team.
     *
     * @param userTeam The {@link UserTeam} domain object to be saved.
     * @return The saved {@link UserTeam} entity.
     */
    @Override
    public UserTeam save(UserTeam userTeam) {
        UserTeamEntity userTeamEntity = jpaUserTeamRepository.findById(userTeam.getId())
                .orElseGet(UserTeamEntity::new);
        mapper.updateEntityFromDomain(userTeam, userTeamEntity);
        jpaUserTeamRepository.saveAndFlush(userTeamEntity);
        return userTeam;
    }

    /**
     * Deletes a Team.
     *
     * @param id The {@link UUID} of the userteam to delete.
     */
    @Override
    public void deleteById(UUID id) {
        jpaUserTeamRepository.deleteById(id);
    }
}
