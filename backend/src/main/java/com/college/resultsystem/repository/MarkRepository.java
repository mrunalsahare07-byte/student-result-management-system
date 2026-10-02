package com.college.resultsystem.repository;

import com.college.resultsystem.entity.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {
    List<Mark> findByStudentId(Long studentId);
    Optional<Mark> findByStudentIdAndSubjectId(Long studentId, Long subjectId);
}