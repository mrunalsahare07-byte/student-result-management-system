package com.college.resultsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResultDTO {
    private String rollNumber;
    private String studentName;
    private String department;
    private List<SubjectMarkDetail> marksList;
    private BigDecimal totalMarksObtained;
    private Integer totalMaxMarks;
    private Double percentage;
    private String overallGrade;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubjectMarkDetail {
        private String subjectCode;
        private String subjectName;
        private BigDecimal marksObtained;
        private Integer maxMarks;
        private String grade;
    }
}