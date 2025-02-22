package me.medev.itsupportticket.repository;

import me.medev.itsupportticket.entity.Status;
import me.medev.itsupportticket.entity.Ticket;
import me.medev.itsupportticket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByCreatedBy(User user);
    List<Ticket> findByStatus(Status status);
}
