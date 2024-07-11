package ru.stmlabs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Ticket DTO")
public class TicketDTO {
    @Schema(description = "Ticket ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Route ID", example = "1")
    private Long routeId;

    @Schema(description = "Date and Time of the Ticket", example = "2024-07-20T12:00:00")
    private LocalDateTime dateTime;

    @Schema(description = "Seat Number", example = "A1")
    private String seatNumber;

    @Schema(description = "Price of the Ticket", example = "100.00")
    private BigDecimal price;

    @Schema(description = "Purchase Status", example = "false", accessMode = Schema.AccessMode.READ_ONLY)
    private boolean purchased;

    @Schema(description = "User ID", example = "null")
    private Long userId;
}
