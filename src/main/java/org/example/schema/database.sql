CREATE DATABASE student_registration;

USE student_registration;

CREATE TABLE students (
                          student_id VARCHAR(10) PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          age INT NOT NULL,
                          address TEXT NOT NULL,
                          phone VARCHAR(15) NOT NULL,
                          course VARCHAR(50) NOT NULL
);

INSERT INTO students (student_id, name, age, address, phone, course)
VALUES
    ('STU10001', 'Hasindu Udara', 23, 'Colombo', '0771234567', 'Computer Science'),
    ('STU10002', 'Nimali Perera', 21, 'Galle', '0712345678', 'Information Technology');
