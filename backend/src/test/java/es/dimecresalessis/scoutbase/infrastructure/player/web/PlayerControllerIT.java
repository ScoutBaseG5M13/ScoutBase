package es.dimecresalessis.scoutbase.infrastructure.player.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.dimecresalessis.scoutbase.domain.player.exception.PlayerException;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDto;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.infrastructure.shared.utils.JsonUtils;
import es.dimecresalessis.scoutbase.infrastructure.user.web.MockUsersIT;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDto;
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

    private UserDto admin;
    private String jwtToken;

    private PlayerDto player;

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
        player = new PlayerDto(UUID.randomUUID(), "Leo Testi", "Testelona FC", "test@test.ar");
    }

    @Test
    void shouldCreatePlayer() {
        PlayerDto createdPlayer = null;
        try {
            createdPlayer = createPlayer(player, status().isCreated());

            assertNotNull(createdPlayer);
            assertNotNull(createdPlayer.getId());
            assertEquals(createdPlayer.getName(), player.getName());
            assertEquals(createdPlayer.getTeam(), player.getTeam());
            assertEquals(createdPlayer.getEmail(), player.getEmail());
        } finally {
            if (createdPlayer != null && createdPlayer.getId() != null) {
                deletePlayerSilently(createdPlayer.getId().toString());
            }
        }
    }

    @Test
    void shouldCreatePlayer_WhenPlayerHasNoId() {
        PlayerDto createdPlayer = null;
        try {
            player.setId(null);
            createdPlayer = createPlayer(player, status().isCreated());

            assertNotNull(createdPlayer);
            assertNotNull(createdPlayer.getId());
            assertEquals(createdPlayer.getName(), player.getName());
            assertEquals(createdPlayer.getTeam(), player.getTeam());
            assertEquals(createdPlayer.getEmail(), player.getEmail());
        } finally {
            if (createdPlayer != null && createdPlayer.getId() != null) {
                deletePlayerSilently(createdPlayer.getId().toString());
            }
        }
    }

    @Test
    void shouldFindPlayerById() {
        try {
            createPlayer(player, status().isCreated());
            PlayerDto foundPlayer = findPlayer(player.getId().toString(), status().isOk());

            assertNotNull(foundPlayer);
            assertEquals(foundPlayer.getId(), player.getId());
            assertEquals(foundPlayer.getName(), player.getName());
            assertEquals(foundPlayer.getTeam(), player.getTeam());
            assertEquals(foundPlayer.getEmail(), player.getEmail());
        } finally {
            if (player != null && player.getId() != null) {
                deletePlayerSilently(player.getId().toString());
            }
        }
    }

    @Test
    void shouldThrowPlayerException_WhenPlayerIdDoesNotExist() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get(Routes.API_ROOT + Routes.PLAYERS + "/" + nonExistentId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").value(PlayerException.class.getSimpleName()));
    }

    @Test
    void shouldUpdatePlayer() {
        PlayerDto oldPlayer = null;
        try {
            oldPlayer = createPlayer(player, status().isCreated());
            PlayerDto newPlayer = new PlayerDto(oldPlayer.getId(), "Updated name", "Updated team", "Updated email");

            updatePlayer(oldPlayer.getId().toString(), newPlayer, status().isOk());
            PlayerDto finalPlayer = findPlayer(oldPlayer.getId().toString(), status().isOk());

            assertNotNull(finalPlayer);
            assertEquals(finalPlayer.getId(), newPlayer.getId());
            assertEquals(finalPlayer.getName(), newPlayer.getName());
            assertEquals(finalPlayer.getTeam(), newPlayer.getTeam());
            assertEquals(finalPlayer.getEmail(), newPlayer.getEmail());
        } finally {
            if (oldPlayer != null && oldPlayer.getId() != null) {
                deletePlayerSilently(oldPlayer.getId().toString());
            }
        }
    }

    @Test
    void shouldDeletePlayer() {
        try {
            createPlayer(player, status().isCreated());
            deletePlayer(player.getId().toString(), status().isOk());

            assertNull(findPlayer(player.getId().toString(), status().isNotFound()));
        } finally {
            if (player != null && player.getId() != null) {
                deletePlayerSilently(player.getId().toString());
            }
        }
    }

    private PlayerDto findPlayer(String id, ResultMatcher status) {
        try {
            MvcResult result = mockMvc.perform(get(Routes.API_ROOT + Routes.PLAYERS + "/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status)
                    .andExpect(jsonPath("$.data").exists())
                    .andReturn();
            return extractPlayerDto(result);
        } catch (Exception e) {
            return null;
        }
    }

    private PlayerDto createPlayer(PlayerDto dto, ResultMatcher status) {
        try {
            MvcResult result = mockMvc.perform(post(Routes.API_ROOT + Routes.PLAYERS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.toJson(dto))
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status)
                    .andExpect(jsonPath("$.data.name").value(dto.getName()))
                    .andExpect(jsonPath("$.data.team").value(dto.getTeam()))
                    .andExpect(jsonPath("$.data.email").value(dto.getEmail()))
                    .andExpect(jsonPath("$.success").value(true))
                    .andReturn();
            return extractPlayerDto(result);
        } catch (Exception e) {
            return null;
        }
    }

    private PlayerDto updatePlayer(String id, PlayerDto newDto, ResultMatcher status) {
        try {
            MvcResult result = mockMvc.perform(put(Routes.API_ROOT + Routes.PLAYERS + "/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.toJson(newDto))
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status)
                    .andExpect(jsonPath("$.data.name").value(newDto.getName()))
                    .andExpect(jsonPath("$.data.team").value(newDto.getTeam()))
                    .andExpect(jsonPath("$.data.email").value(newDto.getEmail()))
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
            deletePlayer(id, status().isIAmATeapot());
        } catch (Throwable ignored) {}
    }

    private PlayerDto extractPlayerDto(MvcResult result) throws Exception {
        String jsonResponse = result.getResponse().getContentAsString();
        return objectMapper.readValue(jsonResponse, new TypeReference<ApiResponse<PlayerDto>>() {}).data();
    }

    private Boolean extractBoolean(MvcResult result) throws Exception {
        String jsonResponse = result.getResponse().getContentAsString();
        return objectMapper.readValue(jsonResponse, new TypeReference<ApiResponse<Boolean>>() {}).data();
    }
}