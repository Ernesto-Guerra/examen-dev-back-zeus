package com.examendevbackzeus.empleados.dto.request;

import jakarta.validation.constraints.NotNull;

public class EmployeeByJobRequest {
    @NotNull(message = "El id del trabajo es obligatorio.")
    private Long job_id;

    public Long getJob_id() {
        return job_id;
    }

    public void setJob_id(Long job_id) {
        this.job_id = job_id;
    }
}
