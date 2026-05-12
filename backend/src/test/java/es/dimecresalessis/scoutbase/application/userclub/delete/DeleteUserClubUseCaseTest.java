package es.dimecresalessis.scoutbase.application.userclub.delete;

import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserClubUseCaseTest {

    @Mock
    private UserClubRepository userClubRepository;

    @InjectMocks
    private DeleteUserClubUseCase deleteUserClubUseCase;

    @Test
    void execute_ShouldReturnTrue_WhenClubExistsAndIsDeleted() {
        UUID clubId = UUID.randomUUID();
        UserClub userClub = UserClub.builder().id(clubId).build();

        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(userClub));

        boolean result = deleteUserClubUseCase.execute(clubId);

        assertTrue(result);
        verify(userClubRepository).findUserClubById(clubId);
        verify(userClubRepository).deleteById(clubId);
    }

    @Test
    void execute_ShouldThrowException_WhenClubDoesNotExist() {
        UUID clubId = UUID.randomUUID();
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> deleteUserClubUseCase.execute(clubId));

        verify(userClubRepository).findUserClubById(clubId);
        verify(userClubRepository, never()).deleteById(any());
    }
}