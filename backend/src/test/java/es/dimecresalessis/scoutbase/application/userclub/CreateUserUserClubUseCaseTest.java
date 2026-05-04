package es.dimecresalessis.scoutbase.application.userclub;

import es.dimecresalessis.scoutbase.application.userclub.create.CreateUserClubUseCase;
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
class CreateUserUserClubUseCaseTest {

    @Mock
    private UserClubRepository userClubRepository;

    @InjectMocks
    private CreateUserClubUseCase createUserClubUseCase;

    private UserClub userClub;
    private UUID clubId;

    @BeforeEach
    void setUp() {
        clubId = UUID.randomUUID();
        userClub = UserClub.builder()
                .id(clubId)
                .name("Test Club")
                .build();
    }

    @Test
    void execute_ShouldCreateClubSuccessfully() throws UserClubException {
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.empty());
        when(userClubRepository.save(userClub)).thenReturn(userClub);

        UserClub result = createUserClubUseCase.execute(userClub);

        assertNotNull(result);
        assertEquals(clubId, result.getId());
        verify(userClubRepository).findUserClubById(clubId);
        verify(userClubRepository).save(userClub);
    }

    @Test
    void execute_ShouldThrowException_WhenClubIsNull() {
        UserClubException exception = assertThrows(UserClubException.class, () ->
                createUserClubUseCase.execute(null)
        );

        assertEquals(ErrorEnum.CLUB_IS_NULL, exception.getErrorEnum());
        verifyNoInteractions(userClubRepository);
    }

    @Test
    void execute_ShouldThrowException_WhenClubIdIsNull() {
        UserClub userClubWithoutId = new UserClub();
        userClubWithoutId.setName("No ID Club");

        UserClubException exception = assertThrows(UserClubException.class, () ->
                createUserClubUseCase.execute(userClubWithoutId)
        );

        assertEquals(ErrorEnum.CLUB_ID_IS_NULL, exception.getErrorEnum());
        verifyNoInteractions(userClubRepository);
    }

    @Test
    void execute_ShouldThrowException_WhenClubAlreadyExists() {
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(userClub));

        UserClubException exception = assertThrows(UserClubException.class, () ->
                createUserClubUseCase.execute(userClub)
        );

        assertEquals(ErrorEnum.CLUB_ALREADY_EXISTS, exception.getErrorEnum());
        verify(userClubRepository).findUserClubById(clubId);
        verify(userClubRepository, never()).save(any());
    }
}