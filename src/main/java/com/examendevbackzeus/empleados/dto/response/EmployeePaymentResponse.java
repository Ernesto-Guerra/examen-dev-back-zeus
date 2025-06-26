package com.examendevbackzeus.empleados.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public class EmployeePaymentResponse {
    @Schema(description = "Cantidad pagada al empleado")
    private BigDecimal payment;
    @Schema(description = "Resultado de la operacion")
    private boolean success;

    public EmployeePaymentResponse(BigDecimal payment, boolean success) {
        this.payment = payment;
        this.success = success;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
