package es.dimecresalessis.scoutbase.application.userclub;

import es.dimecresalessis.scoutbase.application.userclub.delete.DeleteUserClubUseCase;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import org.junit.jupiter.api.BeforeEach;
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
class DeleteUserUserClubUseCaseTest {

    @Mock
    private UserClubRepository userClubRepository;

    @InjectMocks
    private DeleteUserClubUseCase deleteUserClubUseCase;

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
    void execute_ShouldDeleteClubSuccessfully_WhenIdExists() {
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(userClub));
        doNothing().when(userClubRepository).deleteById(clubId);

        boolean result = deleteUserClubUseCase.execute(clubId);

        assertTrue(result);
        verify(userClubRepository).findUserClubById(clubId);
        verify(userClubRepository).deleteById(clubId);
    }

    @Test
    void execute_ShouldThrowException_WhenIdDoesNotExist() {
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                deleteUserClubUseCase.execute(clubId)
        );

        verify(userClubRepository).findUserClubById(clubId);
        verify(userClubRepository, never()).deleteById(any());
    }
}