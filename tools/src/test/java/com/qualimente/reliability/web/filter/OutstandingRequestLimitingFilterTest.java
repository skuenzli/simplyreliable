package com.qualimente.reliability.web.filter;

import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class OutstandingRequestLimitingFilterTest {

    private static final Random random = new Random(System.currentTimeMillis());

    @Test
    public void test_defaults_are_used_when_no_config_is_applied() throws Exception {
        OutstandingRequestLimitingFilter filter = new OutstandingRequestLimitingFilter();

        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter(OutstandingRequestLimitingFilter.MAX_OUTSTANDING_REQUESTS_PARAM))
                .thenReturn(null);

        filter.init(createFilterConfig(null));

        assertEquals(OutstandingRequestLimitingFilter.MAX_OUTSTANDING_REQUESTS_DEFAULT, filter.getMaxOutstandingRequests());
    }

    @Test
    public void test_custom_filter_config_is_applied() throws Exception {
        OutstandingRequestLimitingFilter filter = new OutstandingRequestLimitingFilter();

        int expectedMaxConcurrentRequests = anyInt();

        filter.init(createFilterConfig(String.valueOf(expectedMaxConcurrentRequests)));

        assertEquals(expectedMaxConcurrentRequests, filter.getMaxOutstandingRequests());
    }

    private FilterConfig createFilterConfig(String maxConcurrentRequests) {
        FilterConfig config = mock(FilterConfig.class);
        when(config.getInitParameter(OutstandingRequestLimitingFilter.MAX_OUTSTANDING_REQUESTS_PARAM))
                .thenReturn(maxConcurrentRequests);
        return config;
    }

    @Test
    public void test_filter_proceeds_calls_doFilter_when_below_outstanding_request_limit() throws Exception {
        OutstandingRequestLimitingFilter filter = new OutstandingRequestLimitingFilter();

        filter.init(createFilterConfig("1"));

        FilterChain filterChain = mock(FilterChain.class);

        assertEquals("number of outstanding requests should start at zero",
                0, filter.getNumOutstandingRequests());
        filter.doFilter(null, null, filterChain);
        assertEquals("number of outstanding requests should end at zero",
                0, filter.getNumOutstandingRequests());

        verify(filterChain).doFilter(null, null);
    }
    @Test
    public void test_filter_proceeds_calls_doFilter_when_at_or_above_outstanding_request_limit() throws Exception {
        OutstandingRequestLimitingFilter filter = new OutstandingRequestLimitingFilter();

        filter.init(createFilterConfig("0"));

        FilterChain filterChain = mock(FilterChain.class);

        assertEquals(0, filter.getNumOutstandingRequests());
        final HttpServletResponse servletResponse = mock(HttpServletResponse.class);

        filter.doFilter(null, servletResponse, filterChain);

        verifyZeroInteractions(filterChain);
        verify(servletResponse).setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    }

    private static int anyInt() {
        return random.nextInt();
    }

}
