package es.dimecresalessis.scoutbase.application.userclub.find;

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
 * Use case for finding a {@link List<UserClub>} by {@link User} {@link UUID}.
 */
@Service
@AllArgsConstructor
public class FindAllUserClubsByUserUseCase {

    private final UserClubRepository userClubRepository;

    public List<UserClub> execute(UUID userId) {
        return userClubRepository.findAllUserClubsByUserId(userId);
    }
}
