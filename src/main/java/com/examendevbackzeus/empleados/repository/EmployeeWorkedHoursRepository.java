package com.examendevbackzeus.empleados.repository;

import com.examendevbackzeus.empleados.entity.EmployeeWorkedHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Optional;

public interface EmployeeWorkedHoursRepository extends JpaRepository<EmployeeWorkedHours, Long> {
    boolean existsByEmployeeIdAndWorkedDate(Long employeeId, LocalDate workedDate);

    @NonNull
    Optional<EmployeeWorkedHours> findById(@NonNull Long employeeId);

    @Query("SELECT SUM(e.hours) FROM EmployeeWorkedHours e WHERE e.employee.id = :employeeId AND e.workedDate BETWEEN :startDate AND :endDate")
    Integer sumHoursByEmployeeAndDateRange(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
