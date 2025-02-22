package me.medev.itsupportticket.repository;

import me.medev.itsupportticket.entity.Comment;
import me.medev.itsupportticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTicketOrderByCreatedAtDesc(Ticket ticket);
}