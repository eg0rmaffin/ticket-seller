package ru.stmlabs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.stmlabs.dao.TicketDao;
import ru.stmlabs.entity.Ticket;

import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketDao ticketDao;

    @Autowired
    public TicketServiceImpl(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    @Override
    public List<Ticket> findAll(String dateTime, String departurePoint, String destinationPoint, String carrierName, int page, int size) {
        return ticketDao.findAll(dateTime, departurePoint, destinationPoint, carrierName, page, size);
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return ticketDao.findById(id);
    }

    @Override
    public void save(Ticket ticket) {
        ticketDao.save(ticket);
    }

    @Override
    public void update(Ticket ticket) {
        ticketDao.update(ticket);
    }

    @Override
    public void deleteById(Long id) {
        ticketDao.deleteById(id);
    }

    @Override
    public List<Ticket> findPurchasedTicketsForCurrentUser(Long userId) {
        return ticketDao.findPurchasedTicketsForCurrentUser(userId);
    }

    @Override
    public void purchaseTicket(Long ticketId, Long userId) {
        Optional<Ticket> ticketOptional = ticketDao.findById(ticketId);
        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();
            if (ticket.isPurchased()) {
                throw new RuntimeException("Ticket has already been purchased");
            }
            ticket.purchase(userId);
            ticketDao.update(ticket);
        } else {
            throw new RuntimeException("Ticket not found");
        }
    }
}
