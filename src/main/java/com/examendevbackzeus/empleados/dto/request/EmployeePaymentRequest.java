package com.examendevbackzeus.empleados.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class EmployeePaymentRequest {
    @NotNull
    private Long employee_id;

    @NotNull
    private LocalDate start_date;

    @NotNull
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
