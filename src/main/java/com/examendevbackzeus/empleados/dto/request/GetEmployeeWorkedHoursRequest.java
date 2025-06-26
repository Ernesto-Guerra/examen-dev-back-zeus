package com.examendevbackzeus.empleados.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class GetEmployeeWorkedHoursRequest {
    @NotNull(message = "El id del empleado es obligatorio.")
    @Schema(description = "ID del empleado", example = "1")
    private Long employee_id;

    @NotNull(message = "La fecha de inicio es obligatoria.")
    @Schema(description = "Fecha de inicio del rango", example = "2024-10-30")
    private LocalDate start_date;

    @NotNull(message = "La fecha de fin es obligatoria.")
    @Schema(description = "Fecha de fin del rango", example = "2025-10-30")
    private LocalDate end_date;

    public Long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Long employee_id) {
        this.employee_id = employee_id;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }
}
