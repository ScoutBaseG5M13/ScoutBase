package es.dimecresalessis.scoutbase.application.user.find;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FindUserRoleInClubUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindUserRoleInClubUseCase.class);
    private final ClubRepository clubRepository;

    /**
     * Resolves the role of a user within a specific club.
     *
     * @param user The user to evaluate.
     * @param clubId The unique identifier of the club.
     * @return {@link RoleEnum#ADMIN} if the user is in the club's admin list,
     * or {@code null} otherwise.
     */
    public RoleEnum execute(User user, UUID clubId) {
        Optional<Club> club = clubRepository.findById(clubId);
        if (club.isPresent()) {
            boolean clubHasUser = club.get().getAdminUserIds()
                    .stream()
                    .anyMatch(t -> t.equals(user.getId()));
            if (clubHasUser) {
                logger.info("[AUTH] User '{}' has ROLE '{}' in CLUB '{}'", user.getUsername(), RoleEnum.ADMIN, club.get().getName());
                return RoleEnum.ADMIN;
            }
        }
        return null;
    }
}
