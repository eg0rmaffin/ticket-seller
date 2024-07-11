package ru.stmlabs.service;

import ru.stmlabs.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    Optional<Ticket> findById(Long id);
    List<Ticket> findAll(String dateTime, String departurePoint, String destinationPoint, String carrierName, int page, int size);
    void save(Ticket ticket);
    void update(Ticket ticket);
    void deleteById(Long id);
    List<Ticket> findPurchasedTicketsForCurrentUser(Long userId);
    void purchaseTicket(Long ticketId, Long userId);
}
