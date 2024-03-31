package edu.java.domain.dto.jdbc;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Link {
    private Long id;
    private String url;
    private OffsetDateTime lastUpdate;
    private OffsetDateTime lastCheck;
}
