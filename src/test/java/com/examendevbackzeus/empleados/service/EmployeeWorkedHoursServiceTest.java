package com.examendevbackzeus.empleados.service;

import com.examendevbackzeus.empleados.dto.request.GetEmployeeWorkedHoursRequest;
import com.examendevbackzeus.empleados.dto.request.SaveEmployeeWorkedHoursRequest;
import com.examendevbackzeus.empleados.dto.response.GetEmployeeWorkedHoursResponse;
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

    // Pruebas de save() en employeeWorkedHoursService
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

    // Pruebas de getWorkedHoursInRange() en employeeWorkedHoursService
    @Test
    void getWorkedHoursInRangeDebeRetornarTotalDeHorasCorrecto() {
        GetEmployeeWorkedHoursRequest request = new GetEmployeeWorkedHoursRequest();
        request.setEmployee_id(1L);
        request.setStart_date(LocalDate.of(2024, 1, 1));
        request.setEnd_date(LocalDate.of(2024, 1, 31));

        // simulamos que el empleado existe
        when(employeeRepository.existsById(1L)).thenReturn(true);

        // simulamos que el empleado trabajó 40 horas
        when(employeeWorkedHoursRepository.sumHoursByEmployeeAndDateRange(1L, request.getStart_date(), request.getEnd_date()))
                .thenReturn(40);

        GetEmployeeWorkedHoursResponse response = employeeWorkedHoursService.getWorkedHoursInRange(request);

        // success true porque el empleado existe y buscamos los datos
        assertTrue(response.isSuccess());
        // las horas trabajadas deben ser 40
        assertEquals(40, response.getTotal_worked_hours());
    }

    @Test
    void getWorkedHoursInRangeDebeRetornarCeroSiNoHayRegistros() {
        GetEmployeeWorkedHoursRequest request = new GetEmployeeWorkedHoursRequest();
        request.setEmployee_id(1L);
        request.setStart_date(LocalDate.of(2024, 1, 1));
        request.setEnd_date(LocalDate.of(2024, 1, 31));

        // simulamos que el empleado existe
        when(employeeRepository.existsById(1L)).thenReturn(true);

        // simulamos que el empleado no tiene registros de horas trabajadas, por lo que las respuesta es null (el metodo debe convertirlo a 0)
        when(employeeWorkedHoursRepository.sumHoursByEmployeeAndDateRange(1L, request.getStart_date(), request.getEnd_date()))
                .thenReturn(null);

        GetEmployeeWorkedHoursResponse response = employeeWorkedHoursService.getWorkedHoursInRange(request);

        // success true porque el empleado existe y buscamos los datos
        assertTrue(response.isSuccess());
        // las horas trabajadas deben ser 0 porque no tiene ningun registro en el rango de fechas
        assertEquals(0, response.getTotal_worked_hours());
    }

    @Test
    void getWorkedHoursInRangeDebeFallarSiEmpleadoNoExiste() {
        GetEmployeeWorkedHoursRequest request = new GetEmployeeWorkedHoursRequest();
        request.setEmployee_id(99L);
        request.setStart_date(LocalDate.of(2024, 1, 1));
        request.setEnd_date(LocalDate.of(2024, 1, 31));

        // simulamos que el empleado no existe
        when(employeeRepository.existsById(99L)).thenReturn(false);

        GetEmployeeWorkedHoursResponse response = employeeWorkedHoursService.getWorkedHoursInRange(request);

        // success debe ser false porque debe fallar si el empleado no existe
        assertFalse(response.isSuccess());
        // las horas trabajadas son null porque no se hizo la busqueda
        assertNull(response.getTotal_worked_hours());
        //lavidamos que sumHoursByEmployeeAndDateRange() no haya sido llamado,
        verify(employeeWorkedHoursRepository, never()).sumHoursByEmployeeAndDateRange(any(Long.class), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getWorkedHoursInRangeDebeFallarSiFechaInicioEsDespuesDeFechaFin() {
        GetEmployeeWorkedHoursRequest request = new GetEmployeeWorkedHoursRequest();
        request.setEmployee_id(1L);
        // fecha inicio en 2025
        request.setStart_date(LocalDate.of(2025, 1, 1));
        // fecha fin en 2024
        request.setEnd_date(LocalDate.of(2024, 1, 1));

        // simulamos que el empleado existe
        when(employeeRepository.existsById(1L)).thenReturn(true);

        GetEmployeeWorkedHoursResponse response = employeeWorkedHoursService.getWorkedHoursInRange(request);

        // debe fallar porque la fecha inicio es despues de la fecha fin
        assertFalse(response.isSuccess());
        // las horas trabajadas deben ser null falló la verificacion de fechas
        assertNull(response.getTotal_worked_hours());
        // validamos que sumHoursByEmployeeAndDateRange() no haya sido llamado, debe fallar antes
        verify(employeeWorkedHoursRepository, never()).sumHoursByEmployeeAndDateRange(any(Long.class), any(LocalDate.class), any(LocalDate.class));
    }

}
