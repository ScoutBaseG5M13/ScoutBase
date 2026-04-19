package es.dimecresalessis.scoutbase.infrastructure.player.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDTO;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.infrastructure.user.web.MockUsersIT;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDTO;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("dev")
class PlayerControllerIT {

    private final MockMvc mockMvc;
    private final MockUsersIT mockUsers;
    private final ObjectMapper objectMapper;

    private UserDTO admin;
    private String jwtToken;
    private List<UUID> playersToDelete;

    @BeforeAll
    void setUp() throws Exception {
        this.admin = mockUsers.createAdmin();
        this.jwtToken = mockUsers.login(admin);
    }

    @BeforeEach
    void beforeEach() {
        playersToDelete = new ArrayList<>();
    }

    @AfterEach
    void afterEach() {
        playersToDelete.forEach(this::deletePlayerSilently);
    }

    @AfterAll
    void tearDown() throws Exception {
        mockUsers.deleteUser(admin, jwtToken);
    }

    @Test
    void shouldCreatePlayer() throws Exception {
        PlayerCreateRequest request = createDefaultRequest();

        PlayerDTO created = createPlayer(request, status().isCreated());

        assertNotNull(created.getId());
        assertEquals(request.getName(), created.getName());
        assertEquals(request.getEmail(), created.getEmail());
    }

    @Test
    void shouldFindPlayerById() throws Exception {
        PlayerDTO created = createPlayer(createDefaultRequest(), status().isCreated());

        PlayerDTO found = findPlayer(created.getId(), status().isOk());

        assertEquals(created.getId(), found.getId());
        assertEquals(created.getName(), found.getName());
    }

    @Test
    void shouldUpdatePlayer() throws Exception {
        PlayerDTO existing = createPlayer(createDefaultRequest(), status().isCreated());
        existing.setName("Updated Name");
        existing.setSurname("Updated Surname");

        updatePlayer(existing.getId(), existing, status().isOk());
        PlayerDTO updated = findPlayer(existing.getId(), status().isOk());

        assertEquals("Updated Name", updated.getName());
        assertEquals("Updated Surname", updated.getSurname());
    }

    @Test
    void shouldDeletePlayer() throws Exception {
        PlayerDTO created = createPlayer(createDefaultRequest(), status().isCreated());

        mockMvc.perform(delete(Routes.API_ROOT + Routes.PLAYERS + "/" + created.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));

        findPlayer(created.getId(), status().isNotFound());
    }

    private PlayerCreateRequest createDefaultRequest() {
        return new PlayerCreateRequest("Ronald", "Araujo", 25, "ronald@scoutbase.es", 4, "DEFENSA_CENTRAL", 1);
    }

    private PlayerDTO createPlayer(PlayerCreateRequest request, ResultMatcher expectedStatus) throws Exception {
        MvcResult result = mockMvc.perform(post(Routes.API_ROOT + Routes.PLAYERS)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(expectedStatus)
                .andReturn();

        PlayerDTO dto = extractData(result, new TypeReference<ApiResponse<PlayerDTO>>() {});
        if (dto != null) playersToDelete.add(dto.getId());
        return dto;
    }

    private PlayerDTO updatePlayer(UUID id, PlayerDTO dto, ResultMatcher expectedStatus) throws Exception {
        MvcResult result = mockMvc.perform(put(Routes.API_ROOT + Routes.PLAYERS + "/" + id)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(expectedStatus)
                .andReturn();

        return extractData(result, new TypeReference<ApiResponse<PlayerDTO>>() {});
    }

    private PlayerDTO findPlayer(UUID id, ResultMatcher expectedStatus) throws Exception {
        MvcResult result = mockMvc.perform(get(Routes.API_ROOT + Routes.PLAYERS + "/" + id)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(expectedStatus)
                .andReturn();

        return extractData(result, new TypeReference<ApiResponse<PlayerDTO>>() {});
    }

    private <T> T extractData(MvcResult result, TypeReference<ApiResponse<T>> typeReference) throws Exception {
        String content = result.getResponse().getContentAsString();
        if (content.isEmpty()) return null;
        ApiResponse<T> response = objectMapper.readValue(content, typeReference);
        return response.data();
    }

    private void deletePlayerSilently(UUID id) {
        try {
            mockMvc.perform(delete(Routes.API_ROOT + Routes.PLAYERS + "/" + id)
                    .header("Authorization", "Bearer " + jwtToken));
        } catch (Exception ignored) {}
    }
}