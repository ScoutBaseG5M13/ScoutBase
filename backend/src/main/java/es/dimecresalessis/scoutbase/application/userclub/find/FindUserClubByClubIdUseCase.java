package es.dimecresalessis.scoutbase.application.userclub.find;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Use case for finding a {@link UserClub} by managed {@link Club} {@link UUID}.
 */
@Service
@AllArgsConstructor
public class FindUserClubByClubIdUseCase {

    private final UserClubRepository userClubRepository;

    public Optional<UserClub> execute(UUID clubId) {
        return userClubRepository.findUserClubByClubId(clubId);
    }
}
