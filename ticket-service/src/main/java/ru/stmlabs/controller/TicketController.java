package ru.stmlabs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stmlabs.dto.TicketDTO;
import ru.stmlabs.entity.Ticket;
import ru.stmlabs.service.TicketService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(summary = "Get all tickets", description = "Get a list of all tickets with optional filters and pagination")
    @GetMapping("/all")
    public ResponseEntity<List<TicketDTO>> getAllTickets(
            @RequestParam(required = false) String dateTime,
            @RequestParam(required = false) String departurePoint,
            @RequestParam(required = false) String destinationPoint,
            @RequestParam(required = false) String carrierName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Ticket> tickets = ticketService.findAll(dateTime, departurePoint, destinationPoint, carrierName, page, size);
        List<TicketDTO> ticketDTOs = tickets.stream().map(this::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(ticketDTOs, HttpStatus.OK);
    }

    @Operation(summary = "Add a new ticket", description = "Add a new ticket to the system",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TicketDTO.class),
                            examples = @ExampleObject(
                                    value = "{ \"routeId\": 1, \"dateTime\": \"2024-07-20T12:00:00\", \"seatNumber\": \"A1\", \"price\": 100.00, \"purchased\": false, \"userId\": null }"
                            )
                    )
            )
    )
    @PostMapping("/add")
    public ResponseEntity<TicketDTO> addTicket(@RequestBody TicketDTO ticketDTO) {
        Ticket ticket = convertToEntity(ticketDTO);
        ticketService.save(ticket);
        return new ResponseEntity<>(convertToDTO(ticket), HttpStatus.CREATED);
    }

    @Operation(summary = "Purchase a ticket", description = "Purchase a ticket by its ID")
    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseTicket(
            @RequestParam Long ticketId,
            @RequestParam Long userId) {
        try {
            ticketService.purchaseTicket(ticketId, userId);
            return new ResponseEntity<>("Ticket purchased successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Ticket purchase failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get tickets for current user", description = "Get all tickets purchased by the current user")
    @GetMapping("/my-tickets/{userId}")
    public ResponseEntity<List<TicketDTO>> getMyTickets(@PathVariable Long userId) {
        List<Ticket> tickets = ticketService.findPurchasedTicketsForCurrentUser(userId);
        List<TicketDTO> ticketDTOs = tickets.stream().map(this::convertToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(ticketDTOs, HttpStatus.OK);
    }

    @Operation(summary = "Get a ticket by ID", description = "Get a ticket by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.findById(id);
        return ticket.map(value -> new ResponseEntity<>(convertToDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private TicketDTO convertToDTO(Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setRouteId(ticket.getRouteId());
        ticketDTO.setDateTime(ticket.getDateTime());
        ticketDTO.setSeatNumber(ticket.getSeatNumber());
        ticketDTO.setPrice(ticket.getPrice());
        ticketDTO.setPurchased(ticket.isPurchased());
        ticketDTO.setUserId(ticket.getUserId());
        return ticketDTO;
    }

    private Ticket convertToEntity(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setId(ticketDTO.getId());
        ticket.setRouteId(ticketDTO.getRouteId());
        ticket.setDateTime(ticketDTO.getDateTime());
        ticket.setSeatNumber(ticketDTO.getSeatNumber());
        ticket.setPrice(ticketDTO.getPrice());
        ticket.setPurchased(ticketDTO.isPurchased());
        ticket.setUserId(ticketDTO.getUserId());
        return ticket;
    }
}
