package com.examendevbackzeus.empleados.service;

import com.examendevbackzeus.empleados.dto.EmployeeDetail;
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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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

    // Pruebas de getEmployeesByJob() en employeeService
    @Test
    void getEmployeesByJobDebeRetornarEmpleadosOrdenados() {
        Long jobId = 2L;

        // simulamos que existe un trabajo con ese id
        when(jobRepository.existsById(jobId)).thenReturn(true);

        // creamos el trabajo y generos de instanciar empleados
        Job job = new Job(jobId, "QA", new BigDecimal("250"));
        Gender gender = new Gender(1L, "Hombre");

        // generamos la respuesta simulada
        List<Employee> empleados = LongStream.rangeClosed(1, 5)
                .mapToObj(i -> new Employee(i, gender, job, "Empleado "+ i, "Apellido "+ i, LocalDate.of(1997, 11, 3)))
                .collect(Collectors.toList());

        // Simulamos empleados con mismo sueldo, distinto id
        when(employeeRepository.findByJobId(jobId)).thenReturn(empleados);

        EmployeeByJobResponse response = employeeService.getEmployeesByJob(jobId);

        // success debe ser true porque el trabajo existe
        assertTrue(response.isSuccess());
        // la lista de empleados puede ser vacia pero no nula
        assertNotNull(response.getEmployees());

        List<EmployeeDetail> detalles = response.getEmployees();

        // Debe estar ordenado por salario descentente (igual para todos asi que no aplica pero la funcionalidad está implementada), luego por ID asc (1, 2, 3, 4, 5)
        assertEquals(1L, detalles.get(0).getId());
        assertEquals(2L, detalles.get(1).getId());
        assertEquals(3L, detalles.get(2).getId());
        assertEquals(4L, detalles.get(3).getId());
        assertEquals(5L, detalles.get(4).getId());

        // la fecha debe venir en formado dd-mm-yyyy (solo validamos uno de los campos)
        assertEquals("03-11-1997", detalles.get(0).getBirthdate());
    }

    @Test
    void getEmployeesByJobDebeFallarSiNoExisteTrabajo() {
        Long jobId = 99L;

        // simulamos que el trabajo no existe
        when(jobRepository.existsById(jobId)).thenReturn(false);

        EmployeeByJobResponse response = employeeService.getEmployeesByJob(jobId);

        assertFalse(response.isSuccess());
        // empleados debe estar vacio porque no existe el trabajo
        assertNull(response.getEmployees());
        // nunca debemos ir a buscar los empleados porque no existe un trabajo con ese id
        verify(employeeRepository, never()).findByJobId(any(Long.class));
    }

    // Pruebas de getEmployeePayment
    @Test
    void getEmployeePaymentDebeFallarSiEmpleadoNoExiste() {
        EmployeePaymentRequest request = new EmployeePaymentRequest();
        request.setEmployee_id(1L);
        request.setStart_date(LocalDate.of(2024, 1, 1));
        request.setEnd_date(LocalDate.of(2024, 1, 31));

        // simulamos que el empleado no existe
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        EmployeePaymentResponse response = employeeService.getEmployeePayment(request);

        // success false, como el empleado no existe aqui nos detenemos
        assertFalse(response.isSuccess());
        // el pago es nulo, ya que no se encontró el empleado
        assertNull(response.getPayment());
        // sumHoursByEmployeeIdAndDateRange() no se debe ejecutar
        verify(employeeRepository, never()).sumHoursByEmployeeIdAndDateRange(any(Long.class), any(LocalDate.class), any(LocalDate.class));

    }

    @Test
    void getEmployeePaymentDebeFallarSiFechaInicioEsDespuesDeFechaFin() {
        EmployeePaymentRequest request = new EmployeePaymentRequest();
        request.setEmployee_id(1L);
        // fecha de incio de 2025
        request.setStart_date(LocalDate.of(2025, 1, 1));
        // fecha de fin de 2024
        request.setEnd_date(LocalDate.of(2024, 1, 1));

        // simulamos que el empleado existe (empleado completo para evitar errores posteriores)
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee(1L, new Gender(1L, "Hombre"), new Job(2L, "QA", new BigDecimal(250)), "Empleado", "Apellido", LocalDate.of(1997, 11, 3))));

        EmployeePaymentResponse response = employeeService.getEmployeePayment(request);

        // success debe ser false porque debe fallar en la validacion de fechas
        assertFalse(response.isSuccess());
        // el pago debe ser null porque no se realizó la busqueda debido al error de validacion en fecha
        assertNull(response.getPayment());
        // sumHoursByEmployeeIdAndDateRange() no se debe ejecutar
        verify(employeeRepository, never()).sumHoursByEmployeeIdAndDateRange(any(Long.class), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getEmployeePaymentDebeCalcularPagoCorrectamente() {
        EmployeePaymentRequest request = new EmployeePaymentRequest();
        request.setEmployee_id(1L);
        request.setStart_date(LocalDate.of(2024, 1, 1));
        request.setEnd_date(LocalDate.of(2024, 1, 31));

        Employee employee = new Employee(1L, new Gender(1L, "Hombre"), new Job(2L, "QA", new BigDecimal(250)), "Empleado", "Apellido", LocalDate.of(1997, 11, 3));
        // simulamos que el empleado existe
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        //simulamos que la suma de horas trabajadas en el rango de fechas es 100 (en este caso, su sueldo es de 250 por hora)
        when(employeeRepository.sumHoursByEmployeeIdAndDateRange(1L, request.getStart_date(), request.getEnd_date()))
                .thenReturn(100);

        EmployeePaymentResponse response = employeeService.getEmployeePayment(request);

        assertTrue(response.isSuccess());
        // multiplicamos el sueldo por las horas trabajadas para saber que resultado esperar
        assertEquals(employee.getJob().getSalary().multiply(new BigDecimal(100)), response.getPayment()); // 250 * 100 = 25000
    }

    @Test
    void getEmployeePaymentDebeRetornarCeroSiNoHayRegistrosDeHorasTrabajadas() {
        EmployeePaymentRequest request = new EmployeePaymentRequest();
        request.setEmployee_id(1L);
        request.setStart_date(LocalDate.of(2024, 1, 1));
        request.setEnd_date(LocalDate.of(2024, 1, 31));

        Employee employee = new Employee(1L, new Gender(1L, "Hombre"), new Job(2L, "QA", new BigDecimal(250)), "Empleado", "Apellido", LocalDate.of(1997, 11, 3));

        // simulamos que el empleado existe
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // simulamos que el empleado no tiene registros de horas trabajadas, por lo tanto la respuesta es null
        when(employeeRepository.sumHoursByEmployeeIdAndDateRange(any(Long.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(null); // sin registros

        EmployeePaymentResponse response = employeeService.getEmployeePayment(request);

        // la respuesta debe ser success true, ya que el empleado existe, aunque no tenga registros de horas trabajadas
        assertTrue(response.isSuccess());
        // el pago debe de ser 0, ya que no tiene horas trabajadas
        assertEquals(BigDecimal.ZERO, response.getPayment());
    }


}
