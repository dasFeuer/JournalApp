package app.barun.journalApp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomConfig() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Journal App APIs")
                                .description("By Barun")
                                .version("1.0")
                )
                .servers(Arrays.asList(
                        new Server()
                                .url("http://localhost:8080/journal")
                                .description("Local environment")
                ))
                .tags(Arrays.asList(
                        new Tag().name("Public APIs").description("APIs available to all users"),
                        new Tag().name("User APIs").description("APIs for user management"),
                        new Tag().name("Journal APIs").description("APIs for managing journals"),
                        new Tag().name("Admin APIs").description("APIs for administrative tasks")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                ));
    }
}
