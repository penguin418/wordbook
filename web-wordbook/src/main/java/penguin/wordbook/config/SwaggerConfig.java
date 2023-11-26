package penguin.wordbook.config;


import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi swaggerApi() {
        return GroupedOpenApi.builder()
                .group("wordbook-api")
                .packagesToScan("penguin.wordbook.controller")
                .build();
    }
}
