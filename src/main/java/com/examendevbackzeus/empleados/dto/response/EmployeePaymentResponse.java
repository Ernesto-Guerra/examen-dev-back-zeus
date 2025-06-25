package com.examendevbackzeus.empleados.dto.response;

import java.math.BigDecimal;

public class EmployeePaymentResponse {
    private BigDecimal payment;
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
