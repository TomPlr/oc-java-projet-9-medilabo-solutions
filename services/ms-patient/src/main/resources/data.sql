-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS ms_patient_db;
USE ms_patient_db;

-- Drop tables if they exist
DROP TABLE IF EXISTS patient;
DROP TABLE IF EXISTS user;

-- Table to store information about patient
CREATE TABLE patient
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    last_name     VARCHAR(50) NOT NULL,
    first_name    VARCHAR(50) NOT NULL,
    date_of_birth DATE        NOT NULL,
    gender        CHAR(1)     NOT NULL CHECK (gender IN ('M', 'F')),
    address       VARCHAR(100),
    phone_number  VARCHAR(12)
);

-- Table to store information about user
CREATE TABLE user
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    last_name  VARCHAR(50)  NOT NULL,
    first_name VARCHAR(50)  NOT NULL,
    email      VARCHAR(50)  NOT NULL,
    password   VARCHAR(100) NOT NULL,
    role       VARCHAR(100) NOT NULL CHECK ( role IN ('MANAGER', 'PRACTITIONER'))
);

-- Insert data into the patient table
INSERT INTO patient (last_name, first_name, date_of_birth, gender, address, phone_number)
VALUES ('TestNone', 'Test', '1966-12-31', 'F', '1 Brookside St', '100-222-3333'),
       ('TestBorderline', 'Test', '1945-06-24', 'M', '2 High St', '200-333-4444'),
       ('TestInDanger', 'Test', '2004-06-18', 'M', '3 Club Road', '300-444-5555'),
       ('TestEarlyOnset', 'Test', '2002-06-28', 'F', '4 Valley Dr', '400-555-6666');

-- Insert data into the user table
INSERT INTO user (last_name, first_name, email, password,role)
VALUES ('Doe', 'john', 'johndoe@gmail.com', '$2y$10$tCmRdeGQ0VMAUsp6LgaPY.yijeg/d9tAI0qGD.a9dECwUC7bMppHW','MANAGER');