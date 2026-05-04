package es.dimecresalessis.scoutbase.application.userclub.find;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.userclub.exception.UserClubException;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Use case for finding a {@link List < Club >} by {@link User} {@link UUID}.
 */
@Service
@AllArgsConstructor
public class FindUserClubByUserTeamUseCase {

    private final UserClubRepository userClubRepository;

    public UserClub execute(UUID teamId) {
        List<UserClub> userClubs = userClubRepository.findAll();
        return userClubs.stream()
                .filter(c -> c.getUserTeams() != null && c.getUserTeams().contains(teamId))
                .findFirst()
                .orElseThrow(() -> new UserClubException(ErrorEnum.NO_CLUB_HAS_BEEN_FOUND));
    }
}
