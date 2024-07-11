package ru.stmlabs.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_id")
    private Long routeId;

    private LocalDateTime dateTime;
    private String seatNumber;
    private BigDecimal price;
    private boolean purchased;

    @Column(name = "user_id")
    private Long userId;

    public void purchase(Long userId) {
        this.purchased = true;
        this.userId = userId;
    }
}
