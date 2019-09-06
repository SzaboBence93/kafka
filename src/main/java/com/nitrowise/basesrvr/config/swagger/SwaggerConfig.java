package com.nitrowise.basesrvr.config.swagger;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import springfox.bean.validators.plugins.Validators;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String TOKEN = "TOKEN";
    private final String applicationName = "MyAppName";

    @Bean
    public Docket docket() {
        final Set<String> consumedContentType = new HashSet<>();
        consumedContentType.add("application/json");
        final Set<String> producedContentType = new HashSet<>();
        producedContentType.add("application/json");

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .consumes(consumedContentType)
                .produces(producedContentType)
                .pathMapping("/")
                .useDefaultResponseMessages(false)
                .securitySchemes(Arrays.asList(apiKey()))
                .securityContexts(Arrays.asList(securityContext()))
                .apiInfo(apiInfo());
    }

    private ApiKey apiKey() {
        return new ApiKey(TOKEN, TOKEN, "header");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(String.format("%s REST API", applicationName))
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/api/.*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference(TOKEN, authorizationScopes));
    }

    @Bean
    SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .scopeSeparator(",")
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .docExpansion(DocExpansion.NONE)
                .operationsSorter(OperationsSorter.ALPHA)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .build();
    }

    @Bean
    public SwaggerConfig.PreAuthorizeAnnotationPlugin getPreAuthorizeAnnotationPlugin() {
        return new SwaggerConfig.PreAuthorizeAnnotationPlugin();
    }

    @Bean
    public SwaggerConfig.PageableParameterPlugin getPageableParameterPlugin(TypeResolver typeResolver) {
        return new SwaggerConfig.PageableParameterPlugin(typeResolver);
    }

    /**
     * Spring Pageable 'size' és 'page' paramétereinek hozzáadása az API dokumentációhoz
     */
    @Order(Ordered.LOWEST_PRECEDENCE)
    public static class PageableParameterPlugin implements OperationBuilderPlugin {
        private final ResolvedType pageableType;

        @Autowired
        public PageableParameterPlugin(TypeResolver resolver) {
            this.pageableType = resolver.resolve(Pageable.class);
        }

        @Override
        public boolean supports(DocumentationType delimiter) {
            return true;
        }

        @Override
        public void apply(OperationContext context) {
            List<ResolvedMethodParameter> methodParameters = context.getParameters();
            List<Parameter> parameters = newArrayList();

            for (ResolvedMethodParameter methodParameter : methodParameters) {
                ResolvedType resolvedType = methodParameter.getParameterType();

                if (pageableType.equals(resolvedType)) {
                    ModelReference intModel = new ModelRef(Integer.TYPE.getSimpleName());

                    parameters.add(new ParameterBuilder()
                            .parameterType("query")
                            .name("page")
                            .modelRef(intModel)
                            .description("Page number (0..N)").build());
                    parameters.add(new ParameterBuilder()
                            .parameterType("query")
                            .name("size")
                            .modelRef(intModel)
                            .description("Number of records per page").build());
                    context.operationBuilder().parameters(parameters);
                }
            }
        }
    }

    /**
     * A @PreAuthorize annotációkat keresi meg az Api metódusokon és azokat az API leírás
     * 'notes' részében tünteti fel.
     */
    @Order(Validators.BEAN_VALIDATOR_PLUGIN_ORDER)
    public static class PreAuthorizeAnnotationPlugin implements OperationBuilderPlugin {

        @Override
        public boolean supports(DocumentationType documentationType) {
            return true;
        }

        @Override
        public void apply(OperationContext context) {
            final List<PreAuthorize> authorizes = context.findAllAnnotations(PreAuthorize.class);
            if (authorizes == null || authorizes.isEmpty()) {
                return;
            }
            final String originalNotes = context.operationBuilder().build().getNotes();
            final StringBuilder authDoc = new StringBuilder();
            if (!StringUtils.isBlank(originalNotes)) {
                authDoc.append(originalNotes);
            }
            authDoc.append("<p><b>authorization</b>: ");
            authDoc.append(authorizes.get(0).value());
            authDoc.append("</p>");
            context.operationBuilder().notes(authDoc.toString());
        }
    }
}
