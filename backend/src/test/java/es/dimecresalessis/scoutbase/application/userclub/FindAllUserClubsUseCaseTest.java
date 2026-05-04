package es.dimecresalessis.scoutbase.application.userclub;

import es.dimecresalessis.scoutbase.application.userclub.find.FindAllUserClubsUseCase;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllUserClubsUseCaseTest {

    @Mock
    private UserClubRepository userClubRepository;

    @InjectMocks
    private FindAllUserClubsUseCase findAllUserClubsUseCase;

    private UserClub userClub;

    @BeforeEach
    void setUp() {
        userClub = UserClub.builder()
                .id(UUID.randomUUID())
                .name("Test Club")
                .build();
    }

    @Test
    void execute_ShouldReturnListOfClubs() {
        List<UserClub> userClubs = List.of(userClub);
        when(userClubRepository.findAll()).thenReturn(userClubs);

        List<UserClub> result = findAllUserClubsUseCase.execute();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userClub.getName(), result.getFirst().getName());
        verify(userClubRepository).findAll();
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenNoClubsExist() {
        when(userClubRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserClub> result = findAllUserClubsUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userClubRepository).findAll();
    }
}