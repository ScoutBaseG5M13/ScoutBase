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
class FindAllUserClubsUseCaseTest {

    @Mock
    private UserClubRepository userClubRepository;

    @InjectMocks
    private FindAllUserClubsUseCase findAllUserClubsUseCase;

    @Test
    void execute_ShouldReturnAllUserClubs() {
        UserClub club1 = UserClub.builder().id(UUID.randomUUID()).name("Club 1").build();
        UserClub club2 = UserClub.builder().id(UUID.randomUUID()).name("Club 2").build();
        List<UserClub> expectedClubs = List.of(club1, club2);

        when(userClubRepository.findAll()).thenReturn(expectedClubs);

        List<UserClub> result = findAllUserClubsUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedClubs, result);
        verify(userClubRepository).findAll();
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenNoClubsExist() {
        when(userClubRepository.findAll()).thenReturn(List.of());

        List<UserClub> result = findAllUserClubsUseCase.execute();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(userClubRepository).findAll();
    }
}