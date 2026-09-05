package com.alisondev.live_storage_hub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(new Components()
            .addSecuritySchemes("bearerAuth", new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"))
            .addSchemas("ApiErrorResponse", new Schema<>()
                .description("Standard response returned whenever an API request fails.")
                .addProperty("code", new Schema<>().type("string").example("2.1.1"))
                .addProperty("message", new Schema<>().type("string").example("Invalid admin key."))
                .addProperty("data", new Schema<>().nullable(true))))
        .info(new Info()
            .title("Live Storage Hub API")
            .description("Centralized API for multiples apps.")
            .version("1.0"));
  }

  @Bean
  public OpenApiCustomizer internalErrorResponse() {
    return openApi -> openApi.getPaths().forEach((path, pathItem) ->
        pathItem.readOperationsMap().forEach((method, operation) -> {
          String successMessage = successMessage(method.name(), path);
          boolean downloadRoute = "GET".equals(method.name()) && path.endsWith("/download");

          if (!operation.getResponses().containsKey("200")) {
            ApiResponse successResponse = new ApiResponse().description(successMessage);
            if (!downloadRoute) {
              successResponse.content(new Content().addMediaType("application/json", new MediaType()
                  .example(successExample(successMessage))));
            }
            operation.getResponses().addApiResponse("200", successResponse);
          }

          ApiResponse successResponse = operation.getResponses().get("200");
          successResponse.setDescription(successMessage);
          if (!downloadRoute && successResponse.getContent() != null) {
            successResponse.getContent().values()
                .forEach(mediaType -> mediaType.setExample(successExample(successMessage)));
          }

          if (!operation.getResponses().containsKey("500")) {
            operation.getResponses().addApiResponse("500", new ApiResponse()
                .description("Unexpected internal error (0.0.0).")
                .content(new Content().addMediaType("application/json", new MediaType()
                    .schema(new Schema<>().$ref("#/components/schemas/ApiErrorResponse"))
                    .example(internalErrorExample()))));
          }
        }));
  }

  private String successMessage(String method, String path) {
    return switch (method + " " + path) {
      case "POST /apps" -> "App registered successfully.";
      case "GET /apps" -> "Apps listed successfully.";
      case "POST /users" -> "User registered successfully.";
      case "POST /auth" -> "User authenticated successfully.";
      case "POST /users/data" -> "User data registered successfully.";
      case "GET /users/data" -> "User data listed successfully.";
      case "POST /users/files" -> "File uploaded successfully.";
      case "GET /users/files" -> "Files listed successfully.";
      case "GET /users/files/{fileId}/download" -> "File downloaded successfully.";
      case "DELETE /users/files/{fileId}" -> "File deleted successfully.";
      default -> "Operation completed successfully.";
    };
  }

  private Map<String, Object> successExample(String message) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("code", "0");
    response.put("message", message);
    response.put("data", Map.of());
    return response;
  }

  private Map<String, Object> internalErrorExample() {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("code", "0.0.0");
    response.put("message", "Unexpected internal error.");
    response.put("data", null);
    return response;
  }

}
