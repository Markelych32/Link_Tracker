package edu.java.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record RepositoryResponse(
    @JsonProperty("id") String id,
    @JsonProperty("full_name") String fullName,
    @JsonProperty("pushed_at") OffsetDateTime updatedAt
) {
}
