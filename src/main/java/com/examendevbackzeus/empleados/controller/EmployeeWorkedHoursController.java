package com.examendevbackzeus.empleados.controller;

import com.examendevbackzeus.empleados.dto.request.GetEmployeeWorkedHoursRequest;
import com.examendevbackzeus.empleados.dto.request.SaveEmployeeWorkedHoursRequest;
import com.examendevbackzeus.empleados.dto.response.GetEmployeeWorkedHoursResponse;
import com.examendevbackzeus.empleados.dto.response.SaveEmployeeWorkedHoursResponse;
import com.examendevbackzeus.empleados.service.EmployeeWorkedHoursService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Crear un nuevo registro de horas trabajadas.",
            description = "Crea un registro que indica las horas trabajadas por un empleado en una fecha especifica")
    @PostMapping
    public SaveEmployeeWorkedHoursResponse save(/*omito el @Valid para mantener la respuesta como indica el ejercicio*/ @RequestBody() SaveEmployeeWorkedHoursRequest request) {
        return service.save(request);
    }

    @Operation(summary = "Obtener las horas trabajadas por un empleado",
            description = "Retorna el total de horas que un empleado trabajó entre dos fechas.")
    @PostMapping("/by-range")
    public GetEmployeeWorkedHoursResponse getWorkedHours(/*omito el @Valid para mantener la respuesta como indica el ejercicio*/ @RequestBody GetEmployeeWorkedHoursRequest request) {
        return service.getWorkedHoursInRange(request);
    }
}
