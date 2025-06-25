package com.examendevbackzeus.empleados.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(precision = 9, scale = 2)
    private BigDecimal salary;

    public Job(){}
    public Job(Long l, String nombre, BigDecimal bigDecimal) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
