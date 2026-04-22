package es.dimecresalessis.scoutbase.infrastructure.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestInterceptorTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Object handler;

    @InjectMocks
    private RequestInterceptor requestInterceptor;

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    @DisplayName("preHandle - Should use existing X-Session-ID from header")
    void preHandle_WithExistingHeader() {
        String existingId = "session-123";
        when(request.getHeader("X-Session-ID")).thenReturn(existingId);

        boolean result = requestInterceptor.preHandle(request, response, handler);

        assertTrue(result);
        assertEquals(existingId, MDC.get("sessionId"));
    }

    @Test
    @DisplayName("preHandle - Should generate new UUID if header is missing")
    void preHandle_WithoutHeader() {
        when(request.getHeader("X-Session-ID")).thenReturn(null);

        boolean result = requestInterceptor.preHandle(request, response, handler);

        assertTrue(result);
        assertNotNull(MDC.get("sessionId"));
        assertTrue(MDC.get("sessionId").contains("-"));
    }

    @Test
    @DisplayName("afterCompletion - Should clear MDC to avoid thread pollution")
    void afterCompletion_ShouldClearMDC() {
        MDC.put("sessionId", "mySessionId!");

        requestInterceptor.afterCompletion(request, response, handler, null);

        assertNull(MDC.get("sessionId"));
    }
}