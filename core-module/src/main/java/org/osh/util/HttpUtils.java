package org.osh.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HttpUtils {

    private static String hostName;
    private static String hostIp;

    /**
     * 빈 초기화시 호스트 정보 등록
     * InetAddress 호출 시간을 줄이기 위한 캐싱 목적
     */
    public HttpUtils() {
        try {
            hostName = InetAddress.getLocalHost().getHostName();
            hostIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostName = "UNKNOWN_HOST";
            hostIp = "UNKNOWN_HOST";
        }
    }

    public static String getHostName() {
        return hostName;
    }

    public static String getHostIp() {
        return hostIp;
    }

    public static String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if(ip == null) ip = request.getHeader("Proxy-Client-IP");
        if(ip == null) ip = request.getHeader("NS-CLIENT-IP");
        if(ip == null) ip = request.getHeader("HTTP_CLIENT_IP");
        if(ip == null) ip = request.getRemoteAddr();

        return ip;
    }

    /**
     * 헤더에서 Accept Languages 목록을 받아 첫번째 언어를 String으로 반환한다.
     * @return 2자리 언어코드 ex)'en', 'ko' (ISO 639)
     */
    public static String getLanguage(HttpHeaders headers) {
        List<Locale> locales = headers.getAcceptLanguageAsLocales();
        return locales.stream().findFirst().orElse(Locale.ENGLISH).getLanguage();
    }

    public static String getLanguage(HttpServletRequest request) {
        return getLanguage(getHeaders(request));
    }

    public static HttpHeaders getHeaders(HttpServletRequest request) {
        return Collections
                .list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        header -> Collections.list(request.getHeaders(header)),
                        (oldVal, newVal) -> newVal,
                        HttpHeaders::new
                ));
    }

    public static HttpHeaders getHeaders(HttpServletResponse response) {
        return response.getHeaderNames()
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        header -> Collections.list(Collections.enumeration(response.getHeaders(header))),
                        (oldVal, newVal) -> newVal,
                        HttpHeaders::new
                ));
    }
}
