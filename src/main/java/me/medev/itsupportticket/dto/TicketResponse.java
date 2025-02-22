package me.medev.itsupportticket.dto;

import lombok.*;
import me.medev.itsupportticket.entity.Category;
import me.medev.itsupportticket.entity.Priority;
import me.medev.itsupportticket.entity.Status;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Long ticketId;
    private String title;
    private String description;
    private Priority priority;
    private Category category;
    private Status status;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentResponse> comments;
    private List<AuditLogResponse> auditLogs;
}
