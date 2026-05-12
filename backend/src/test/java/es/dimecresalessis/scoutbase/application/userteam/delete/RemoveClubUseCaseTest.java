package es.dimecresalessis.scoutbase.application.userteam.delete;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveClubUseCaseTest {

    @Mock
    private UserClubRepository userClubRepository;

    @InjectMocks
    private RemoveClubUseCase removeClubUseCase;

    @Test
    void execute_ShouldRemoveClub_WhenUserClubExists() {
        UUID userClubId = UUID.randomUUID();
        UUID clubIdToRemove = UUID.randomUUID();
        List<UUID> managedClubs = new ArrayList<>(List.of(clubIdToRemove, UUID.randomUUID()));

        UserClub userClub = UserClub.builder()
                .id(userClubId)
                .managedClubs(managedClubs)
                .build();

        when(userClubRepository.findUserClubById(userClubId)).thenReturn(Optional.of(userClub));

        boolean result = removeClubUseCase.execute(userClubId, clubIdToRemove);

        assertTrue(result);
        assertFalse(userClub.getManagedClubs().contains(clubIdToRemove));
        verify(userClubRepository).save(userClub);
    }

    @Test
    void execute_ShouldThrowException_WhenUserClubNotFound() {
        UUID userClubId = UUID.randomUUID();
        UUID clubId = UUID.randomUUID();
        when(userClubRepository.findUserClubById(userClubId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                removeClubUseCase.execute(userClubId, clubId)
        );
        verify(userClubRepository, never()).save(any());
    }

    @Test
    void execute_ShouldSucceed_WhenClubIdIsNotInList() {
        UUID userClubId = UUID.randomUUID();
        UUID randomClubId = UUID.randomUUID();
        UserClub userClub = UserClub.builder()
                .id(userClubId)
                .managedClubs(new ArrayList<>(List.of(UUID.randomUUID())))
                .build();

        when(userClubRepository.findUserClubById(userClubId)).thenReturn(Optional.of(userClub));

        boolean result = removeClubUseCase.execute(userClubId, randomClubId);

        assertTrue(result);
        verify(userClubRepository).save(userClub);
    }
}