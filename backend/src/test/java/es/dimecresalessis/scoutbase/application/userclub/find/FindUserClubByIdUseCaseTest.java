package es.dimecresalessis.scoutbase.application.userclub.find;

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
class FindUserClubByIdUseCaseTest {

    @Mock
    private UserClubRepository userClubRepository;

    @InjectMocks
    private FindUserClubByIdUseCase findUserClubByIdUseCase;

    private UUID clubId;
    private UserClub userClub;

    @BeforeEach
    void setUp() {
        clubId = UUID.randomUUID();
        userClub = UserClub.builder()
                .id(clubId)
                .name("Test Club")
                .build();
    }

    @Test
    void execute_ShouldReturnUserClub_WhenIdExists() {
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(userClub));

        UserClub result = findUserClubByIdUseCase.execute(clubId);

        assertNotNull(result);
        assertEquals(clubId, result.getId());
        assertEquals("Test Club", result.getName());
        verify(userClubRepository).findUserClubById(clubId);
    }

    @Test
    void execute_ShouldThrowUserClubException_WhenIdDoesNotExist() {
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.empty());

        UserClubException exception = assertThrows(UserClubException.class, () ->
                findUserClubByIdUseCase.execute(clubId)
        );

        assertEquals(ErrorEnum.USER_CLUB_NOT_FOUND, exception.getErrorEnum());
        assertTrue(exception.getMessage().contains(clubId.toString()));
        verify(userClubRepository).findUserClubById(clubId);
    }
}