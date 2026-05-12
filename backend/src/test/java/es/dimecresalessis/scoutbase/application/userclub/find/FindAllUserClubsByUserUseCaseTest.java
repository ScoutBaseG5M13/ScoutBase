package es.dimecresalessis.scoutbase.application.userclub.find;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllUserClubsByUserUseCaseTest {

    @Mock
    private UserClubRepository userClubRepository;

    @InjectMocks
    private FindAllUserClubsByUserUseCase findAllUserClubsByUserUseCase;

    @Test
    void execute_ShouldReturnClubs_WhenUserHasAssociations() {
        UUID userId = UUID.randomUUID();
        UserClub club1 = UserClub.builder().id(UUID.randomUUID()).name("Club A").build();
        UserClub club2 = UserClub.builder().id(UUID.randomUUID()).name("Club B").build();
        List<UserClub> expectedClubs = List.of(club1, club2);

        when(userClubRepository.findAllUserClubsByUserId(userId)).thenReturn(expectedClubs);

        List<UserClub> result = findAllUserClubsByUserUseCase.execute(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedClubs, result);
        verify(userClubRepository).findAllUserClubsByUserId(userId);
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenUserHasNoAssociations() {
        UUID userId = UUID.randomUUID();
        when(userClubRepository.findAllUserClubsByUserId(userId)).thenReturn(List.of());

        List<UserClub> result = findAllUserClubsByUserUseCase.execute(userId);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(userClubRepository).findAllUserClubsByUserId(userId);
    }
}