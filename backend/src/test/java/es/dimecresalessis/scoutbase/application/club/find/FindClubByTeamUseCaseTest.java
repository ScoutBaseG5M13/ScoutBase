package es.dimecresalessis.scoutbase.application.club.find;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindClubByTeamUseCaseTest {

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private FindClubByTeamUseCase findClubByTeamUseCase;

    private UUID teamId;
    private UUID clubId;
    private Club club;

    @BeforeEach
    void setUp() {
        teamId = UUID.randomUUID();
        clubId = UUID.randomUUID();
        club = Club.builder()
                .id(clubId)
                .name("Test Club")
                .teams(List.of(teamId))
                .build();
    }

    @Test
    void execute_ShouldReturnClub_WhenTeamExistsInClub() {
        when(clubRepository.findAll()).thenReturn(List.of(club));

        Club result = findClubByTeamUseCase.execute(teamId);

        assertNotNull(result);
        assertEquals(clubId, result.getId());
        assertTrue(result.getTeams().contains(teamId));
        verify(clubRepository).findAll();
    }

    @Test
    void execute_ShouldReturnNull_WhenTeamDoesNotExistInAnyClub() {
        Club otherClub = Club.builder()
                .id(UUID.randomUUID())
                .name("Other Club")
                .teams(List.of(UUID.randomUUID()))
                .build();

        when(clubRepository.findAll()).thenReturn(List.of(otherClub));

        Club result = findClubByTeamUseCase.execute(teamId);

        assertNull(result);
        verify(clubRepository).findAll();
    }

    @Test
    void execute_ShouldReturnNull_WhenClubsListIsEmpty() {
        when(clubRepository.findAll()).thenReturn(Collections.emptyList());

        Club result = findClubByTeamUseCase.execute(teamId);

        assertNull(result);
        verify(clubRepository).findAll();
    }

    @Test
    void execute_ShouldReturnNull_WhenClubHasNullTeams() {
        Club clubWithNullTeams = Club.builder()
                .id(UUID.randomUUID())
                .teams(null)
                .build();

        when(clubRepository.findAll()).thenReturn(List.of(clubWithNullTeams));

        assertThrows(NullPointerException.class, () -> findClubByTeamUseCase.execute(teamId));
    }
}