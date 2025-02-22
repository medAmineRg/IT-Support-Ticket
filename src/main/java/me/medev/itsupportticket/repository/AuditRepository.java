package me.medev.itsupportticket.repository;

import me.medev.itsupportticket.entity.AuditLog;
import me.medev.itsupportticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByTicketOrderByCreatedAtDesc(Ticket ticket);
}
