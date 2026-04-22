package es.dimecresalessis.scoutbase.application.club.find;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Use case for finding a {@link List<Club>} by {@link User} {@link UUID}.
 */
@Service
@AllArgsConstructor
public class FindAllClubsByUserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindAllClubsByUserUseCase.class);
    private final ClubRepository clubRepository;

    public List<Club> execute(UUID userId) {
        return clubRepository.findAllClubsByUserId(userId);
    }
}
