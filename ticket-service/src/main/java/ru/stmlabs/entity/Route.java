package ru.stmlabs.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "departure_point")
    private String departurePoint;

    @Column(name = "destination_point")
    private String destinationPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrier_id")
    private Carrier carrier;

    @OneToMany(mappedBy = "routeId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;
}
