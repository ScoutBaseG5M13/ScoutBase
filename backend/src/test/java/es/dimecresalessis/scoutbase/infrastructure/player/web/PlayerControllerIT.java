package es.dimecresalessis.scoutbase.infrastructure.player.web;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDto;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.infrastructure.shared.utils.JsonUtils;
import es.dimecresalessis.scoutbase.infrastructure.user.web.MockUsersIT;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private UserDto admin;
    private String jwtToken;

    @BeforeAll
    void setUp() throws Exception {
        this.admin = mockUsers.createAdmin();
        this.jwtToken = mockUsers.login(admin);
    }

    @Test
    void shouldCreatePlayerAndReturn200() throws Exception {
        PlayerDto dto = new PlayerDto(null, "Leo Messi", "Barcelona FC", "leo@messi.ar");

        mockMvc.perform(post(Routes.API_ROOT + Routes.PLAYERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.toJson(dto))
                        .header("Authorization", "Bearer " + this.jwtToken))
                .andExpect(status().isCreated()) // Code 200?
                .andExpect(jsonPath("$.name").value("Leo Messi"));
    }

    @Test
    void shouldGetPlayerById() throws Exception {
        // Asumiendo que el ID 1 existe o usas una DB de test (H2)
        mockMvc.perform(get("/api/players/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }
}