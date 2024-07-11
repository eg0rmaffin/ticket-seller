DROP TABLE IF EXISTS tickets CASCADE;
DROP TABLE IF EXISTS routes CASCADE;
DROP TABLE IF EXISTS carriers CASCADE;

CREATE TABLE carriers (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          phone VARCHAR(255)
);

CREATE TABLE routes (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        departure_point VARCHAR(255) NOT NULL,
                        destination_point VARCHAR(255) NOT NULL,
                        carrier_id INTEGER,
                        FOREIGN KEY (carrier_id) REFERENCES carriers (id) ON DELETE CASCADE
);

CREATE TABLE tickets (
                         id SERIAL PRIMARY KEY,
                         route_id INTEGER,
                         date_time TIMESTAMP,
                         seat_number VARCHAR(255),
                         price DECIMAL,
                         purchased BOOLEAN,
                         user_id INTEGER,
                         FOREIGN KEY (route_id) REFERENCES routes (id) ON DELETE CASCADE
);
