package es.dimecresalessis.scoutbase.application.userclub.find;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Use case for finding all {@link List <UserClub>}.
 */
@Service
@AllArgsConstructor
public class FindAllUserClubsUseCase {

    private final UserClubRepository userClubRepository;

    public List<UserClub> execute() {
        return userClubRepository.findAll();
    }
}
