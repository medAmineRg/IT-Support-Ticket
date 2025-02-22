package me.medev.itsupportticket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TICKETS")
@ToString
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Enumerated(EnumType.STRING)
    private Category category;
    private LocalDateTime creationDate;
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    @ToString.Exclude
    private User createdBy;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<AuditLog> auditLogs = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}