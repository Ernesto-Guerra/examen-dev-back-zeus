package com.examendevbackzeus.empleados.controller;


import com.examendevbackzeus.empleados.dto.request.EmployeeByJobRequest;
import com.examendevbackzeus.empleados.dto.request.EmployeePaymentRequest;
import com.examendevbackzeus.empleados.dto.request.SaveEmployeeRequest;
import com.examendevbackzeus.empleados.dto.response.EmployeeByJobResponse;
import com.examendevbackzeus.empleados.dto.response.EmployeePaymentResponse;
import com.examendevbackzeus.empleados.dto.response.SaveEmployeeResponse;
import com.examendevbackzeus.empleados.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public SaveEmployeeResponse createEmployee(/*omito el @Valid para mantener la respuesta como indica el ejercicio*/ @RequestBody SaveEmployeeRequest request) {
        return employeeService.save(request);
    }

    @PostMapping("/job")
    public EmployeeByJobResponse getEmployeesByJob(@RequestBody EmployeeByJobRequest request) {
        return employeeService.getEmployeesByJob(request.getJob_id());
    }

    @PostMapping("/payment")
    public EmployeePaymentResponse getPayment(/*omito el @Valid para mantener la respuesta como indica el ejercicio*/ @RequestBody EmployeePaymentRequest request) {
        return employeeService.getEmployeePayment(request);
    }

}
