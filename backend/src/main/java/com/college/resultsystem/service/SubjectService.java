package com.college.resultsystem.service;

import com.college.resultsystem.dto.SubjectDTO;
import com.college.resultsystem.entity.Subject;
import com.college.resultsystem.exception.BadRequestException;
import com.college.resultsystem.exception.ResourceNotFoundException;
import com.college.resultsystem.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public List<SubjectDTO> getAllSubjects() {
        return subjectRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SubjectDTO createSubject(SubjectDTO dto) {
        if (subjectRepository.existsBySubjectCode(dto.getSubjectCode())) {
            throw new BadRequestException("Subject with code " + dto.getSubjectCode() + " already exists");
        }

        Subject subject = new Subject();
        subject.setSubjectCode(dto.getSubjectCode());
        subject.setSubjectName(dto.getSubjectName());
        subject.setMaxMarks(dto.getMaxMarks() != null ? dto.getMaxMarks() : 100);

        Subject saved = subjectRepository.save(subject);
        return mapToDTO(saved);
    }

    private SubjectDTO mapToDTO(Subject subject) {
        return new SubjectDTO(
                subject.getId(),
                subject.getSubjectCode(),
                subject.getSubjectName(),
                subject.getMaxMarks()
        );
    }
}