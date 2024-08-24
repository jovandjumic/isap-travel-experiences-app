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