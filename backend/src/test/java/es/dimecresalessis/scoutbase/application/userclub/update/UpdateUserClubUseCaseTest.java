package es.dimecresalessis.scoutbase.application.userclub.update;

import es.dimecresalessis.scoutbase.application.user.find.FindUserByIdUseCase;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.userclub.exception.UserClubException;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserClubUseCaseTest {

    @Mock
    private UserClubRepository userClubRepository;

    @Mock
    private FindUserByIdUseCase findUserByIdUseCase;

    @InjectMocks
    private UpdateUserClubUseCase updateUserClubUseCase;

    private UUID clubId;
    private UserClub userClub;
    private UserClub savedUserClub;

    @BeforeEach
    void setUp() {
        clubId = UUID.randomUUID();
        userClub = UserClub.builder()
                .id(clubId)
                .name("Updated Club")
                .adminUserIds(List.of(UUID.randomUUID()))
                .build();
        savedUserClub = UserClub.builder()
                .id(clubId)
                .name("Original Club")
                .build();
    }

    @Test
    void execute_ShouldUpdateAndReturnClub_WhenValidRequest() {
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(savedUserClub));
        when(userClubRepository.findById(clubId)).thenReturn(Optional.of(savedUserClub));

        UserClub result = updateUserClubUseCase.execute(userClub, clubId);

        assertNotNull(result);
        assertEquals(clubId, result.getId());
        verify(findUserByIdUseCase).execute(any(UUID.class));
        verify(userClubRepository).save(any(UserClub.class));
    }

    @Test
    void execute_ShouldThrowIllegalArgumentException_WhenIdsDoNotMatch() {
        UUID pathId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () ->
                updateUserClubUseCase.execute(userClub, pathId)
        );

        verify(userClubRepository, never()).save(any());
    }

    @Test
    void execute_ShouldThrowUserClubException_WhenClubNotFound() {
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.empty());

        UserClubException exception = assertThrows(UserClubException.class, () ->
                updateUserClubUseCase.execute(userClub, clubId)
        );

        assertEquals(ErrorEnum.USER_CLUB_NOT_FOUND, exception.getErrorEnum());
    }

    @Test
    void execute_ShouldThrowUserClubException_WhenAdminUserDoesNotExist() {
        UUID userId = userClub.getAdminUserIds().get(0);
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(savedUserClub));
        when(findUserByIdUseCase.execute(userId)).thenThrow(new NoSuchElementException());

        UserClubException exception = assertThrows(UserClubException.class, () ->
                updateUserClubUseCase.execute(userClub, clubId)
        );

        assertEquals(ErrorEnum.USER_NOT_FOUND, exception.getErrorEnum());
        assertTrue(exception.getMessage().contains(userId.toString()));
    }

    @Test
    void execute_ShouldCallMatchWithObject_WhenClubIsFound() {
        UserClub incomingClub = mock(UserClub.class);
        UUID adminId = UUID.randomUUID();
        when(incomingClub.getId()).thenReturn(clubId);
        when(incomingClub.getAdminUserIds()).thenReturn(List.of(adminId));
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(savedUserClub));
        when(userClubRepository.findById(clubId)).thenReturn(Optional.of(savedUserClub));

        updateUserClubUseCase.execute(incomingClub, clubId);

        verify(incomingClub).matchWithObject(savedUserClub);
        verify(userClubRepository).save(incomingClub);
    }
}