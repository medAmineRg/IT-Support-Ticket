package me.medev.itsupportticket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import me.medev.itsupportticket.entity.Category;
import me.medev.itsupportticket.entity.Priority;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotNull(message = "Category is required")
    private Category category;
}