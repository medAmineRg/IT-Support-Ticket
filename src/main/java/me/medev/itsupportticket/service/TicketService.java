package me.medev.itsupportticket.service;

import jakarta.transaction.Transactional;
import me.medev.itsupportticket.dto.*;
import me.medev.itsupportticket.entity.*;
import me.medev.itsupportticket.repository.AuditRepository;
import me.medev.itsupportticket.repository.CommentRepository;
import me.medev.itsupportticket.repository.TicketRepository;
import me.medev.itsupportticket.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final AuditRepository auditRepository;
    private final CommentRepository commentRepository;

    public TicketService(TicketRepository ticketRepository,
                         UserRepository userRepository,
                         AuditRepository auditRepository, CommentRepository commentRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.auditRepository = auditRepository;
        this.commentRepository = commentRepository;
    }

    public TicketResponse createTicket(TicketRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());
        ticket.setCategory(request.getCategory());
        ticket.setStatus(Status.NEW);
        ticket.setCreatedBy(user);
        ticket.setCreatedAt(LocalDateTime.now());

        Ticket savedTicket = ticketRepository.save(ticket);
        return convertToResponse(savedTicket);
    }

    public TicketResponse updateTicketStatus(Long ticketId, Status newStatus, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != UserRole.ITSUPPORT) {
            throw new RuntimeException("Only IT Support can update ticket status");
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Status oldStatus = ticket.getStatus();
        ticket.setStatus(newStatus);
        ticket.setUpdatedAt(LocalDateTime.now());

        AuditLog audit = new AuditLog();
        audit.setTicket(ticket);
        audit.setUser(user);
        audit.setAction("STATUS_CHANGE");
        audit.setOldValue(oldStatus.name());
        audit.setNewValue(newStatus.name());
        audit.setCreatedAt(LocalDateTime.now());

        auditRepository.save(audit);
        return convertToResponse(ticketRepository.save(ticket));
    }

    public List<TicketResponse> getTickets(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Ticket> tickets;
        if (user.getRole() == UserRole.ITSUPPORT) {
            tickets = ticketRepository.findAll();
        } else {
            tickets = ticketRepository.findByCreatedBy(user);
        }

        return tickets.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketResponse addComment(Long ticketId, CommentRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != UserRole.ITSUPPORT) {
            throw new RuntimeException("Only IT Support can update ticket status");
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Comment comment = new Comment();
        comment.setTicket(ticket);
        comment.setUser(user);
        comment.setComment(request.getComment());
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);

        AuditLog audit = new AuditLog();
        audit.setTicket(ticket);
        audit.setUser(user);
        audit.setAction("COMMENT");
        audit.setCreatedAt(LocalDateTime.now());

        auditRepository.save(audit);

        ticket.getComments().add(comment);
        ticket.setUpdatedAt(LocalDateTime.now());
        System.out.println("comment = " + comment);
        System.out.println(" ticket.getComments()  = " + ticket.getComments());
        return convertToResponse(ticketRepository.save(ticket));
    }

    private TicketResponse convertToResponse(Ticket ticket) {
        TicketResponse response = new TicketResponse();
        response.setTicketId(ticket.getTicketId());
        response.setTitle(ticket.getTitle());
        response.setDescription(ticket.getDescription());
        response.setPriority(ticket.getPriority());
        response.setCategory(ticket.getCategory());
        response.setStatus(ticket.getStatus());
        response.setCreatedBy(ticket.getCreatedBy().getUsername());
        response.setCreatedAt(ticket.getCreatedAt());
        response.setUpdatedAt(ticket.getUpdatedAt());
        response.setComments(ticket.getComments().stream()
                .map(comment -> {
                    CommentResponse commentResponse = new CommentResponse();
                    commentResponse.setCommentId(comment.getCommentId());
                    commentResponse.setComment(comment.getComment());
                    commentResponse.setUsername(comment.getUser().getUsername());
                    commentResponse.setCreatedAt(comment.getCreatedAt());
                    return commentResponse;
                })
                .collect(Collectors.toList()));
        response.setAuditLogs(ticket.getAuditLogs().stream()
                .map(audit -> {
                    AuditLogResponse auditResponse = new AuditLogResponse();
                    auditResponse.setAuditId(audit.getLogId());
                    auditResponse.setAction(audit.getAction());
                    auditResponse.setOldValue(audit.getOldValue());
                    auditResponse.setNewValue(audit.getNewValue());
                    auditResponse.setUsername(audit.getUser().getUsername());
                    auditResponse.setCreatedAt(audit.getCreatedAt());
                    return auditResponse;
                })
                .collect(Collectors.toList()));
        return response;
    }
}
