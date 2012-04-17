package com.qualimente.reliability.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * OutstandingRequestLimitingFilter is responsible for limiting the number of outstanding, in-progress requests
 * to a configured servlet resource.
 */
public class OutstandingRequestLimitingFilter implements Filter {
    protected static final String MAX_OUTSTANDING_REQUESTS_PARAM = "maxOutstandingRequests";
    protected static final int MAX_OUTSTANDING_REQUESTS_DEFAULT = 100;

    private static final Logger logger = LoggerFactory.getLogger(OutstandingRequestLimitingFilter.class);

    private int maxOutstandingRequests = MAX_OUTSTANDING_REQUESTS_DEFAULT;
    private final AtomicInteger numOutstandingRequests = new AtomicInteger(0);

    public void init(FilterConfig filterConfig) throws ServletException {
        final String maxConcurrentRequestsStr = filterConfig.getInitParameter(MAX_OUTSTANDING_REQUESTS_PARAM);
        if (maxConcurrentRequestsStr != null) {
            setMaxOutstandingRequests(Integer.parseInt(maxConcurrentRequestsStr));
        }
        logger.info("initialized with maxOutstandingRequests: " + getMaxOutstandingRequests());
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        try {
            final int currentRequests = numOutstandingRequests.incrementAndGet();
            final int maxRequests = getMaxOutstandingRequests();
            if (currentRequests <= maxRequests) {
                logOutstandingRequestsWithinLimits(currentRequests, maxRequests);
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                logSkippingFilterChainDoFilter(maxRequests);
                if (servletResponse instanceof HttpServletResponse) {
                    HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
                    httpServletResponse.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                }
            }
        } finally {
            numOutstandingRequests.decrementAndGet();
        }

    }

    private void logSkippingFilterChainDoFilter(int maxRequests) {
        logger.warn("maxOutstandingRequests (" + maxRequests + ") has been met; "
                + "skipping call to doFilter");
    }

    private void logOutstandingRequestsWithinLimits(int currentRequests, int maxRequests) {
        if (logger.isTraceEnabled()) {
            logger.trace("outstanding requests within limits ("
                    + currentRequests + "/" + maxRequests + "); "
                    + "capacity used: " + (currentRequests / maxRequests) + "%; "
                    + "proceeding with doFilter");
        }
    }

    public void destroy() {
        logger.info("destroyed filter with numOutstandingRequests: " + getNumOutstandingRequests());
    }

    protected void setMaxOutstandingRequests(int maxOutstandingRequests) {
        this.maxOutstandingRequests = maxOutstandingRequests;
    }

    public int getMaxOutstandingRequests() {
        return maxOutstandingRequests;
    }

    public int getNumOutstandingRequests() {
        return numOutstandingRequests.get();
    }
}
