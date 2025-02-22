package me.medev.itsupportticket.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuditLogResponse {
    private Long auditId;
    private String action;
    private String username;
    private String oldValue;
    private String newValue;
    private LocalDateTime createdAt;
}
