USE student_result_db;

-- Insert Roles
INSERT INTO roles (name) VALUES
                             ('ROLE_ADMIN'),
                             ('ROLE_STUDENT');

-- Insert Users (one admin, two students)
INSERT INTO users (username, password, email, role_id) VALUES
                                                           ('admin', 'admin123', 'admin@college.edu', 1),
                                                           ('john_doe', 'student123', 'john@college.edu', 2),
                                                           ('jane_smith', 'student123', 'jane@college.edu', 2);

-- Insert Students
INSERT INTO students (roll_number, first_name, last_name, department, user_id) VALUES
                                                                                   ('CS202601', 'John', 'Doe', 'Computer Engineering', 2),
                                                                                   ('CS202602', 'Jane', 'Smith', 'Computer Engineering', 3);

-- Insert Subjects
INSERT INTO subjects (subject_code, subject_name, max_marks) VALUES
                                                                 ('CS501', 'Database Management Systems', 100),
                                                                 ('CS502', 'Operating Systems', 100),
                                                                 ('CS503', 'Computer Networks', 100);

-- Insert Marks
INSERT INTO marks (student_id, subject_id, marks_obtained, grade) VALUES
                                                                      (1, 1, 88.50, 'A'),
                                                                      (1, 2, 76.00, 'B'),
                                                                      (2, 1, 94.00, 'A+'),
                                                                      (2, 3, 81.50, 'A');

