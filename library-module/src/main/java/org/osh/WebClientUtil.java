package org.osh;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebClientUtil {

    private static final WebClient webClient = ApplicationContextProvider.getBean(WebClient.class);

    public static String get() {
        return webClient.get()
                .uri("http://localhost:8082/hello")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
