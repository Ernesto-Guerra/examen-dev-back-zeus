package com.examendevbackzeus.empleados.service;

import com.examendevbackzeus.empleados.dto.request.GetEmployeeWorkedHoursRequest;
import com.examendevbackzeus.empleados.dto.request.SaveEmployeeWorkedHoursRequest;
import com.examendevbackzeus.empleados.dto.response.GetEmployeeWorkedHoursResponse;
import com.examendevbackzeus.empleados.dto.response.SaveEmployeeWorkedHoursResponse;
import com.examendevbackzeus.empleados.entity.Employee;
import com.examendevbackzeus.empleados.entity.EmployeeWorkedHours;
import com.examendevbackzeus.empleados.repository.EmployeeRepository;
import com.examendevbackzeus.empleados.repository.EmployeeWorkedHoursRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EmployeeWorkedHoursService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeWorkedHoursRepository workedHoursRepository;

    public EmployeeWorkedHoursService(EmployeeRepository employeeRepository, EmployeeWorkedHoursRepository workedHoursRepository) {
        this.employeeRepository = employeeRepository;
        this.workedHoursRepository = workedHoursRepository;
    }

    public SaveEmployeeWorkedHoursResponse save(SaveEmployeeWorkedHoursRequest request) {
        try{
            // Validamos que el empleado exista mediante el id
            Optional<Employee> employee = employeeRepository.findById(request.getEmployee_id());
            // Si no existe empleado con ese id, no podemos hacer el registro
            if (employee.isEmpty()) return new SaveEmployeeWorkedHoursResponse(null, false);

            // Validamos que sea una fecha que aun no haya pasado
            if (request.getWorked_date().isAfter(LocalDate.now()))
                // si la fecha es despues de hoy, regresamos el error
                return new SaveEmployeeWorkedHoursResponse(null, false);

            // Validamos que el empleado que ya sabemos que existe no tenga un registro en esa fecha
            boolean yaExiste = workedHoursRepository.existsByEmployeeIdAndWorkedDate(
                    request.getEmployee_id(), request.getWorked_date());
            if (yaExiste) return new SaveEmployeeWorkedHoursResponse(null, false);

            // Validamos que no supere las 20 horas trabajadas en la request
            if (request.getHours() < 1 || request.getHours() > 20) {
                // Si son mas de 20 horas o menos de 1, no debemos hacer el registro
                return new SaveEmployeeWorkedHoursResponse(null, false);
            }

            // Construir EmployeeWorkedHours
            EmployeeWorkedHours registro = new EmployeeWorkedHours();
            registro.setEmployee(employee.get());
            registro.setWorkedDate(request.getWorked_date());
            registro.setHours(request.getHours());


            // Mandamos el registro para guardarlo
            EmployeeWorkedHours saved = workedHoursRepository.save(registro);

            return new SaveEmployeeWorkedHoursResponse(saved.getId(), true);
        }
        catch (Exception e){
            // En caso de algun error, la respuesta es id: null y success: false
            return new SaveEmployeeWorkedHoursResponse(null, false);
        }
    }

    public GetEmployeeWorkedHoursResponse getWorkedHoursInRange(GetEmployeeWorkedHoursRequest request) {
        try {
            // Validaamos que el empleado exista
            if (!employeeRepository.existsById(request.getEmployee_id())) {
                return new GetEmployeeWorkedHoursResponse(null, false);
            }

            // Validar si la fecha inicial es despues de la fecha final
            if (request.getStart_date().isAfter(request.getEnd_date())) {
                // si la fecha inicial va despues de la fecha final, no debemos hacer el registro
                return new GetEmployeeWorkedHoursResponse(null, false);
            }

            // Sumamos las horas del empleado en ese rango de fechas
            Integer total = workedHoursRepository.sumHoursByEmployeeAndDateRange(
                    request.getEmployee_id(), request.getStart_date(), request.getEnd_date());

            // Si no hay registros, total puede ser null, en ese caso indicamos que tiene 0 horas registradas
            return new GetEmployeeWorkedHoursResponse(total == null ? 0 : total, true);

        } catch (Exception e) {
            return new GetEmployeeWorkedHoursResponse(null, false);
        }
    }

}

