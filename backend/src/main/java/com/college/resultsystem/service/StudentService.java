package com.college.resultsystem.service;

import com.college.resultsystem.dto.StudentRequestDTO;
import com.college.resultsystem.dto.StudentResponseDTO;
import com.college.resultsystem.entity.Student;
import com.college.resultsystem.exception.BadRequestException;
import com.college.resultsystem.exception.ResourceNotFoundException;
import com.college.resultsystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public StudentResponseDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return mapToResponseDTO(student);
    }

    public StudentResponseDTO getStudentByRollNumber(String rollNumber) {
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with roll number: " + rollNumber));
        return mapToResponseDTO(student);
    }

    public StudentResponseDTO createStudent(StudentRequestDTO requestDTO) {
        if (studentRepository.existsByRollNumber(requestDTO.getRollNumber())) {
            throw new BadRequestException("Student with roll number " + requestDTO.getRollNumber() + " already exists");
        }

        Student student = new Student();
        student.setRollNumber(requestDTO.getRollNumber());
        student.setFirstName(requestDTO.getFirstName());
        student.setLastName(requestDTO.getLastName());
        student.setDepartment(requestDTO.getDepartment());

        Student savedStudent = studentRepository.save(student);
        return mapToResponseDTO(savedStudent);
    }

    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO requestDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        // If roll number is being changed, ensure new roll number isn't taken by another student
        if (!student.getRollNumber().equalsIgnoreCase(requestDTO.getRollNumber())
                && studentRepository.existsByRollNumber(requestDTO.getRollNumber())) {
            throw new BadRequestException("Student with roll number " + requestDTO.getRollNumber() + " already exists");
        }

        student.setRollNumber(requestDTO.getRollNumber());
        student.setFirstName(requestDTO.getFirstName());
        student.setLastName(requestDTO.getLastName());
        student.setDepartment(requestDTO.getDepartment());

        Student updatedStudent = studentRepository.save(student);
        return mapToResponseDTO(updatedStudent);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    private StudentResponseDTO mapToResponseDTO(Student student) {
        return new StudentResponseDTO(
                student.getId(),
                student.getRollNumber(),
                student.getFirstName(),
                student.getLastName(),
                student.getDepartment()
        );
    }
}