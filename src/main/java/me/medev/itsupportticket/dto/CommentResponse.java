package me.medev.itsupportticket.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long commentId;
    private String comment;
    private String username;
    private LocalDateTime createdAt;
}


