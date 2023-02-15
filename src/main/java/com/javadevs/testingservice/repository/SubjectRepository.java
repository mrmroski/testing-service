package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
