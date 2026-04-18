package es.dimecresalessis.scoutbase.infrastructure.player.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDTO;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.infrastructure.shared.utils.JsonUtils;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("dev")
public class PlayerControllerIT {

    private final MockMvc mockMvc;
    private final MockUsersIT mockUsers;
    private final ObjectMapper objectMapper;

    private UserDTO admin;
    private String jwtToken;

    private PlayerDTO player;
    private UUID teamId;

    @BeforeAll
    void setUp() throws Exception {
        this.admin = mockUsers.createAdmin();
        this.jwtToken = mockUsers.login(admin);
    }

    @AfterAll
    void tearDown() throws Exception {
        mockUsers.deleteUser(admin, jwtToken);
    }

    @BeforeEach
    void beforeEach() {
        teamId = UUID.randomUUID();
        player = new PlayerDTO(
                UUID.randomUUID(),
                teamId,
                "Leo",
                "Testi",
                25,
                "test@test.ar",
                10,
                "DELANTERO",
                "SENIOR",
                4
        );
    }

    @Test
    void shouldCreatePlayer() {
        PlayerDTO createdPlayer = null;
        try {
            createdPlayer = createPlayer(player, status().isCreated());

            assertNotNull(createdPlayer);
            assertNotNull(createdPlayer.getId());
            assertEquals(player.getName(), createdPlayer.getName());
            assertEquals(player.getSurname(), createdPlayer.getSurname());
        } finally {
            if (createdPlayer != null && createdPlayer.getId() != null) {
                deletePlayerSilently(createdPlayer.getId().toString());
            }
        }
    }

    @Test
    void shouldCreatePlayer_WhenPlayerHasNoId() {
        PlayerDTO createdPlayer = null;
        try {
            player.setId(null);
            createdPlayer = createPlayer(player, status().isCreated());

            assertNotNull(createdPlayer);
            assertNotNull(createdPlayer.getId());
            assertEquals(player.getName(), createdPlayer.getName());
            assertEquals(player.getTeamId(), createdPlayer.getTeamId());
            assertEquals(player.getEmail(), createdPlayer.getEmail());
        } finally {
            if (createdPlayer != null && createdPlayer.getId() != null) {
                deletePlayerSilently(createdPlayer.getId().toString());
            }
        }
    }

    @Test
    void shouldFindPlayerById() {
        PlayerDTO createdPlayer = null;
        try {
            createdPlayer = createPlayer(player, status().isCreated());
            PlayerDTO foundPlayer = findPlayer(createdPlayer.getId().toString(), status().isOk());

            assertNotNull(foundPlayer);
            assertEquals(createdPlayer.getId(), foundPlayer.getId());
            assertEquals(createdPlayer.getName(), foundPlayer.getName());
            assertEquals(createdPlayer.getEmail(), foundPlayer.getEmail());
        } finally {
            if (createdPlayer != null && createdPlayer.getId() != null) {
                deletePlayerSilently(createdPlayer.getId().toString());
            }
        }
    }

    @Test
    void shouldThrowPlayerException_WhenPlayerIdDoesNotExist() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get(Routes.API_ROOT + Routes.PLAYERS + "/" + nonExistentId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldUpdatePlayer() {
        PlayerDTO oldPlayer = null;
        try {
            oldPlayer = createPlayer(player, status().isCreated());
            PlayerDTO updateRequest = new PlayerDTO(
                    oldPlayer.getId(),
                    oldPlayer.getTeamId(),
                    "Updated name",
                    "Updated surname",
                    oldPlayer.getAge(),
                    "updated@email.com",
                    oldPlayer.getNumber(),
                    oldPlayer.getPosition(),
                    oldPlayer.getCategory(),
                    7
            );

            updatePlayer(oldPlayer.getId().toString(), updateRequest, status().isOk());
            PlayerDTO finalPlayer = findPlayer(oldPlayer.getId().toString(), status().isOk());

            assertNotNull(finalPlayer);
            assertEquals(updateRequest.getName(), finalPlayer.getName());
            assertEquals(updateRequest.getEmail(), finalPlayer.getEmail());
        } finally {
            if (oldPlayer != null && oldPlayer.getId() != null) {
                deletePlayerSilently(oldPlayer.getId().toString());
            }
        }
    }

    @Test
    void shouldDeletePlayer() {
        PlayerDTO createdPlayer = createPlayer(player, status().isCreated());
        assertNotNull(createdPlayer);

        deletePlayer(createdPlayer.getId().toString(), status().isOk());

        findPlayer(createdPlayer.getId().toString(), status().isNotFound());
    }

    private PlayerDTO findPlayer(String id, ResultMatcher status) {
        try {
            MvcResult result = mockMvc.perform(get(Routes.API_ROOT + Routes.PLAYERS + "/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status)
                    .andReturn();
            return extractPlayerDto(result);
        } catch (Exception e) {
            return null;
        }
    }

    private PlayerDTO createPlayer(PlayerDTO dto, ResultMatcher status) {
        try {
            MvcResult result = mockMvc.perform(post(Routes.API_ROOT + Routes.PLAYERS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.toJson(dto))
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status)
                    .andExpect(jsonPath("$.success").value(true))
                    .andReturn();
            return extractPlayerDto(result);
        } catch (Exception e) {
            return null;
        }
    }

    private PlayerDTO updatePlayer(String id, PlayerDTO newDto, ResultMatcher status) {
        try {
            MvcResult result = mockMvc.perform(put(Routes.API_ROOT + Routes.PLAYERS + "/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.toJson(newDto))
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status)
                    .andExpect(jsonPath("$.success").value(true))
                    .andReturn();
            return extractPlayerDto(result);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean deletePlayer(String id, ResultMatcher status) {
        try {
            MvcResult result =  mockMvc.perform(delete(Routes.API_ROOT + Routes.PLAYERS + "/" + id)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status)
                    .andExpect(jsonPath("$.success").value(true))
                    .andReturn();
            return extractBoolean(result);
        } catch (Exception e) {
            return false;
        }
    }

    private void deletePlayerSilently(String id) {
        try {
            mockMvc.perform(delete(Routes.API_ROOT + Routes.PLAYERS + "/" + id)
                    .header("Authorization", "Bearer " + jwtToken));
        } catch (Exception ignored) {}
    }

    private PlayerDTO extractPlayerDto(MvcResult result) throws Exception {
        String jsonResponse = result.getResponse().getContentAsString();
        if (jsonResponse.isEmpty()) return null;
        ApiResponse<PlayerDTO> response = objectMapper.readValue(jsonResponse, new TypeReference<ApiResponse<PlayerDTO>>() {});
        return response.data();
    }

    private Boolean extractBoolean(MvcResult result) throws Exception {
        String jsonResponse = result.getResponse().getContentAsString();
        if (jsonResponse.isEmpty()) return null;
        ApiResponse<Boolean> response = objectMapper.readValue(jsonResponse, new TypeReference<ApiResponse<Boolean>>() {});
        return response.data();
    }
}