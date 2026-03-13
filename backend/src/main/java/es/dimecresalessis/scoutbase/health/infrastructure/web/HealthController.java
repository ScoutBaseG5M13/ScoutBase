package es.dimecresalessis.scoutbase.health.infrastructure.web;

import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import es.dimecresalessis.scoutbase.shared.exception.ScoutbaseException;
import es.dimecresalessis.scoutbase.shared.routes.Routes;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static es.dimecresalessis.scoutbase.infrastructure.web.dto.ResponseFactory.handleResponse;

@RestController
@AllArgsConstructor
@RequestMapping(Routes.API_BASE + Routes.HEALTH)
public class HealthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @GetMapping(value = Routes.IS_ALIVE_PATH)
    public ApiResponse<Boolean> isAlive() throws ScoutbaseException {
        logger.info("[PING] Health check requested");
        return handleResponse(true).ok();
    }
}
