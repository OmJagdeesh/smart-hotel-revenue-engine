CREATE TABLE IF NOT EXISTS hotels (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL UNIQUE,
    location VARCHAR(120),
    created_at DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(120) NOT NULL,
    role VARCHAR(20) NOT NULL,
    active BIT NOT NULL,
    hotel_id BIGINT,
    CONSTRAINT fk_users_hotel FOREIGN KEY (hotel_id) REFERENCES hotels (id)
);

CREATE TABLE IF NOT EXISTS rooms (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    hotel_id BIGINT NOT NULL,
    room_number VARCHAR(60) NOT NULL,
    name VARCHAR(80) NOT NULL,
    room_type VARCHAR(50) NOT NULL,
    base_price DECIMAL(10, 2) NOT NULL,
    active BIT NOT NULL,
    CONSTRAINT fk_rooms_hotel FOREIGN KEY (hotel_id) REFERENCES hotels (id),
    CONSTRAINT uk_room_number_per_hotel UNIQUE (hotel_id, room_number)
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    hotel_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_bookings_hotel FOREIGN KEY (hotel_id) REFERENCES hotels (id),
    CONSTRAINT fk_bookings_room FOREIGN KEY (room_id) REFERENCES rooms (id),
    CONSTRAINT fk_bookings_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS pricing_rules (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    hotel_id BIGINT NOT NULL,
    season_name VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    demand_multiplier DECIMAL(6, 3) NOT NULL,
    min_occupancy DECIMAL(6, 3) NOT NULL,
    max_occupancy DECIMAL(6, 3) NOT NULL,
    active BIT NOT NULL,
    CONSTRAINT fk_pricing_rule_hotel FOREIGN KEY (hotel_id) REFERENCES hotels (id)
);

CREATE TABLE IF NOT EXISTS revenue_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    hotel_id BIGINT NOT NULL,
    business_date DATE NOT NULL,
    total_revenue DECIMAL(12, 2) NOT NULL,
    occupied_rooms INT NOT NULL,
    available_rooms INT NOT NULL,
    occupancy_rate DECIMAL(6, 3) NOT NULL,
    rev_par DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_revenue_hotel FOREIGN KEY (hotel_id) REFERENCES hotels (id),
    CONSTRAINT uk_revenue_date UNIQUE (hotel_id, business_date)
);
