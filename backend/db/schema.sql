-- Step 1: Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS student_result_db;
USE student_result_db;

-- Step 2: Drop existing tables in reverse order of foreign keys (clean slate)
DROP TABLE IF EXISTS marks;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS subjects;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

-- Table 1: Roles (Admin, Faculty, Student)
CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE
);

-- Table 2: Users (Login credentials)
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       role_id BIGINT NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT fk_users_roles FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE RESTRICT
);

-- Table 3: Students (Academic profile)
CREATE TABLE students (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          roll_number VARCHAR(30) NOT NULL UNIQUE,
                          first_name VARCHAR(50) NOT NULL,
                          last_name VARCHAR(50) NOT NULL,
                          department VARCHAR(50) NOT NULL,
                          user_id BIGINT UNIQUE,
                          CONSTRAINT fk_students_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL
);

-- Table 4: Subjects (Course modules)
CREATE TABLE subjects (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          subject_code VARCHAR(20) NOT NULL UNIQUE,
                          subject_name VARCHAR(100) NOT NULL,
                          max_marks INT NOT NULL DEFAULT 100
);

-- Table 5: Marks (Bridge between Student and Subject)
CREATE TABLE marks (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       student_id BIGINT NOT NULL,
                       subject_id BIGINT NOT NULL,
                       marks_obtained DECIMAL(5, 2) NOT NULL,
                       grade VARCHAR(5) NOT NULL,
                       CONSTRAINT fk_marks_students FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
                       CONSTRAINT fk_marks_subjects FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE,
                       CONSTRAINT uk_student_subject UNIQUE (student_id, subject_id)
);
