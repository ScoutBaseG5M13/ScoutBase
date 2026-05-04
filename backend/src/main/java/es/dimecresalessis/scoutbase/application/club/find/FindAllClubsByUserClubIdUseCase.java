package es.dimecresalessis.scoutbase.application.club.find;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Use case for finding a {@link List<Club>} by {@link User} {@link UUID}.
 */
@Service
@AllArgsConstructor
public class FindAllClubsByUserClubIdUseCase {

    private final UserClubRepository userClubRepository;
    private final ClubRepository clubRepository;

    public List<Club> execute(UUID userClubId) {
        Optional<UserClub> userClub = userClubRepository.findUserClubById(userClubId);
        List<Club> clubs = new ArrayList<>();
        if (userClub.isPresent()) {
            for (UUID clubId : userClub.get().getManagedClubs()) {
                Optional<Club> tempClub = clubRepository.findById(clubId);
                if (tempClub.isPresent()) {
                    clubs.add(tempClub.get());
                }
            }
        }
        return clubs;
    }
}
