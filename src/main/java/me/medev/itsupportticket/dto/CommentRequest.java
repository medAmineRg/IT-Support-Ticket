package me.medev.itsupportticket.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

// CommentRequest.java
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    @NotBlank(message = "Comment is required")
    private String comment;
}
