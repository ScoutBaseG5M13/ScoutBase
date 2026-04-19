package es.dimecresalessis.scoutbase.application.club;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
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
class FindAllClubsUseCaseTest {

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private FindAllClubsUseCase findAllClubsUseCase;

    private Club club;

    @BeforeEach
    void setUp() {
        club = Club.builder()
                .id(UUID.randomUUID())
                .name("Test Club")
                .build();
    }

    @Test
    void execute_ShouldReturnListOfClubs() {
        List<Club> clubs = List.of(club);
        when(clubRepository.findAll()).thenReturn(clubs);

        List<Club> result = findAllClubsUseCase.execute();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(club.getName(), result.get(0).getName());
        verify(clubRepository).findAll();
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenNoClubsExist() {
        when(clubRepository.findAll()).thenReturn(Collections.emptyList());

        List<Club> result = findAllClubsUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clubRepository).findAll();
    }
}