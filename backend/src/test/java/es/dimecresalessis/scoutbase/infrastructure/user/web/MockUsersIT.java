package es.dimecresalessis.scoutbase.infrastructure.user.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.dimecresalessis.scoutbase.application.security.LoginRequest;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDto;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@ActiveProfiles("dev")
@AllArgsConstructor
public class MockUsersIT {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public UserDto createAdmin() throws Exception {
        MvcResult result = mockMvc.perform(get(Routes.API_ROOT + Routes.USERS + Routes.NEW_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("role", "ROLE_ADMIN"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ApiResponse<UserDto> response = objectMapper.readValue(jsonResponse, new TypeReference<ApiResponse<UserDto>>() {});
        return response.data();
    }

    public String login(UserDto user) throws Exception {
        LoginRequest loginRequest = new LoginRequest(user.getUsername(), user.getPassword());
        String jsonBody = objectMapper.writeValueAsString(loginRequest);

        MvcResult result = mockMvc.perform(post(Routes.API_ROOT + Routes.USERS + Routes.AUTH_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ApiResponse<Map<String, String>> response = objectMapper.readValue(jsonResponse, new TypeReference<ApiResponse<Map<String, String>>>() {});
        return response.data().get("token");
    }
}
