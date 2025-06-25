package com.examendevbackzeus.empleados.service;

import com.examendevbackzeus.empleados.dto.request.SaveEmployeeRequest;
import com.examendevbackzeus.empleados.dto.response.SaveEmployeeResponse;
import com.examendevbackzeus.empleados.entity.Employee;
import com.examendevbackzeus.empleados.entity.Gender;
import com.examendevbackzeus.empleados.entity.Job;
import com.examendevbackzeus.empleados.repository.EmployeeRepository;
import com.examendevbackzeus.empleados.repository.GenderRepository;
import com.examendevbackzeus.empleados.repository.JobRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private GenderRepository genderRepository;

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private EmployeeService employeeService;

    public EmployeeServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    // Pruebas de save() en employeeService
    @Test
    void saveDebeGuardarConDatosValidos() {
        SaveEmployeeRequest request = new SaveEmployeeRequest();
        request.setName("Juan");
        request.setLastName("Perez");
        request.setBirthdate(LocalDate.of(1990, 1, 1));
        request.setGender_id(1L);
        request.setJob_id(2L);

        // simulamos que el empleado no existe
        when(employeeRepository.existsByNameIgnoreCaseAndLastNameIgnoreCase("Juan", "Perez"))
                .thenReturn(false);
        // simulamos que el genero y el trabajo ya existen
        when(genderRepository.findById(1L)).thenReturn(Optional.of(new Gender(1L, "Hombre")));
        when(jobRepository.findById(2L)).thenReturn(Optional.of(new Job(2L, "QA", BigDecimal.valueOf(250))));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> {
            Employee emp = inv.getArgument(0);
            emp.setId(1L);
            return emp;
        });

        SaveEmployeeResponse response = employeeService.save(request);

        // success debe ser true porque enviamos informacion correcta
        assertTrue(response.isSuccess());
        assertEquals(1L, response.getId());
    }

    @Test
    void saveNoDebeGuardarEmpleadoDuplicado() {
        SaveEmployeeRequest request = new SaveEmployeeRequest();
        request.setName("Juan");
        request.setLastName("Perez");
        request.setBirthdate(LocalDate.of(2000, 1, 1));
        request.setGender_id(1L);
        request.setJob_id(1L);

        // simulamos que el empleado ya existe
        when(employeeRepository.existsByNameIgnoreCaseAndLastNameIgnoreCase("Juan", "Perez"))
                .thenReturn(true);

        SaveEmployeeResponse response = employeeService.save(request);

        // success debe ser false porque no debe guardar el empleado duplicado
        assertFalse(response.isSuccess());
        // id debe ser null porque no se guardó un empleado nuevo
        assertNull(response.getId());
        // no debe llegar a validar el genero porque debe retornar el response antes
        verify(genderRepository, never()).findById(any(Long.class));
    }

    @Test
    void saveNoGuardaMenoresDeEdad() {
        SaveEmployeeRequest request = new SaveEmployeeRequest();
        request.setName("Juan");
        request.setLastName("Perez");
        request.setBirthdate(LocalDate.now().minusYears(15));
        request.setGender_id(1L);
        request.setJob_id(2L);

        // simulamos que el empleado no existe aun
        when(employeeRepository.existsByNameIgnoreCaseAndLastNameIgnoreCase("Juan", "Perez"))
                .thenReturn(false);

        SaveEmployeeResponse response = employeeService.save(request);

        // success debe ser false porque no debe guardar menores de edad
        assertFalse(response.isSuccess());
        // id debe ser null porque no guarda nada
        assertNull(response.getId());
        // nunca se debe intentar buscar el genero porque debe fallar la validacion de edad
        verify(genderRepository, never()).findById(any(Long.class));
    }

    @Test
    void saveNoGuardaSiNoExisteElGenero() {
        SaveEmployeeRequest request = new SaveEmployeeRequest();
        request.setName("Juan");
        request.setLastName("Perez");
        request.setBirthdate(LocalDate.of(1990, 1, 1));
        request.setGender_id(99L);

        // simulamos que no hay empleado duplicado
        when(employeeRepository.existsByNameIgnoreCaseAndLastNameIgnoreCase("Juan", "Perez"))
                .thenReturn(false);

        // simulamos que el genero con id 99 no existe
        when(genderRepository.findById(99L)).thenReturn(Optional.empty());

        SaveEmployeeResponse response = employeeService.save(request);

        // success debe ser false porque no debe guardar al empleado
        assertFalse(response.isSuccess());
        assertNull(response.getId());
        // la busqueda de genero debe haberse ejecutado al menos una vez
        verify(genderRepository, atLeast(1)).findById(any(Long.class));
    }

    @Test
    void saveNoGuardaSiNoExisteTrabajo() {
        SaveEmployeeRequest request = new SaveEmployeeRequest();
        request.setName("Juan");
        request.setLastName("Perez");
        request.setBirthdate(LocalDate.of(1990, 1, 1));
        request.setGender_id(1L);
        request.setJob_id(99L);

        // simulamos que no existe el empleado aun
        when(employeeRepository.existsByNameIgnoreCaseAndLastNameIgnoreCase("Juan", "Perez"))
                .thenReturn(false);
        // simulamos que el genero si existe
        when(genderRepository.findById(1L)).thenReturn(Optional.of(new Gender(1L, "Hombre")));
        //simulamos que el trabajo con id 99 no existe
        when(jobRepository.findById(99L)).thenReturn(Optional.empty());

        SaveEmployeeResponse response = employeeService.save(request);

        assertFalse(response.isSuccess());
        assertNull(response.getId());
        verify(jobRepository, atLeast(1)).findById(any(Long.class));
    }
}
