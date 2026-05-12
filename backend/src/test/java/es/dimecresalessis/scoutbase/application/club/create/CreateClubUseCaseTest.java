package es.dimecresalessis.scoutbase.application.club.create;

import es.dimecresalessis.scoutbase.domain.club.exception.ClubException;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
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
class CreateClubUseCaseTest {

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private UserClubRepository userClubRepository;

    @InjectMocks
    private CreateClubUseCase createClubUseCase;

    private UUID userClubId;
    private Club club;
    private UUID clubId;

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
    void execute_ShouldCreateClubSuccessfully() throws ClubException {
        when(clubRepository.findById(clubId)).thenReturn(Optional.empty());
        when(clubRepository.save(club)).thenReturn(club);
        when(userClubRepository.findUserClubById(any())).thenReturn(Optional.of(UserClub.builder().id(userClubId).build()));

        Club result = createClubUseCase.execute(club, userClubId);

        assertNotNull(result);
        assertEquals(clubId, result.getId());
        verify(clubRepository).findById(clubId);
        verify(clubRepository).save(club);
    }

    @Test
    void execute_ShouldThrowException_WhenClubIsNull() {
        ClubException exception = assertThrows(ClubException.class, () ->
                createClubUseCase.execute(null, userClubId)
        );

        assertEquals(ErrorEnum.CLUB_IS_NULL, exception.getErrorEnum());
        verifyNoInteractions(clubRepository);
    }

    @Test
    void execute_ShouldThrowException_WhenClubIdIsNull() {
        Club clubWithoutId = new Club();
        clubWithoutId.setName("No ID Club");

        ClubException exception = assertThrows(ClubException.class, () ->
                createClubUseCase.execute(clubWithoutId, userClubId)
        );

        assertEquals(ErrorEnum.CLUB_ID_IS_NULL, exception.getErrorEnum());
        verifyNoInteractions(clubRepository);
    }

    @Test
    void execute_ShouldThrowException_WhenClubAlreadyExists() {
        when(clubRepository.findById(clubId)).thenReturn(Optional.of(club));

        ClubException exception = assertThrows(ClubException.class, () ->
                createClubUseCase.execute(club, userClubId)
        );

        assertEquals(ErrorEnum.CLUB_ALREADY_EXISTS, exception.getErrorEnum());
        verify(clubRepository).findById(clubId);
        verify(clubRepository, never()).save(any());
    }
}