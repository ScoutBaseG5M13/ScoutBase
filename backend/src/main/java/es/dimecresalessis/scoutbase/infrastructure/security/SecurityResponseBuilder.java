package es.dimecresalessis.scoutbase.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class SecurityResponseBuilder {
    private final ObjectMapper mapper;

    public SecurityResponseBuilder() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void buildResponse(HttpServletRequest request, HttpServletResponse response, int status, String message) throws IOException {
        String sessionId = (String) request.getAttribute("sessionId");

        ApiResponse<Void> apiResponse = new ApiResponse<>(
                false, message, null, sessionId, LocalDateTime.now()
        );
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(apiResponse));
    }
}