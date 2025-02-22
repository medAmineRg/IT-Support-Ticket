package me.medev.itsupportticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.medev.itsupportticket.dto.CommentRequest;
import me.medev.itsupportticket.dto.TicketRequest;
import me.medev.itsupportticket.dto.TicketResponse;
import me.medev.itsupportticket.entity.Category;
import me.medev.itsupportticket.entity.Priority;
import me.medev.itsupportticket.entity.Status;
import me.medev.itsupportticket.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TicketControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TicketController ticketController;

    @Mock
    private TicketService ticketService;

    private final String TEST_USERNAME = "itsupport1";

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
    }

    @Test
    public void testCreateTicket() throws Exception {
        TicketRequest request = new TicketRequest(
                "Network Issue",
                "Cannot connect to internet",
                Priority.HIGH,
                Category.NETWORK);

        TicketResponse response = new TicketResponse(
                1L,
                "Network Issue",
                "Cannot connect to internet",
                Priority.HIGH,
                Category.NETWORK,
                Status.NEW,
                TEST_USERNAME,
                LocalDateTime.now(),
                LocalDateTime.now(),
                Collections.emptyList(),
                Collections.emptyList());

        when(ticketService.createTicket(any(TicketRequest.class), eq(TEST_USERNAME)))
                .thenReturn(response);

        mockMvc.perform(post("/api/tickets")
                .header("X-User", TEST_USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value(1L))
                .andExpect(jsonPath("$.title").value("Network Issue"))
                .andDo(print());

        verify(ticketService, times(1)).createTicket(any(TicketRequest.class), eq(TEST_USERNAME));
    }

    @Test
    public void testGetTickets() throws Exception {
        TicketResponse ticket = new TicketResponse(
                1L,
                "Network Issue",
                "Cannot connect to internet",
                Priority.HIGH,
                Category.NETWORK,
                Status.NEW,
                TEST_USERNAME,
                LocalDateTime.now(),
                LocalDateTime.now(),
                Collections.emptyList(),
                Collections.emptyList());

        when(ticketService.getTickets(TEST_USERNAME))
                .thenReturn(Arrays.asList(ticket));

        mockMvc.perform(get("/api/tickets")
                .header("X-User", TEST_USERNAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ticketId").value(1L))
                .andDo(print());

        verify(ticketService, times(1)).getTickets(TEST_USERNAME);
    }

    @Test
    public void testUpdateTicketStatus() throws Exception {
        TicketResponse updatedTicket = new TicketResponse(
                1L,
                "Network Issue",
                "Cannot connect to internet",
                Priority.HIGH,
                Category.NETWORK,
                Status.IN_PROGRESS,
                TEST_USERNAME,
                LocalDateTime.now(),
                LocalDateTime.now(),
                Collections.emptyList(),
                Collections.emptyList());

        when(ticketService.updateTicketStatus(eq(1L), eq(Status.IN_PROGRESS), eq(TEST_USERNAME)))
                .thenReturn(updatedTicket);

        mockMvc.perform(put("/api/tickets/1/status")
                .header("X-User", TEST_USERNAME)
                .param("status", "IN_PROGRESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andDo(print());

        verify(ticketService, times(1)).updateTicketStatus(1L, Status.IN_PROGRESS, TEST_USERNAME);
    }

    @Test
    public void testAddComment() throws Exception {
        CommentRequest commentRequest = new CommentRequest("New comment");
        TicketResponse updatedTicket = new TicketResponse(
                1L,
                "Network Issue",
                "Cannot connect to internet",
                Priority.HIGH,
                Category.NETWORK,
                Status.IN_PROGRESS,
                TEST_USERNAME,
                LocalDateTime.now(),
                LocalDateTime.now(),
                Collections.emptyList(),
                Collections.emptyList());

        when(ticketService.addComment(eq(1L), any(CommentRequest.class), eq(TEST_USERNAME)))
                .thenReturn(updatedTicket);

        mockMvc.perform(post("/api/tickets/1/comments")
                .header("X-User", TEST_USERNAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(commentRequest)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(ticketService, times(1)).addComment(eq(1L), any(CommentRequest.class), eq(TEST_USERNAME));
    }

    @Test
    public void testSearchTickets() throws Exception {
        TicketResponse ticket = new TicketResponse(
                1L,
                "Network Issue",
                "Cannot connect to internet",
                Priority.HIGH,
                Category.NETWORK,
                Status.NEW,
                TEST_USERNAME,
                LocalDateTime.now(),
                LocalDateTime.now(),
                Collections.emptyList(),
                Collections.emptyList());

        when(ticketService.searchTickets(eq(TEST_USERNAME), eq(1L), eq(Status.NEW)))
                .thenReturn(Arrays.asList(ticket));

        mockMvc.perform(get("/api/tickets/search")
                .header("X-User", TEST_USERNAME)
                .param("ticketId", "1")
                .param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ticketId").value(1L))
                .andExpect(jsonPath("$[0].status").value("NEW"))
                .andDo(print());

        verify(ticketService, times(1)).searchTickets(TEST_USERNAME, 1L, Status.NEW);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
