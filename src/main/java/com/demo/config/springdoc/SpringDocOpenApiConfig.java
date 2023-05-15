package com.demo.config.springdoc;

import com.demo.common.utilities.Constant;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author DigiEx Group
 */
@Configuration
public class SpringDocOpenApiConfig {

    private final String moduleName;
    private final String apiVersion;

    public SpringDocOpenApiConfig(
            @Value("${app.name}") String moduleName,
            @Value("${app.api.version}") String apiVersion) {
        this.moduleName = moduleName;
        this.apiVersion = apiVersion;
    }


    @Bean
    public GroupedOpenApi authenticateApi() {
        return GroupedOpenApi.builder().group("Authenticate").pathsToMatch("/api/auth/**").build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder().group("User").pathsToMatch("/api/user/**").build();
    }
    @Bean
    public GroupedOpenApi classApi() {
        return GroupedOpenApi.builder().group("Class").pathsToMatch("/api/class/**").build();
    }

    @Bean
    public GroupedOpenApi studentApi() {
        return GroupedOpenApi.builder().group("Student").pathsToMatch("/api/student/**").build();
    }
    @Bean
    public OpenAPI applicationOpenAPI() {
        final String securitySchemeName = Constant.HEADER_TOKEN;
        final String apiTitle = String.format("%s API", StringUtils.capitalize(moduleName));
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                    new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                        )
                )
                .info(new Info().title(apiTitle)
                        .version(apiVersion)
                        .license(new License().name("Supercoder").url("https://www.supercoder.co")));
    }
}
