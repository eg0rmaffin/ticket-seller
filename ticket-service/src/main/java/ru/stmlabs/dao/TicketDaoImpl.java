package ru.stmlabs.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.stmlabs.dao.TicketDao;
import ru.stmlabs.entity.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class TicketDaoImpl implements TicketDao {

    private final JdbcTemplate jdbcTemplate;

    public TicketDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Ticket> ticketRowMapper = new RowMapper<>() {
        @Override
        public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
            Ticket ticket = new Ticket();
            ticket.setId(rs.getLong("id"));
            ticket.setRouteId(rs.getLong("route_id"));
            ticket.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
            ticket.setSeatNumber(rs.getString("seat_number"));
            ticket.setPrice(rs.getBigDecimal("price"));
            ticket.setPurchased(rs.getBoolean("purchased"));
            ticket.setUserId(rs.getLong("user_id"));
            return ticket;
        }
    };

    @Override
    public Optional<Ticket> findById(Long id) {
        String sql = "SELECT * FROM tickets WHERE id = ?";
        return jdbcTemplate.query(sql, ticketRowMapper, id).stream().findFirst();
    }

    @Override
    public List<Ticket> findAll(String dateTime, String departurePoint, String destinationPoint, String carrierName, int page, int size) {
        StringBuilder sql = new StringBuilder("SELECT t.* FROM tickets t " +
                "JOIN routes r ON t.route_id = r.id " +
                "JOIN carriers c ON r.carrier_id = c.id " +
                "WHERE (1=1)");

        if (dateTime != null) {
            sql.append(" AND t.date_time = '").append(dateTime).append("'");
        }
        if (departurePoint != null) {
            sql.append(" AND r.departure_point = '").append(departurePoint).append("'");
        }
        if (destinationPoint != null) {
            sql.append(" AND r.destination_point = '").append(destinationPoint).append("'");
        }
        if (carrierName != null) {
            sql.append(" AND c.name = '").append(carrierName).append("'");
        }
        sql.append(" LIMIT ").append(size).append(" OFFSET ").append(page * size);

        return jdbcTemplate.query(sql.toString(), ticketRowMapper);
    }

    @Override
    public void save(Ticket ticket) {
        String sql = "INSERT INTO tickets (route_id, date_time, seat_number, price, purchased, user_id) VALUES (?, ?, ?, ?, false, ?)";
        jdbcTemplate.update(sql, ticket.getRouteId(), ticket.getDateTime(), ticket.getSeatNumber(), ticket.getPrice(), ticket.getUserId());
    }

    @Override
    public void update(Ticket ticket) {
        String sql = "UPDATE tickets SET route_id = ?, date_time = ?, seat_number = ?, price = ?, purchased = ?, user_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, ticket.getRouteId(), ticket.getDateTime(), ticket.getSeatNumber(), ticket.getPrice(), ticket.isPurchased(), ticket.getUserId(), ticket.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM tickets WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Ticket> findPurchasedTicketsForCurrentUser(Long userId) {
        String sql = "SELECT * FROM tickets WHERE user_id = ?";
        return jdbcTemplate.query(sql, ticketRowMapper, userId);
    }
}
