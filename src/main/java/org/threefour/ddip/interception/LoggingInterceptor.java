package org.threefour.ddip.interception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.threefour.ddip.util.PerformanceMeasurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoggingInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);
    private static final String REQUEST_RECEPTION = "Received request for '{}'";
    private static final String REQUEST_COMPLETION
            = "Completed request for '{}', status code: {}, estimated time: {}ms, used memory: {} bytes";
    private static final String BEFORE_MEMORY = "beforeMemory";
    private static final String STARTED_AT = "startedAt";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info(REQUEST_RECEPTION, request.getRequestURI());

        long beforeMemory = PerformanceMeasurer.computeUsedMemory(0);
        long startedAt = PerformanceMeasurer.computeElapsedTime(0);

        request.setAttribute(BEFORE_MEMORY, beforeMemory);
        request.setAttribute(STARTED_AT, startedAt);

        return true;
    }

    @Override
    public void afterCompletion
            (HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        long beforeMemory = (long) request.getAttribute(BEFORE_MEMORY);
        long startedAt = (long) request.getAttribute(STARTED_AT);
        long elapsedTime = PerformanceMeasurer.computeElapsedTime(startedAt);
        long memoryUsage = PerformanceMeasurer.computeUsedMemory(beforeMemory);

        log.info(REQUEST_COMPLETION, request.getRequestURI(), response.getStatus(), elapsedTime, memoryUsage);
    }
}