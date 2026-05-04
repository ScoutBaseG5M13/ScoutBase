package es.dimecresalessis.scoutbase.application.userclub;

import es.dimecresalessis.scoutbase.application.userclub.update.UpdateUserClubUseCase;
import es.dimecresalessis.scoutbase.domain.userclub.exception.UserClubException;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
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
class UpdateUserUserClubUseCaseTest {

    @Mock
    private UserClubRepository userClubRepository;

    @InjectMocks
    private UpdateUserClubUseCase updateUserClubUseCase;

    private UUID clubId;
    private UserClub userClub;

    @BeforeEach
    void setUp() {
        clubId = UUID.randomUUID();
        userClub = UserClub.builder()
                .id(clubId)
                .name("Original Club")
                .build();
    }

    @Test
    void execute_ShouldUpdateAndReturnClub_WhenIdsMatch() {
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(userClub));

        UserClub result = updateUserClubUseCase.execute(userClub, clubId);

        assertNotNull(result);
        assertEquals(clubId, result.getId());
        verify(userClubRepository, times(2)).findUserClubById(clubId);
        verify(userClubRepository).save(userClub);
    }

    @Test
    void execute_ShouldThrowException_WhenBodyClubNotFound() {
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.empty());

        UserClubException exception = assertThrows(UserClubException.class, () ->
                updateUserClubUseCase.execute(userClub, clubId)
        );

        assertEquals(ErrorEnum.CLUB_NOT_FOUND, exception.getErrorEnum());
        verify(userClubRepository, never()).save(any());
    }

    @Test
    void execute_ShouldThrowException_WhenIdsMismatch() {
        UUID pathId = UUID.randomUUID();
        UserClub pathUserClub = UserClub.builder().id(pathId).name("Path Club").build();

        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(userClub));
        when(userClubRepository.findUserClubById(pathId)).thenReturn(Optional.of(pathUserClub));

        assertThrows(IllegalArgumentException.class, () ->
                updateUserClubUseCase.execute(userClub, pathId)
        );

        verify(userClubRepository, never()).save(any());
    }

    @Test
    void execute_ShouldThrowException_WhenPathIdNotFound() {
        UUID pathId = UUID.randomUUID();
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(userClub));
        when(userClubRepository.findUserClubById(pathId)).thenReturn(Optional.empty());

        UserClubException exception = assertThrows(UserClubException.class, () ->
                updateUserClubUseCase.execute(userClub, pathId)
        );

        assertEquals(ErrorEnum.CLUB_NOT_FOUND, exception.getErrorEnum());
        verify(userClubRepository, never()).save(any());
    }
}