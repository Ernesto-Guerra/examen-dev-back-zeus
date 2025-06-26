package com.examendevbackzeus.empleados.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class GetEmployeeWorkedHoursResponse {
    @Schema(description = "Total de horas trabajadas en el rango de fechas")
    private Integer total_worked_hours;
    @Schema(description = "Resultado de la operacion")
    private boolean success;

    public GetEmployeeWorkedHoursResponse(Integer total_worked_hours, boolean success) {
        this.total_worked_hours = total_worked_hours;
        this.success = success;
    }

    public Integer getTotal_worked_hours() {
        return total_worked_hours;
    }

    public void setTotal_worked_hours(Integer total_worked_hours) {
        this.total_worked_hours = total_worked_hours;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
