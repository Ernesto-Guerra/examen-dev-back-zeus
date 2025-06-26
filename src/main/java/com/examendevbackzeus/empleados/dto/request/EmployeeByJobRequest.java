package com.examendevbackzeus.empleados.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class EmployeeByJobRequest {
    @NotNull(message = "El id del trabajo es obligatorio.")
    @Schema(description = "ID del trabajo a buscar", example = "1")
    private Long job_id;

    public Long getJob_id() {
        return job_id;
    }

    public void setJob_id(Long job_id) {
        this.job_id = job_id;
    }
}
