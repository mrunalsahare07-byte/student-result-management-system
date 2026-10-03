package com.college.resultsystem.service;

import com.college.resultsystem.dto.MarkEntryDTO;
import com.college.resultsystem.dto.StudentResultDTO;
import com.college.resultsystem.entity.Mark;
import com.college.resultsystem.entity.Student;
import com.college.resultsystem.entity.Subject;
import com.college.resultsystem.exception.BadRequestException;
import com.college.resultsystem.exception.ResourceNotFoundException;
import com.college.resultsystem.repository.MarkRepository;
import com.college.resultsystem.repository.StudentRepository;
import com.college.resultsystem.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkService {

    private final MarkRepository markRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public Mark saveOrUpdateMark(MarkEntryDTO dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + dto.getStudentId()));

        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + dto.getSubjectId()));

        if (dto.getMarksObtained().doubleValue() > subject.getMaxMarks()) {
            throw new BadRequestException("Marks obtained cannot be greater than max marks (" + subject.getMaxMarks() + ")");
        }

        // Check if a mark entry already exists for this student and subject
        Mark mark = markRepository.findByStudentIdAndSubjectId(student.getId(), subject.getId())
                .orElse(new Mark());

        mark.setStudent(student);
        mark.setSubject(subject);
        mark.setMarksObtained(dto.getMarksObtained());
        mark.setGrade(calculateGrade(dto.getMarksObtained(), subject.getMaxMarks()));

        return markRepository.save(mark);
    }

    public StudentResultDTO getStudentResultByRollNumber(String rollNumber) {
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with roll number: " + rollNumber));

        List<Mark> marks = markRepository.findByStudentId(student.getId());

        List<StudentResultDTO.SubjectMarkDetail> markDetails = new ArrayList<>();
        BigDecimal totalObtained = BigDecimal.ZERO;
        int totalMax = 0;

        for (Mark m : marks) {
            markDetails.add(new StudentResultDTO.SubjectMarkDetail(
                    m.getSubject().getSubjectCode(),
                    m.getSubject().getSubjectName(),
                    m.getMarksObtained(),
                    m.getSubject().getMaxMarks(),
                    m.getGrade()
            ));
            totalObtained = totalObtained.add(m.getMarksObtained());
            totalMax += m.getSubject().getMaxMarks();
        }

        double percentage = 0.0;
        if (totalMax > 0) {
            percentage = totalObtained.divide(BigDecimal.valueOf(totalMax), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
        }

        String overallGrade = calculateOverallGrade(percentage);

        return new StudentResultDTO(
                student.getRollNumber(),
                student.getFirstName() + " " + student.getLastName(),
                student.getDepartment(),
                markDetails,
                totalObtained,
                totalMax,
                percentage,
                overallGrade
        );
    }

    private String calculateGrade(BigDecimal marks, int maxMarks) {
        double percentage = (marks.doubleValue() / maxMarks) * 100.0;
        return calculateOverallGrade(percentage);
    }

    private String calculateOverallGrade(double percentage) {
        if (percentage >= 90.0) return "A+";
        if (percentage >= 80.0) return "A";
        if (percentage >= 70.0) return "B";
        if (percentage >= 60.0) return "C";
        if (percentage >= 50.0) return "D";
        return "F";
    }
}