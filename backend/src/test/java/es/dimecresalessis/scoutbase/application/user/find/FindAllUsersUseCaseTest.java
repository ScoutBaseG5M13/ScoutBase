package es.dimecresalessis.scoutbase.application.user.find;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAllUsersUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindAllUsersUseCase findAllUsersUseCase;

    @Test
    void execute_ShouldReturnAllUsers() {
        User user1 = User.builder().id(UUID.randomUUID()).name("User One").build();
        User user2 = User.builder().id(UUID.randomUUID()).name("User Two").build();
        List<User> expectedUsers = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> result = findAllUsersUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedUsers, result);
        verify(userRepository).findAll();
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> result = findAllUsersUseCase.execute();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(userRepository).findAll();
    }
}