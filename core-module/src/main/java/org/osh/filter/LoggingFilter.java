package org.osh.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.osh.util.HttpUtils.getHeaders;
import static org.springframework.util.ObjectUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final int BODY_MAX_LENGTH = 1000;

    private static final String[] excludeUrlPatterns = {"/actuator*", "/*.ico"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper httpRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper httpResponse = new ContentCachingResponseWrapper(response);

        LocalDateTime requestTime = LocalDateTime.now();
        chain.doFilter(httpRequest, httpResponse);
        LocalDateTime responseTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        LocalDate requestDate = requestTime.toLocalDate();
        String requestTimeStr = requestTime.format(formatter);
        String responseTimeStr = responseTime.format(formatter);
        String durationStr = getDurationStr(requestTime, responseTime);

        String method = httpRequest.getMethod();
        String query = httpRequest.getQueryString();
        String host = httpRequest.getServerName() + ":" + httpRequest.getServerPort();
        String uri = httpRequest.getRequestURI();
        String url = isEmpty(query) ? host + uri : host + uri + "?" + query;

        String protocol = httpRequest.getProtocol();
        HttpHeaders requestHeaders = getHeaders(httpRequest);
        byte[] requestBytes = httpRequest.getContentAsByteArray();
        String requestBody = getBodyStr(requestBytes);

        int requestHeadersSize = getContentSize(requestHeaders);
        String requestHeadersSizeStr = formatSize(requestHeadersSize);
        int requestBodySize = getContentSize(requestBytes);
        String requestBodySizeStr = formatSize(requestBodySize);
        int requestTotalSize = requestHeadersSize + requestBodySize;
        String requestSizeStr = formatSize(requestTotalSize);

        String requestBodyStr = hasText(requestBody) ? String.format("\nRequest Body     : %s (%s)", requestBody, requestBodySizeStr) : "";

        byte[] responseBytes = httpResponse.getContentAsByteArray();
        String responseBody = getBodyStr(responseBytes);
        httpResponse.copyBodyToResponse();
        int status = httpResponse.getStatus();
        String statusName = HttpStatus.valueOf(status).name();

        HttpHeaders responseHeaders = getHeaders(httpResponse);
        List<String> responseContentTypes = responseHeaders.get(HttpHeaders.CONTENT_TYPE);
        if (!isEmpty(responseContentTypes) && responseContentTypes.contains(MediaType.APPLICATION_OCTET_STREAM_VALUE)) {
            responseBytes = null;
        }
        int responseHeadersSize = getContentSize(responseHeaders);
        String responseHeadersSizeStr = formatSize(responseHeadersSize);
        int responseBodySize = getContentSize(responseBytes);
        String responseBodySizeStr = formatSize(responseBodySize);
        int responseTotalSize = responseHeadersSize + responseBodySize;
        String responseSizeStr = formatSize(responseTotalSize);

        String responseBodyStr = hasText(responseBody) ? String.format("\nResponse Body    : %s (%s)", responseBody, responseBodySizeStr) : "";

        log.info("""

                        ====================================================================================================
                        Time             : {} {} ~ {} ({})
                        URL              : {}) {}
                        Request Headers  : {} ({}) {}
                        Status           : {} {} {}
                        Response Headers : {} ({}) {}
                        ===================================================================================================="""
                , requestDate, requestTimeStr, responseTimeStr, durationStr
                , method, url
                , requestHeaders, requestHeadersSizeStr
                , requestBodyStr
                , protocol, status, statusName
                , responseHeaders, responseHeadersSizeStr
                , responseBodyStr
        );
    }

    private String getBodyStr(byte[] content) {
        if (isEmpty(content)) return "";
        String str = new String(content);
        str = str.replace("\n", "").replace("\r", "");
        String ellipsis = "...";
        StringBuilder sb = new StringBuilder();
        return str.length() < BODY_MAX_LENGTH ? str
                : sb.append(str, 0, BODY_MAX_LENGTH - ellipsis.length()).append(ellipsis).toString();
    }

    private String getDurationStr(LocalDateTime requestTime, LocalDateTime responseTime) {
        long millis = Duration.between(requestTime, responseTime).toMillis();
        return millis >= 10000 ? millis / 1000.0 + " s" : millis + " ms";
    }

    private int getContentSize(byte[] content) {
        if (content == null) return 0;
        return content.length;
    }

    private int getContentSize(HttpHeaders headers) {
        return headers.entrySet().stream()
                .mapToInt(entry ->
                        entry.getKey().getBytes().length +
                                entry.getValue().stream().mapToInt(String::length).sum()
                )
                .sum();
    }

    private String formatSize(double size) {
        String unit = "B";
        if (size > 1024 * 1024) {
            size = size / (1024 * 1024);
            unit = "MB";
        } else if (size > 1024) {
            size = size / 1024;
            unit = "KB";
        }
        return String.format("%.2f%s", size, unit);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return PatternMatchUtils.simpleMatch(excludeUrlPatterns, path);
    }
}