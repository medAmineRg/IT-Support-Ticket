package me.medev.itsupportticket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import me.medev.itsupportticket.dto.CommentRequest;
import me.medev.itsupportticket.dto.TicketRequest;
import me.medev.itsupportticket.dto.TicketResponse;
import me.medev.itsupportticket.entity.Status;
import me.medev.itsupportticket.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "Create a new ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TicketResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })
    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(
            @RequestBody TicketRequest request,
            @Parameter(description = "Username of the user", required = true, example = "employee1 or itsupport1")
            @RequestHeader("X-User") String username) {
        TicketResponse ticket = ticketService.createTicket(request, username);
        return ResponseEntity.ok(ticket);
    }

    @Operation(summary = "Get all tickets for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TicketResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })
    @GetMapping
    public ResponseEntity<List<TicketResponse>> getTickets(
            @Parameter(description = "Username of the user", required = true, example = "employee1 or itsupport1")
            @RequestHeader("X-User") String username) {
        List<TicketResponse> tickets = ticketService.getTickets(username);
        return ResponseEntity.ok(tickets);
    }

    @Operation(summary = "Update the status of a ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TicketResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })
    @PutMapping("/{ticketId}/status")
    public ResponseEntity<TicketResponse> updateTicketStatus(
            @PathVariable Long ticketId,
            @RequestParam Status status,
            @Parameter(description = "Username of the user", required = true, example = "employee1 or itsupport1")
            @RequestHeader("X-User") String username) {
        TicketResponse ticket = ticketService.updateTicketStatus(ticketId, status, username);
        return ResponseEntity.ok(ticket);
    }

    @Operation(summary = "Add a comment to a ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TicketResponse.class)) }),
            @ApiResponse(responseCode = "500",
                    content = @Content) })
    @PostMapping("/{ticketId}/comments")
    public ResponseEntity<TicketResponse> addComment(
            @PathVariable Long ticketId,
            @RequestBody CommentRequest comment,
            @Parameter(description = "Username of the user", required = true, example = "employee1 or itsupport1")
            @RequestHeader("X-User") String username) {
        TicketResponse ticket = ticketService.addComment(ticketId, comment, username);
        return ResponseEntity.ok(ticket);
    }

    @Operation(summary = "Get a ticket by id and status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TicketResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content) })
    @GetMapping("/search")
    public ResponseEntity<List<TicketResponse>> searchTickets(
            @Parameter(description = "Username of the user", required = true, example = "employee1 or itsupport1")
            @RequestHeader("X-User") String username,
            @RequestParam(required = false) Long ticketId,
            @RequestParam(required = false) Status status) {
        List<TicketResponse> tickets = ticketService.searchTickets(username, ticketId, status);
        return ResponseEntity.ok(tickets);
    }
}