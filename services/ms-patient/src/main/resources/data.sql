-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS ms_patient_db;
USE ms_patient_db;

-- Drop tables if they exist
DROP TABLE IF EXISTS patient;
DROP TABLE IF EXISTS address;

-- Table to store information about address
CREATE TABLE address
(
    address_id   INT AUTO_INCREMENT PRIMARY KEY,
    street       VARCHAR(100) NOT NULL,
    city         VARCHAR(50) NOT NULL,
    postal_code  VARCHAR(10) NOT NULL,
    UNIQUE KEY unique_address (street, city, postal_code)
);

-- Table to store information about patient
CREATE TABLE patient
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    last_name     VARCHAR(50) NOT NULL,
    first_name    VARCHAR(50) NOT NULL,
    date_of_birth DATE        NOT NULL,
    gender        CHAR(1)     NOT NULL CHECK (gender IN ('M', 'F')),
    address_id    INT,
    phone_number  VARCHAR(12),
    FOREIGN KEY (address_id) REFERENCES address(address_id),
    UNIQUE KEY unique_patient (first_name, last_name, date_of_birth)
);

-- Insert data into the address table
INSERT INTO address (street, city, postal_code)
VALUES ('1 Brookside St', 'City1', '12345'),
       ('2 High St', 'City2', '67890'),
       ('3 Club Road', 'City3', '54321'),
       ('4 Valley Dr', 'City4', '09876');

-- Insert data into the patient table
INSERT INTO patient (last_name, first_name, date_of_birth, gender, address_id, phone_number)
VALUES ('TestNone', 'Test', '1966-12-31', 'F', 1, '100-222-3333'),
       ('TestBorderline', 'Test', '1945-06-24', 'M', 2, '200-333-4444'),
       ('TestInDanger', 'Test', '2004-06-18', 'M', 3, '300-444-5555'),
       ('TestEarlyOnset', 'Test', '2002-06-28', 'F', 4, '400-555-6666');
