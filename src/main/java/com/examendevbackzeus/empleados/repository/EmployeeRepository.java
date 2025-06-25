package com.examendevbackzeus.empleados.repository;

import com.examendevbackzeus.empleados.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByNameIgnoreCaseAndLastNameIgnoreCase(String name, String lastName);
    List<Employee> findByJobId(Long jobId);

    @NonNull
    Optional<Employee> findById(@NonNull Long employeeId);

    @Query("SELECT SUM(e.hours) FROM EmployeeWorkedHours e WHERE e.employee.id = :employeeId AND e.workedDate BETWEEN :start AND :end")
    Integer sumHoursByEmployeeIdAndDateRange(Long employeeId, LocalDate start, LocalDate end);

}
