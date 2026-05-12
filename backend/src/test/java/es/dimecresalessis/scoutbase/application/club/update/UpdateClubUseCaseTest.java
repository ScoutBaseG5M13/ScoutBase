package es.dimecresalessis.scoutbase.application.club.update;

import es.dimecresalessis.scoutbase.domain.club.exception.ClubException;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateClubUseCaseTest {

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private UpdateClubUseCase updateClubUseCase;

    private UUID clubId;
    private Club club;
    private Club savedClub;

    @BeforeEach
    void setUp() {
        clubId = UUID.randomUUID();
        club = Club.builder()
                .id(clubId)
                .name("Updated Name")
                .build();
        savedClub = Club.builder()
                .id(clubId)
                .name("Old Name")
                .build();
    }

    @Test
    void execute_ShouldUpdateClubSuccessfully() {
        when(clubRepository.findById(clubId)).thenReturn(Optional.of(savedClub));
        when(clubRepository.save(any(Club.class))).thenReturn(club);

        Club result = updateClubUseCase.execute(club, clubId);

        assertNotNull(result);
        assertEquals(clubId, result.getId());
        verify(clubRepository, times(3)).findById(clubId);
        verify(clubRepository).save(any(Club.class));
    }

    @Test
    void execute_ShouldThrowIllegalArgumentException_WhenIdsDoNotMatch() {
        UUID differentId = UUID.randomUUID();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                updateClubUseCase.execute(club, differentId)
        );

        assertTrue(exception.getMessage().contains("does not match"));
        verifyNoInteractions(clubRepository);
    }

    @Test
    void execute_ShouldThrowClubException_WhenClubNotFound() {
        when(clubRepository.findById(clubId)).thenReturn(Optional.empty());

        ClubException exception = assertThrows(ClubException.class, () ->
                updateClubUseCase.execute(club, clubId)
        );

        assertEquals(ErrorEnum.CLUB_NOT_FOUND, exception.getErrorEnum());
        verify(clubRepository).findById(clubId);
        verify(clubRepository, never()).save(any());
    }

    @Test
    void execute_ShouldCallMatchWithObject_WhenUpdating() {
        Club spyClub = spy(club);
        when(clubRepository.findById(clubId)).thenReturn(Optional.of(savedClub));

        updateClubUseCase.execute(spyClub, clubId);

        verify(spyClub).matchWithObject(savedClub);
        verify(clubRepository).save(spyClub);
    }
}