package br.com.indra.derek_lisboa.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Derek Lisboa - API da Avaliaçao da Capacitação Minsait")
                        .description("API para gerenciar produtos, categorias, carrinho, pedidos e historico de preços.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name(": Derek Lisboa")
                                .email("derekmlisboa@gmail.com"))
                )
                .servers(List.of(
                        new Server().url("http://localhost:9090").description("Servidor local")
                ));

    }
}