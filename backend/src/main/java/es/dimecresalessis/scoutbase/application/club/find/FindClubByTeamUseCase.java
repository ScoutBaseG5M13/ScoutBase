package es.dimecresalessis.scoutbase.application.club.find;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Use case for finding a {@link List<Club>} by {@link User} {@link UUID}.
 */
@Service
@AllArgsConstructor
public class FindClubByTeamUseCase {

    private final ClubRepository clubRepository;

    /**
     * Executes the search logic to find the parent Club of a team.
     *
     * @param teamId The unique identifier of the team whose parent club is being looked for.
     * @return The {@link Club} entity that owns the team; {@code null} if no matching club is found.
     */
    public Club execute(UUID teamId) {
        List<Club> clubs = clubRepository.findAll();
        return clubs.stream()
                .filter(c -> c.getTeams().contains(teamId))
                .findFirst()
                .orElse(null);
    }
}
