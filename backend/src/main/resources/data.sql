CREATE TABLE country (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    country_name VARCHAR(255) NOT NULL,
    continent VARCHAR(255) NOT NULL
);

CREATE TABLE destination (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    location_name VARCHAR(255) NOT NULL,
    location_type VARCHAR(255),
    region_area VARCHAR(255),
    country_id BIGINT,
    FOREIGN KEY (country_id) REFERENCES country(id)
);
CREATE TABLE costs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    travel_cost DOUBLE NOT NULL,
    travel_mode VARCHAR(255),
    travel_route VARCHAR(255),
    travel_cost_type VARCHAR(255),
    accommodation_cost DOUBLE NOT NULL,
    accommodation_cost_type VARCHAR(255),
    other_costs DOUBLE,
    other_costs_type VARCHAR(255),
    total_cost DOUBLE
);
CREATE TABLE experience (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    destination_id BIGINT,
    days_spent INT NOT NULL,
    costs_id BIGINT,
    rating DOUBLE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (destination_id) REFERENCES destination(id),
    FOREIGN KEY (costs_id) REFERENCES costs(id)
);



-- Insert data
INSERT INTO country (country_name, continent) VALUES ('France', 'Europe');
INSERT INTO country (country_name, continent) VALUES ('Germany', 'Europe');
INSERT INTO country (country_name, continent) VALUES ('Italy', 'Europe');

INSERT INTO destination (location_name, location_type, region_area, country_id) VALUES ('Paris', 'City', 'Île-de-France', 1);
INSERT INTO destination (location_name, location_type, region_area, country_id) VALUES ('Berlin', 'City-State', 'Berlin', 2);
INSERT INTO destination (location_name, location_type, region_area, country_id) VALUES ('Rome', 'City', 'Lazio', 3);
INSERT INTO destination (location_name, location_type, region_area, country_id) VALUES ('Munich', 'City', 'Bavaria', 2);
INSERT INTO destination (location_name, location_type, region_area, country_id) VALUES ('Lyon', 'City', 'Auvergne-Rhône-Alpes', 1);
INSERT INTO destination (location_name, location_type, region_area, country_id) VALUES ('Florence', 'City', 'Tuscany', 3);

INSERT INTO costs (travel_cost, accommodation_cost, other_costs) VALUES (1000.0, 500.0, 200.0);
INSERT INTO costs (travel_cost, accommodation_cost, other_costs) VALUES (800.0, 450.0, 150.0);
INSERT INTO costs (travel_cost, accommodation_cost, other_costs) VALUES (700.0, 400.0, 100.0);
INSERT INTO costs (travel_cost, accommodation_cost, other_costs) VALUES (900.0, 600.0, 250.0);
INSERT INTO costs (travel_cost, accommodation_cost, other_costs) VALUES (600.0, 350.0, 100.0);
INSERT INTO costs (travel_cost, accommodation_cost, other_costs) VALUES (1100.0, 550.0, 200.0);

INSERT INTO experience (destination_id, days_spent, rating, created_at, costs_id) VALUES (1, 7, 4.5, '2023-07-30T10:00:00', 1);
INSERT INTO experience (destination_id, days_spent, rating, created_at, costs_id) VALUES (2, 5, 4.7, '2023-06-15T14:30:00', 2);
INSERT INTO experience (destination_id, days_spent, rating, created_at, costs_id) VALUES (3, 3, 4.0, '2023-08-10T09:00:00', 3);
INSERT INTO experience (destination_id, days_spent, rating, created_at, costs_id) VALUES (4, 6, 4.8, '2023-05-20T08:45:00', 4);
INSERT INTO experience (destination_id, days_spent, rating, created_at, costs_id) VALUES (5, 4, 3.9, '2023-07-25T16:20:00', 5);
INSERT INTO experience (destination_id, days_spent, rating, created_at, costs_id) VALUES (6, 8, 4.6, '2023-07-05T11:10:00', 6);
