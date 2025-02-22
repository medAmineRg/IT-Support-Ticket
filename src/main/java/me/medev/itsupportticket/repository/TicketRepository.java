package me.medev.itsupportticket.repository;

import me.medev.itsupportticket.entity.Status;
import me.medev.itsupportticket.entity.Ticket;
import me.medev.itsupportticket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCreatedBy(User user);

    @Query("SELECT t FROM Ticket t WHERE " +
            "(:ticketId IS NULL OR t.ticketId = :ticketId) AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:userId IS NULL OR t.createdBy.userId = :userId)")
    List<Ticket> searchTickets(
            @Param("ticketId") Long ticketId,
            @Param("status") Status status,
            @Param("userId") Long userId
    );}
