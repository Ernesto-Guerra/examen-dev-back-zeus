package com.examendevbackzeus.empleados.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "employee_worked_hours", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id", "worked_date"})
})
public class EmployeeWorkedHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "worked_date", nullable = false)
    private LocalDate workedDate;

    @Column(nullable = false)
    private int hours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getWorkedDate() {
        return workedDate;
    }

    public void setWorkedDate(LocalDate workedDate) {
        this.workedDate = workedDate;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
}
