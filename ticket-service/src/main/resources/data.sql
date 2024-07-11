INSERT INTO carriers (name, phone) VALUES ('Aeroflot', '123-456-7890');
INSERT INTO carriers (name, phone) VALUES ('S7', '987-654-3210');

INSERT INTO routes (name, departure_point, destination_point, carrier_id) VALUES ('Moscow to Saint Petersburg', 'Moscow', 'Saint Petersburg', 1);
INSERT INTO routes (name, departure_point, destination_point, carrier_id) VALUES ('Amsterdam to Berlin', 'Amsterdam', 'Berlin', 2);

INSERT INTO tickets (route_id, date_time, seat_number, price, purchased, user_id) VALUES (1, '2024-07-20 12:00:00', 'A1', 100.00, false, NULL);
INSERT INTO tickets (route_id, date_time, seat_number, price, purchased, user_id) VALUES (2, '2024-07-21 12:00:00', 'B2', 200.00, false, NULL);
