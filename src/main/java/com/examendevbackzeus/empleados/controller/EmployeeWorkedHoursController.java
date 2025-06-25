package com.examendevbackzeus.empleados.controller;

import com.examendevbackzeus.empleados.dto.request.GetEmployeeWorkedHoursRequest;
import com.examendevbackzeus.empleados.dto.request.SaveEmployeeWorkedHoursRequest;
import com.examendevbackzeus.empleados.dto.response.GetEmployeeWorkedHoursResponse;
import com.examendevbackzeus.empleados.dto.response.SaveEmployeeWorkedHoursResponse;
import com.examendevbackzeus.empleados.service.EmployeeWorkedHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee/worked-hours")
public class EmployeeWorkedHoursController {
    @Autowired
    private EmployeeWorkedHoursService service;


    @PostMapping
    public SaveEmployeeWorkedHoursResponse save(/*omito el @Valid para mantener la respuesta como indica el ejercicio*/ @RequestBody SaveEmployeeWorkedHoursRequest request) {
        return service.save(request);
    }

    @PostMapping("/by-range")
    public GetEmployeeWorkedHoursResponse getWorkedHours(/*omito el @Valid para mantener la respuesta como indica el ejercicio*/ @RequestBody GetEmployeeWorkedHoursRequest request) {
        return service.getWorkedHoursInRange(request);
    }
}
