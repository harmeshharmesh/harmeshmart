package com.harmesh.harmeshmart.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Filter that attaches a unique request ID to SLF4J MDC for structured traceability.
 * Conforms to Section 18 Observability & Environment Requirements.
 */
@WebFilter(filterName = "LoggingFilter", urlPatterns = "/*")
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String MDC_KEY = "requestId";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestId = httpRequest.getHeader("X-Request-ID");
        if (requestId == null || requestId.trim().isEmpty()) {
            requestId = UUID.randomUUID().toString().substring(0, 8);
        }

        MDC.put(MDC_KEY, requestId);
        httpResponse.setHeader("X-Request-ID", requestId);

        long start = System.currentTimeMillis();
        String uri = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        try {
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            int status = httpResponse.getStatus();
            if (!uri.contains("/static/") && !uri.endsWith(".css") && !uri.endsWith(".js") && !uri.endsWith(".png")) {
                logger.info("{} {} -> {} ({} ms)", method, uri, status, duration);
            }
            MDC.remove(MDC_KEY);
        }
    }

    @Override
    public void destroy() {
    }
}
