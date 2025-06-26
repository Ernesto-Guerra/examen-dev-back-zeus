package com.examendevbackzeus.empleados.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class SaveEmployeeResponse {
    @Schema(description = "ID del empleado guardado")
    private Long id;
    @Schema(description = "Resultado de la operacion")
    private boolean success;

    public SaveEmployeeResponse(Long id, boolean success) {
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
