package com.examendevbackzeus.empleados.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class SaveEmployeeWorkedHoursRequest {
    @NotNull(message = "El id del empleado es obligatorio.")
    private Long employee_id;

    @NotNull(message = "La fecha de trabajo es obligatoria.")
    private LocalDate worked_date;

    @Min(value = 1, message = "Debe trabajar al menos 1 hora.")
    @Max(value = 20, message = "No puede trabajar más de 20 horas.")
    private int hours;

    public Long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Long employee_id) {
        this.employee_id = employee_id;
    }

    public LocalDate getWorked_date() {
        return worked_date;
    }

    public void setWorked_date(LocalDate worked_date) {
        this.worked_date = worked_date;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
}
