package es.dimecresalessis.scoutbase.application.club;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
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
class DeleteClubUseCaseTest {

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private DeleteClubUseCase deleteClubUseCase;

    private UUID clubId;
    private Club club;

    @BeforeEach
    void setUp() {
        clubId = UUID.randomUUID();
        club = Club.builder()
                .id(clubId)
                .name("Test Club")
                .build();
    }

    @Test
    void execute_ShouldDeleteClubSuccessfully_WhenIdExists() {
        when(clubRepository.findById(clubId)).thenReturn(Optional.of(club));
        doNothing().when(clubRepository).deleteById(clubId);

        boolean result = deleteClubUseCase.execute(clubId);

        assertTrue(result);
        verify(clubRepository).findById(clubId);
        verify(clubRepository).deleteById(clubId);
    }

    @Test
    void execute_ShouldThrowException_WhenIdDoesNotExist() {
        when(clubRepository.findById(clubId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                deleteClubUseCase.execute(clubId)
        );

        verify(clubRepository).findById(clubId);
        verify(clubRepository, never()).deleteById(any());
    }
}