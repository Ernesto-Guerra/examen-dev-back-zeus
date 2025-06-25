package com.examendevbackzeus.empleados.service;

import com.examendevbackzeus.empleados.dto.*;
import com.examendevbackzeus.empleados.dto.request.EmployeePaymentRequest;
import com.examendevbackzeus.empleados.dto.request.SaveEmployeeRequest;
import com.examendevbackzeus.empleados.dto.response.EmployeeByJobResponse;
import com.examendevbackzeus.empleados.dto.response.EmployeePaymentResponse;
import com.examendevbackzeus.empleados.dto.response.SaveEmployeeResponse;
import com.examendevbackzeus.empleados.entity.Employee;
import com.examendevbackzeus.empleados.entity.Gender;
import com.examendevbackzeus.empleados.entity.Job;
import com.examendevbackzeus.empleados.repository.EmployeeRepository;
import com.examendevbackzeus.empleados.repository.GenderRepository;
import com.examendevbackzeus.empleados.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final GenderRepository genderRepository;
    private final JobRepository jobRepository;

    public EmployeeService(EmployeeRepository employeeRepository, GenderRepository genderRepository, JobRepository jobRepository) {
        this.employeeRepository = employeeRepository;
        this.genderRepository = genderRepository;
        this.jobRepository = jobRepository;
    }

    // metodo para crear nuevos empleados
    public SaveEmployeeResponse save(SaveEmployeeRequest employee){
        try{
            // buscamos si existe empleado con ese nombre y apellido
            boolean exists = employeeRepository.existsByNameIgnoreCaseAndLastNameIgnoreCase(
                    employee.getName(), employee.getLastName());
            if (exists) return new SaveEmployeeResponse(null, false);

            // Validamos que el empleado sea mayor de edad (al menos 18 años)
            if ((Period.between(employee.getBirthdate(), LocalDate.now()).getYears()) < 18)
                return new SaveEmployeeResponse(null, false);

            Gender gender = genderRepository.findById(employee.getGender_id())
                    .orElse(null);
            if (gender == null) return new SaveEmployeeResponse(null, false);

            Job job = jobRepository.findById(employee.getJob_id())
                    .orElse(null);
            if (job == null) return new SaveEmployeeResponse(null, false);

            // Construir empleado
            Employee employeeBuild = new Employee();
            employeeBuild.setGender(gender);
            employeeBuild.setJob(job);
            employeeBuild.setName(employee.getName());
            employeeBuild.setLastName(employee.getLastName());
            employeeBuild.setBirthdate(employee.getBirthdate());

            // Mandamos el empleado para guardarlo
            Employee nuevoEmpleado = employeeRepository.save(employeeBuild);

            return new SaveEmployeeResponse(nuevoEmpleado.getId(), true);
        }
        catch (Exception e){
            // En caso de algun error, la respuesta es id: null y success: false
            return new SaveEmployeeResponse(null, false);
        }
    }

    // metodo para obtener empleados por id del trabajo/puesto
    public EmployeeByJobResponse getEmployeesByJob(Long jobId) {
        try {
            // Validamos que el trabajo exista
            if (!jobRepository.existsById(jobId)) {
                // NO EXISTE EL TRABAJO
                // si no existe el trabajo, tenemos un problema
                return new EmployeeByJobResponse(null, false);
            }

            // Obtenemos los empleados con el trabajo cuyo id ya verificamos que existe
            List<Employee> empleados = employeeRepository.findByJobId(jobId);

            // Usamos parallel stream oara ordenar
            List<Employee> empleadosOrdenados = empleados.parallelStream()
                    .sorted(Comparator
                            // primero ordenamos por sueldo de manera descendente (según, porque como tienen el mismo trabajo todos tienen el mismo sueldo) ¿¿¿¿¿?????
                            .comparing((Employee e) -> e.getJob().getSalary()).reversed()
                            // despues ordenamos por id de manera ascendente
                            .thenComparing(Employee::getId))
                    .collect(Collectors.toList());

            // También usamos parallel strean en el mapeo de datos
            List<EmployeeDetail> detalles = empleadosOrdenados.parallelStream().map(e -> {
                EmployeeDetail detail = new EmployeeDetail();
                detail.setId(e.getId());
                detail.setName(e.getName());
                detail.setLast_name(e.getLastName());
                // Mantenemos el formato indicado en la respuesta del ejercicio
                detail.setBirthdate(e.getBirthdate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                detail.setJob(e.getJob());
                detail.setGender(e.getGender());
                return detail;
            }).collect(Collectors.toList());

            return new EmployeeByJobResponse(detalles, true);

        } catch (Exception e) {
            return new EmployeeByJobResponse(null, false);
        }
    }

    public EmployeePaymentResponse getEmployeePayment(EmployeePaymentRequest request) {
        try {
            // Utilizamos un optional para obtener al empleado en caso de existir
            Optional<Employee> optional = employeeRepository.findById(request.getEmployee_id());
            if (optional.isEmpty()) {
                return new EmployeePaymentResponse(null, false);
            }

            // Validar si la fecha inicial es despues de la fecha final
            if (request.getStart_date().isAfter(request.getEnd_date())) {
                // si la fecha inicial va despues de la fecha final, los datos recibidos son invalidos
                return new EmployeePaymentResponse(null, false);
            }

            // si estamos aqui, es porque ya sabemos que el empleado SI existe
            Employee employee = optional.get();

            // Estoy considerando que el sueldo registrado en el trabajo es por hora
            BigDecimal hourlySalary = employee.getJob().getSalary();

            // Obtenemos las horas que el empleado trabajó en el rango de fechas
            Integer totalHours = employeeRepository
                    .sumHoursByEmployeeIdAndDateRange(
                            request.getEmployee_id(),
                            request.getStart_date(),
                            request.getEnd_date()
                    );


            //if (totalHours == null) totalHours = 0;
            // Multiplicamos el sueldo por hora por la cantidad de horas trabajadas en el rango de fechas
            BigDecimal payment = hourlySalary.multiply(BigDecimal.valueOf(totalHours == null ? 0 : totalHours));

            return new EmployeePaymentResponse(payment, true);
        } catch (Exception e) {
            return new EmployeePaymentResponse(null, false);
        }
    }

}
