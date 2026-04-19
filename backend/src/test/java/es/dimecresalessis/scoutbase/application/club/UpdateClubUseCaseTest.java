package es.dimecresalessis.scoutbase.application.club;

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

    @BeforeEach
    void setUp() {
        clubId = UUID.randomUUID();
        club = Club.builder()
                .id(clubId)
                .name("Original Club")
                .build();
    }

    @Test
    void execute_ShouldUpdateAndReturnClub_WhenIdsMatch() {
        when(clubRepository.findById(clubId)).thenReturn(Optional.of(club));

        Club result = updateClubUseCase.execute(club, clubId);

        assertNotNull(result);
        assertEquals(clubId, result.getId());
        verify(clubRepository, times(2)).findById(clubId);
        verify(clubRepository).save(club);
    }

    @Test
    void execute_ShouldThrowException_WhenBodyClubNotFound() {
        when(clubRepository.findById(clubId)).thenReturn(Optional.empty());

        ClubException exception = assertThrows(ClubException.class, () ->
                updateClubUseCase.execute(club, clubId)
        );

        assertEquals(ErrorEnum.CLUB_NOT_FOUND, exception.getErrorEnum());
        verify(clubRepository, never()).save(any());
    }

    @Test
    void execute_ShouldThrowException_WhenIdsMismatch() {
        UUID pathId = UUID.randomUUID();
        Club pathClub = Club.builder().id(pathId).name("Path Club").build();

        when(clubRepository.findById(clubId)).thenReturn(Optional.of(club));
        when(clubRepository.findById(pathId)).thenReturn(Optional.of(pathClub));

        assertThrows(IllegalArgumentException.class, () ->
                updateClubUseCase.execute(club, pathId)
        );

        verify(clubRepository, never()).save(any());
    }

    @Test
    void execute_ShouldThrowException_WhenPathIdNotFound() {
        UUID pathId = UUID.randomUUID();
        when(clubRepository.findById(clubId)).thenReturn(Optional.of(club));
        when(clubRepository.findById(pathId)).thenReturn(Optional.empty());

        ClubException exception = assertThrows(ClubException.class, () ->
                updateClubUseCase.execute(club, pathId)
        );

        assertEquals(ErrorEnum.CLUB_NOT_FOUND, exception.getErrorEnum());
        verify(clubRepository, never()).save(any());
    }
}