package com.examendevbackzeus.empleados.controller;


import com.examendevbackzeus.empleados.dto.request.EmployeeByJobRequest;
import com.examendevbackzeus.empleados.dto.request.EmployeePaymentRequest;
import com.examendevbackzeus.empleados.dto.request.SaveEmployeeRequest;
import com.examendevbackzeus.empleados.dto.response.EmployeeByJobResponse;
import com.examendevbackzeus.empleados.dto.response.EmployeePaymentResponse;
import com.examendevbackzeus.empleados.dto.response.SaveEmployeeResponse;
import com.examendevbackzeus.empleados.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Operation(summary = "Crear un nuevo registro de empleado.")
    @PostMapping
    public SaveEmployeeResponse createEmployee(/*omito el @Valid para mantener la respuesta como indica el ejercicio*/ @RequestBody SaveEmployeeRequest request) {
        return employeeService.save(request);
    }

    @Operation(summary = "Obtener los empleados por trabajo",
            description = "Retorna los registros de empleados que tienen el trabajo con el ID enviado.")
    @PostMapping("/job")
    public EmployeeByJobResponse getEmployeesByJob(@RequestBody EmployeeByJobRequest request) {
        return employeeService.getEmployeesByJob(request.getJob_id());
    }

    @Operation(summary = "Obtener el pago de un empleado por fecha.",
            description = "Retorna el total de a pagar a un empleado segun las horas trabajadas entre dos fechas.")
    @PostMapping("/payment")
    public EmployeePaymentResponse getPayment(/*omito el @Valid para mantener la respuesta como indica el ejercicio*/ @RequestBody EmployeePaymentRequest request) {
        return employeeService.getEmployeePayment(request);
    }

}
