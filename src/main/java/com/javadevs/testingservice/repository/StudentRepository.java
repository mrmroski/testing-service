package com.javadevs.testingservice.repository;

import com.javadevs.testingservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
