package com.examendevbackzeus.empleados.dto.response;

public class SaveEmployeeWorkedHoursResponse {
    private Long id;
    private boolean success;

    public SaveEmployeeWorkedHoursResponse(Long id, boolean success) {
        this.id = id;
        this.success = success;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
