package es.dimecresalessis.scoutbase.infrastructure.club.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA implementation for club persistence.
 * <p>
 * It leverages Spring Data's abstraction to provide low-level CRUD operations against
 * the database for {@link ClubEntity} objects.
 * </p>
 */
public interface JpaClubRepository extends JpaRepository<ClubEntity, UUID> {

    @Query(value = "SELECT * FROM club c WHERE :teamId = ANY(c.teams)", nativeQuery = true)
    List<ClubEntity> findAllByTeam(@Param("teamId") UUID teamId);

    @Query(value = "SELECT * FROM club c WHERE :userId = ANY(c.admin_user_ids)", nativeQuery = true)
    List<ClubEntity> findAllByUserId(@Param("userId") UUID userId);
}
