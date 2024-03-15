package edu.java.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Link {
    private Long id;
    private String url;
    private OffsetDateTime lastUpdate;
    private OffsetDateTime lastCheck;
}
