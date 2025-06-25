package com.examendevbackzeus.empleados.service;

import com.examendevbackzeus.empleados.dto.request.SaveEmployeeWorkedHoursRequest;
import com.examendevbackzeus.empleados.dto.response.SaveEmployeeWorkedHoursResponse;
import com.examendevbackzeus.empleados.entity.Employee;
import com.examendevbackzeus.empleados.entity.EmployeeWorkedHours;
import com.examendevbackzeus.empleados.entity.Gender;
import com.examendevbackzeus.empleados.entity.Job;
import com.examendevbackzeus.empleados.repository.EmployeeWorkedHoursRepository;
import com.examendevbackzeus.empleados.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class EmployeeWorkedHoursServiceTest {
    @Mock
    private EmployeeWorkedHoursRepository employeeWorkedHoursRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeWorkedHoursService employeeWorkedHoursService;

    public EmployeeWorkedHoursServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveDebeGuardarConDatosValidos() {
        SaveEmployeeWorkedHoursRequest request = new SaveEmployeeWorkedHoursRequest();
        request.setEmployee_id(1L);
        // fecha valida
        request.setWorked_date(LocalDate.now());
        // horas validas
        request.setHours(5);

        // simulamos que el empleado si existe
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee(
                1L,
                new Gender(1L, "Hombre"),
                new Job(2L, "QA", BigDecimal.valueOf(250)),
                "Juan",
                "Perez",
                LocalDate.now().minusYears(25)
        )));

        // simulamos que el empleado no tiene registro de horas en esa fecha
        when(employeeWorkedHoursRepository.existsByEmployeeIdAndWorkedDate(1L, LocalDate.now())).thenReturn(false);

        // al guardar retornamos el EmployeeWorkedHours
        when(employeeWorkedHoursRepository.save(any(EmployeeWorkedHours.class))).thenAnswer(inv -> {
            EmployeeWorkedHours emp = inv.getArgument(0);
            emp.setId(1L);
            return emp;
        });

        SaveEmployeeWorkedHoursResponse response = employeeWorkedHoursService.save(request);

        assertTrue(response.isSuccess());
        assertNotNull(response.getId());
    }

    @Test
    void saveNoDebeGuardarSiEmpleadoNoExiste() {
        SaveEmployeeWorkedHoursRequest request = new SaveEmployeeWorkedHoursRequest();
        request.setEmployee_id(99L);
        request.setWorked_date(LocalDate.now());
        request.setHours(5);

        // simulamos que el empleado no existe
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        SaveEmployeeWorkedHoursResponse response = employeeWorkedHoursService.save(request);

        assertFalse(response.isSuccess());
        assertNull(response.getId());
        verify(employeeWorkedHoursRepository, never()).existsByEmployeeIdAndWorkedDate(any(Long.class), any(LocalDate.class));
    }

    @Test
    void saveNoDebeGuardarSiFechaEsFutura() {
        SaveEmployeeWorkedHoursRequest request = new SaveEmployeeWorkedHoursRequest();
        request.setEmployee_id(1L);
        // fecha de 10 dias despues de hoy
        request.setWorked_date(LocalDate.now().plusDays(10));
        request.setHours(5);

        // simulamos que encontramos a un empleado con id 1
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee()));

        SaveEmployeeWorkedHoursResponse response = employeeWorkedHoursService.save(request);

        // no debe guardar porque es fecha futura, falla la validacion de fecha y debe retornar success:false y id:null
        assertFalse(response.isSuccess());
        assertNull(response.getId());
        // Verificamos que existsByEmployeeIdAndWorkedDate nunca fue llamado porque debe fallar la validacion de fecha
        verify(employeeWorkedHoursRepository, never()).existsByEmployeeIdAndWorkedDate(any(Long.class), any(LocalDate.class));
    }

    @Test
    void saveNoDebeGuardarSiYaExisteRegistroEseDia() {
        SaveEmployeeWorkedHoursRequest request = new SaveEmployeeWorkedHoursRequest();
        request.setEmployee_id(1L);
        // fecha valida
        request.setWorked_date(LocalDate.now());
        request.setHours(5);

        // simulamos que el empleado existe
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee()));

        // simulamos que el empleado ya tiene registro de horas en esa fecha
        when(employeeWorkedHoursRepository.existsByEmployeeIdAndWorkedDate(1L, LocalDate.now()))
                .thenReturn(true);

        SaveEmployeeWorkedHoursResponse response = employeeWorkedHoursService.save(request);

        assertFalse(response.isSuccess());
        assertNull(response.getId());
        verify(employeeWorkedHoursRepository, never()).save(any(EmployeeWorkedHours.class));
    }

    @Test
    void saveNoDebeGuardarSiHorasExcedenLimite() {
        SaveEmployeeWorkedHoursRequest request = new SaveEmployeeWorkedHoursRequest();
        request.setEmployee_id(1L);
        // fecha valida
        request.setWorked_date(LocalDate.now());
        request.setHours(100); // límite superado

        // simulamos que el empleado existe
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee()));
        // simulamos que no tiene horas registradas en la fecha, por lo que podemos continuar
        when(employeeWorkedHoursRepository.existsByEmployeeIdAndWorkedDate(1L, LocalDate.now()))
                .thenReturn(false);

        SaveEmployeeWorkedHoursResponse response = employeeWorkedHoursService.save(request);

        assertFalse(response.isSuccess());
        assertNull(response.getId());
        verify(employeeWorkedHoursRepository, never()).save(any(EmployeeWorkedHours.class));
    }


}
