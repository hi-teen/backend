package backend.hiteen.externalapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient neisWebClient(@Value("${OPEN_API_URL}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(ExchangeStrategies.builder()
                                            .codecs(configurer -> configurer
                                                    .defaultCodecs()
                                                    .maxInMemorySize(1024 * 1024))
                                            .build())
                .build();
    }
}
