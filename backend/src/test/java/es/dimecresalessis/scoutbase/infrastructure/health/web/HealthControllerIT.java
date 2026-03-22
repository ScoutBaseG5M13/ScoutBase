package es.dimecresalessis.scoutbase.infrastructure.health.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@ActiveProfiles("dev")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class HealthControllerIT {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    void shouldReturnHealthStatus_WhenSystemIsAlive() throws Exception {
        MvcResult result = mockMvc.perform(get(Routes.API_ROOT + Routes.HEALTH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.data").value(true))
                .andExpect(jsonPath("$.sessionId").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        Boolean isAlive = extractBoolean(result);
        assertNotNull(isAlive);
        assertTrue(isAlive);
    }

    private Boolean extractBoolean(MvcResult result) throws Exception {
        String jsonResponse = result.getResponse().getContentAsString();
        ApiResponse<Boolean> response = objectMapper.readValue(
                jsonResponse,
                new TypeReference<ApiResponse<Boolean>>() {}
        );
        return response.data();
    }
}