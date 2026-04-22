package es.dimecresalessis.scoutbase.infrastructure.health.web;

import es.dimecresalessis.scoutbase.infrastructure.web.annotation.ApiCommonResponses;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import es.dimecresalessis.scoutbase.domain.exception.ScoutbaseException;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static es.dimecresalessis.scoutbase.infrastructure.web.dto.ResponseFactory.handleResponse;

/**
 * Controller for handling application health checks.
 */
@RestController
@AllArgsConstructor
@ApiCommonResponses
@RequestMapping(Routes.API_ROOT + Routes.HEALTH)
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    /**
     * Checks the application status and returns a health report.
     *
     * @return {@link ApiResponse} containing {@code true}, indicating the system is operational.
     * @throws ScoutbaseException If a failure occurs during the health check.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Boolean>> isAlive() {
        logger.info("[PING] Health check requested");
        return handleResponse(true).ok();
    }
}
