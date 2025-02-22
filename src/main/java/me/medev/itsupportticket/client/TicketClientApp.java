package me.medev.itsupportticket.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.medev.itsupportticket.dto.*;
import me.medev.itsupportticket.entity.Status;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class TicketClientApp extends JFrame {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private final String BASE_URL = "http://localhost:8081/api/tickets";

    private JTextField usernameField;
    private JTextArea resultArea;
    private JTextField ticketIdField;
    private JTextArea descriptionArea;
    private JComboBox<Status> statusComboBox;

    public TicketClientApp() {
        setTitle("IT Support Ticket Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setLayout(new MigLayout("fill", "[grow]", "[][grow]")); // adjusted vertical constraints

        JLabel header = new JLabel("IT Support Ticket Client");
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.setHorizontalAlignment(SwingConstants.CENTER);
        add(header, "span, wrap, gapbottom 10");

        JPanel userPanel = new JPanel(new MigLayout("", "[][grow][]", "[]"));
        userPanel.setBorder(BorderFactory.createTitledBorder("User Information"));
        usernameField = new JTextField(20);
        userPanel.add(new JLabel("Username:"), "gapright 5");
        userPanel.add(usernameField, "grow");
        add(userPanel, "growx, wrap, gapbottom 10");

        JPanel actionPanel = new JPanel(new MigLayout("", "[grow][grow][grow]", "[][]"));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        ticketIdField = new JTextField(10);
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        statusComboBox = new JComboBox<>(Status.values());

        actionPanel.add(new JLabel("Ticket ID:"), "split 2, gapright 5");
        actionPanel.add(ticketIdField, "growx");
        actionPanel.add(new JLabel("Status:"), "split 2, gapleft 10, gapright 5");
        actionPanel.add(statusComboBox, "growx, wrap, gapbottom 5");

        actionPanel.add(new JLabel("Description:"), "top, gapright 5");
        actionPanel.add(new JScrollPane(descriptionArea), "span 2, grow, wrap");

        JButton createButton = new JButton("Create Ticket");
        JButton getAllButton = new JButton("Get All Tickets");
        JButton searchButton = new JButton("Search Tickets");
        JButton updateStatusButton = new JButton("Update Status");
        JButton addCommentButton = new JButton("Add Comment");
        actionPanel.add(createButton, "split 5, gapright 5");
        actionPanel.add(getAllButton);
        actionPanel.add(searchButton);
        actionPanel.add(updateStatusButton);
        actionPanel.add(addCommentButton);

        add(actionPanel, "grow, wrap, gapbottom 10");

        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), "grow");

        createButton.addActionListener(e -> createTicket());
        getAllButton.addActionListener(e -> getAllTickets());
        searchButton.addActionListener(e -> searchTickets());
        updateStatusButton.addActionListener(e -> updateStatus());
        addCommentButton.addActionListener(e -> addComment());
    }

    private void createTicket() {
        try {
            TicketRequest request = new TicketRequest();
            request.setDescription(descriptionArea.getText());

            String json = objectMapper.writeValueAsString(request);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .header("X-User", usernameField.getText())
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            sendRequest(httpRequest);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void getAllTickets() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("X-User", usernameField.getText())
                    .GET()
                    .build();

            sendRequest(request);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void searchTickets() {
        try {
            String ticketId = ticketIdField.getText();
            Status status = (Status) statusComboBox.getSelectedItem();
            String query = String.format("?ticketId=%s&status=%s", ticketId, status);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/search" + query))
                    .header("X-User", usernameField.getText())
                    .GET()
                    .build();

            sendRequest(request);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void updateStatus() {
        try {
            String ticketId = ticketIdField.getText();
            Status status = (Status) statusComboBox.getSelectedItem();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format("%s/%s/status?status=%s", BASE_URL, ticketId, status)))
                    .header("X-User", usernameField.getText())
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            sendRequest(request);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void addComment() {
        try {
            String ticketId = ticketIdField.getText();
            CommentRequest comment = new CommentRequest();
            comment.setComment(descriptionArea.getText());

            String json = objectMapper.writeValueAsString(comment);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format("%s/%s/comments", BASE_URL, ticketId)))
                    .header("Content-Type", "application/json")
                    .header("X-User", usernameField.getText())
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            sendRequest(request);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void sendRequest(HttpRequest request) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            if (responseBody == null || responseBody.isBlank()) {
                resultArea.setText("No data available");
                return;
            }

            try {
                List<TicketResponse> tickets;
                if (responseBody.trim().startsWith("[")) {
                    tickets = objectMapper.readValue(
                            responseBody,
                            objectMapper.getTypeFactory().constructCollectionType(List.class, TicketResponse.class));
                } else {
                    TicketResponse singleTicket = objectMapper.readValue(responseBody, TicketResponse.class);
                    tickets = List.of(singleTicket);
                }

                if (tickets.isEmpty()) {
                    resultArea.setText("No tickets found");
                    return;
                }

                StringBuilder table = new StringBuilder();

                table.append("TICKET DETAILS\n");
                table.append(String.format("%-4s | %-15s | %-12s | %-8s | %-8s | %-12s | %-10s\n",
                        "ID", "Title", "Status", "Priority", "Category", "Created By", "Created At"));
                table.append("-".repeat(85)).append("\n");

                for (TicketResponse ticket : tickets) {
                    table.append(String.format("%-4d | %-15s | %-12s | %-8s | %-8s | %-12s | %s\n",
                            ticket.getTicketId(),
                            limitString(ticket.getTitle(), 15),
                            ticket.getStatus(),
                            ticket.getPriority(),
                            ticket.getCategory(),
                            ticket.getCreatedBy(),
                            ticket.getCreatedAt().toString().split("T")[0]));

                    if (ticket.getComments() != null && !ticket.getComments().isEmpty()) {
                        table.append("\nCOMMENTS\n");
                        table.append(String.format("%-4s | %-25s | %-12s | %-10s\n",
                                "ID", "Content", "User", "Date"));
                        table.append("-".repeat(60)).append("\n");

                        for (CommentResponse comment : ticket.getComments()) {
                            table.append(String.format("%-4d | %-25s | %-12s | %s\n",
                                    comment.getCommentId(),
                                    limitString(comment.getComment(), 25),
                                    comment.getUsername(),
                                    comment.getCreatedAt().toString().split("T")[0]));
                        }
                    }

                    if (ticket.getAuditLogs() != null && !ticket.getAuditLogs().isEmpty()) {
                        table.append("\nAUDIT LOGS\n");
                        table.append(String.format("%-4s | %-12s | %-12s | %-12s | %-12s | %-10s\n",
                                "ID", "Action", "User", "Old", "New", "Date"));
                        table.append("-".repeat(75)).append("\n");

                        for (AuditLogResponse log : ticket.getAuditLogs()) {
                            table.append(String.format("%-4d | %-12s | %-12s | %-12s | %-12s | %s\n",
                                    log.getAuditId(),
                                    limitString(log.getAction(), 12),
                                    log.getUsername(),
                                    limitString(log.getOldValue(), 12),
                                    limitString(log.getNewValue(), 12),
                                    log.getCreatedAt().toString().split("T")[0]));
                        }
                    }

                    table.append("\n").append("=".repeat(85)).append("\n");
                }

                resultArea.setText(table.toString());

            } catch (Exception e) {
                e.printStackTrace();
                String formattedJson = objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(objectMapper.readTree(responseBody));
                resultArea.setText("Error parsing response: " + e.getMessage() +
                        "\n\nRaw JSON Response:\n" + formattedJson);
            }
        } catch (Exception e) {
            showError(e);
        }
    }

    private String limitString(String input, int maxLength) {
        if (input == null)
            return "N/A";
        return input.length() > maxLength ? input.substring(0, maxLength - 3) + "..." : input;
    }

    private void showError(Exception e) {
        resultArea.setText("Error: " + e.getMessage());
        e.printStackTrace();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new TicketClientApp().setVisible(true);
        });
    }
}
