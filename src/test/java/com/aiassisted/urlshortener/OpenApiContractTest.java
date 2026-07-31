package com.aiassisted.urlshortener;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiContractTest {

    @Test
    void openApiSpecMustParseWithoutErrors() {
        SwaggerParseResult parseResult = new OpenAPIV3Parser().readLocation("openapi/openapi.yaml", null, null);

        assertThat(parseResult.getMessages()).as("OpenAPI parse messages").isNullOrEmpty();
        assertThat(parseResult.getOpenAPI()).isNotNull();
    }

    @Test
    void shortenEndpointContractIncludesRequestAndCoreResponses() {
        OpenAPI openAPI = parseOpenApi();

        Operation shorten = operation(openAPI, "/api/v1/shorten", PathItem.HttpMethod.POST);
        assertThat(shorten.getSummary()).containsIgnoringCase("create");

        RequestBody requestBody = shorten.getRequestBody();
        assertThat(requestBody).isNotNull();
        Schema<?> jsonSchema = requestBody.getContent().get("application/json").getSchema();
        assertThat(jsonSchema).isNotNull();
        assertThat(jsonSchema.getProperties()).containsKeys("longUrl", "customAlias");
        assertThat(jsonSchema.getRequired()).contains("longUrl");

        assertThat(shorten.getResponses()).containsKeys("201", "400", "409", "429");
    }

    @Test
    void publicAndProtectedEndpointResponsesAreDocumented() {
        OpenAPI openAPI = parseOpenApi();

        Operation redirect = operation(openAPI, "/{token}", PathItem.HttpMethod.GET);
        assertResponseDescriptions(redirect.getResponses(), "302", "404", "429");

        Operation metrics = operation(openAPI, "/api/v1/urls/{id}/metrics", PathItem.HttpMethod.GET);
        assertResponseDescriptions(metrics.getResponses(), "200", "401", "404");
        assertThat(metrics.getSecurity()).isNotNull();
        assertThat(metrics.getSecurity()).isNotEmpty();
        assertThat(metrics.getSecurity().get(0).containsKey("ApiKeyAuth")).isTrue();

        Operation delete = operation(openAPI, "/api/v1/urls/{id}", PathItem.HttpMethod.DELETE);
        assertResponseDescriptions(delete.getResponses(), "204", "401", "404");
        assertThat(delete.getSecurity()).isNotNull();
        assertThat(delete.getSecurity()).isNotEmpty();
        assertThat(delete.getSecurity().get(0).containsKey("ApiKeyAuth")).isTrue();
    }

    @Test
    void apiKeySecuritySchemeMustBeDefinedAsHeaderApiKey() {
        OpenAPI openAPI = parseOpenApi();

        assertThat(openAPI.getComponents()).isNotNull();
        assertThat(openAPI.getComponents().getSecuritySchemes()).containsKey("ApiKeyAuth");

        var apiKeyScheme = openAPI.getComponents().getSecuritySchemes().get("ApiKeyAuth");
        assertThat(apiKeyScheme.getType()).isEqualTo(io.swagger.v3.oas.models.security.SecurityScheme.Type.APIKEY);
        assertThat(apiKeyScheme.getIn()).isEqualTo(io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER);
        assertThat(apiKeyScheme.getName()).isEqualTo("X-API-KEY");
    }

    private OpenAPI parseOpenApi() {
        assertThat(Files.exists(Path.of("openapi/openapi.yaml"))).isTrue();

        SwaggerParseResult parseResult = new OpenAPIV3Parser().readLocation("openapi/openapi.yaml", null, null);
        assertThat(parseResult.getMessages()).as("OpenAPI parse messages").isNullOrEmpty();
        return parseResult.getOpenAPI();
    }

    private Operation operation(OpenAPI openAPI, String path, PathItem.HttpMethod method) {
        assertThat(openAPI.getPaths()).containsKey(path);
        PathItem pathItem = openAPI.getPaths().get(path);
        assertThat(pathItem).isNotNull();
        Operation operation = pathItem.readOperationsMap().get(method);
        assertThat(operation).isNotNull();
        return operation;
    }

    private void assertResponseDescriptions(ApiResponses responses, String... statusCodes) {
        assertThat(responses).isNotNull();
        assertThat(responses.keySet()).contains(statusCodes);
        for (String statusCode : statusCodes) {
            assertThat(responses.get(statusCode)).isNotNull();
            assertThat(responses.get(statusCode).getDescription()).isNotBlank();
        }
    }
}
