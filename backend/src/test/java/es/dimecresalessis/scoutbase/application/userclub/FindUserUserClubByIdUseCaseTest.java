package es.dimecresalessis.scoutbase.application.userclub;

import es.dimecresalessis.scoutbase.application.userclub.find.FindUserClubByIdUseCase;
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
class FindUserUserClubByIdUseCaseTest {

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
    void execute_ShouldReturnClub_WhenIdExists() {
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(userClub));

        UserClub result = findUserClubByIdUseCase.execute(clubId);

        assertNotNull(result);
        assertEquals(clubId, result.getId());
        assertEquals("Test Club", result.getName());
        verify(userClubRepository).findUserClubById(clubId);
    }

    @Test
    void execute_ShouldThrowException_WhenIdDoesNotExist() {
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                findUserClubByIdUseCase.execute(clubId)
        );

        verify(userClubRepository).findUserClubById(clubId);
    }
}