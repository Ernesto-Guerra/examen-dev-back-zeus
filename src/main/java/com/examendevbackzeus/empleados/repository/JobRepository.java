package com.examendevbackzeus.empleados.repository;

import com.examendevbackzeus.empleados.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

}
