package es.dimecresalessis.scoutbase.infrastructure.health.web;

import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HealthControllerTest {

    @InjectMocks
    private HealthController healthController;

    @Test
    @DisplayName("isAlive - Should return true if alive")
    void isAlive_ShouldReturnOk() {
        ResponseEntity<ApiResponse<Boolean>> response = healthController.isAlive();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().data());
    }
}