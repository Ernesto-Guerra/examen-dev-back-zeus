package com.examendevbackzeus.empleados.dto.response;

import com.examendevbackzeus.empleados.dto.EmployeeDetail;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class EmployeeByJobResponse {
    @Schema(description = "Lista de empleados")
    private List<EmployeeDetail> employees;
    @Schema(description = "Resultado de la operacion")
    private boolean success;

    public EmployeeByJobResponse(List<EmployeeDetail> employees, boolean success) {
        this.employees = employees;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<EmployeeDetail> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDetail> employees) {
        this.employees = employees;
    }
}
