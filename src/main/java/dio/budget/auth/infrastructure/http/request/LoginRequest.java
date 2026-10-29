package dio.budget.auth.infrastructure.http.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(example = "string", requiredMode = Schema.RequiredMode.REQUIRED)
        String username,
        @Schema(example = "string", format = "password", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {}
