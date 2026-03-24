package br.com.indra.derek_lisboa.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
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
                        .title("Derek Lisboa - API da Avaliação da Capacitação Minsait")
                        .description("API para gerenciar produtos, categorias e histórico de preços.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name(": Derek Lisboa")
                                .email("derekmlisboa@gmail.com"))
                )
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor local")
                ))
                .tags(List.of(
                        new Tag().name("Categorias").description("Endpoints relacionados a categorias"),
                        new Tag().name("Produtos").description("Endpoints relacionados a produtos")
                ));
    }
}