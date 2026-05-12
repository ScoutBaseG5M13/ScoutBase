package es.dimecresalessis.scoutbase.application.club.find;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllClubsByUserClubIdUseCaseTest {

    @Mock
    private UserClubRepository userClubRepository;

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private FindAllClubsByUserClubIdUseCase findAllClubsByUserClubIdUseCase;

    private UUID userClubId;
    private UUID clubId;
    private Club club;

    @BeforeEach
    void setUp() {
        userClubId = UUID.randomUUID();
        clubId = UUID.randomUUID();
        club = Club.builder()
                .id(clubId)
                .name("Test Club")
                .build();
    }

    @Test
    void execute_ShouldReturnClubs_WhenUserClubAndClubsExist() {
        UserClub userClub = UserClub.builder()
                .id(userClubId)
                .managedClubs(List.of(clubId))
                .build();

        when(userClubRepository.findUserClubById(userClubId)).thenReturn(Optional.of(userClub));
        when(clubRepository.findById(clubId)).thenReturn(Optional.of(club));

        List<Club> result = findAllClubsByUserClubIdUseCase.execute(userClubId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(clubId, result.get(0).getId());
        verify(userClubRepository).findUserClubById(userClubId);
        verify(clubRepository).findById(clubId);
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenUserClubDoesNotExist() {
        when(userClubRepository.findUserClubById(userClubId)).thenReturn(Optional.empty());

        List<Club> result = findAllClubsByUserClubIdUseCase.execute(userClubId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userClubRepository).findUserClubById(userClubId);
        verifyNoInteractions(clubRepository);
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenManagedClubsIsNull() {
        UserClub userClub = UserClub.builder()
                .id(userClubId)
                .managedClubs(null)
                .build();

        when(userClubRepository.findUserClubById(userClubId)).thenReturn(Optional.of(userClub));

        List<Club> result = findAllClubsByUserClubIdUseCase.execute(userClubId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userClubRepository).findUserClubById(userClubId);
        verifyNoInteractions(clubRepository);
    }

    @Test
    void execute_ShouldIgnoreClub_WhenClubIdInListButNotFoundInRepository() {
        UserClub userClub = UserClub.builder()
                .id(userClubId)
                .managedClubs(List.of(clubId))
                .build();

        when(userClubRepository.findUserClubById(userClubId)).thenReturn(Optional.of(userClub));
        when(clubRepository.findById(clubId)).thenReturn(Optional.empty());

        List<Club> result = findAllClubsByUserClubIdUseCase.execute(userClubId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clubRepository).findById(clubId);
    }
}