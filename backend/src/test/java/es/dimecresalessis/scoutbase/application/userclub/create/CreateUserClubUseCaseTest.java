package es.dimecresalessis.scoutbase.application.userclub.create;

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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserClubUseCaseTest {

    @Mock
    private UserClubRepository userClubRepository;

    @InjectMocks
    private CreateUserClubUseCase createUserClubUseCase;

    private UUID clubId;
    private UserClub userClub;

    @BeforeEach
    void setUp() {
        clubId = UUID.randomUUID();
        userClub = UserClub.builder()
                .id(clubId)
                .name("New Scout Club")
                .build();
    }

    @Test
    void execute_ShouldCreateUserClub_WhenDataIsValid() throws UserClubException {
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.empty());

        UserClub result = createUserClubUseCase.execute(userClub);

        assertNotNull(result);
        assertEquals(clubId, result.getId());
        verify(userClubRepository).findUserClubById(clubId);
        verify(userClubRepository).save(userClub);
    }

    @Test
    void execute_ShouldThrowException_WhenUserClubIsNull() {
        UserClubException exception = assertThrows(UserClubException.class, () ->
                createUserClubUseCase.execute(null)
        );

        assertEquals(ErrorEnum.USER_CLUB_IS_NULL, exception.getErrorEnum());
        verify(userClubRepository, never()).save(any());
    }

    @Test
    void execute_ShouldThrowException_WhenIdIsNull() {
        userClub.setId(null);

        UserClubException exception = assertThrows(UserClubException.class, () ->
                createUserClubUseCase.execute(userClub)
        );

        assertEquals(ErrorEnum.USER_CLUB_ID_IS_NULL, exception.getErrorEnum());
        verify(userClubRepository, never()).save(any());
    }

    @Test
    void execute_ShouldThrowException_WhenClubAlreadyExists() {
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(userClub));

        UserClubException exception = assertThrows(UserClubException.class, () ->
                createUserClubUseCase.execute(userClub)
        );

        assertEquals(ErrorEnum.USER_CLUB_ALREADY_EXISTS, exception.getErrorEnum());
        assertTrue(exception.getMessage().contains(clubId.toString()));
        verify(userClubRepository, never()).save(any());
    }
}