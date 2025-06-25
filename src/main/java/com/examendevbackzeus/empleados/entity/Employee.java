package com.examendevbackzeus.empleados.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El genero es obligatorio.")
    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;

    @NotNull(message = "El trabajo es obligatorio.")
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @NotBlank(message = "El nombre no puede estar vacio.")
    @NotNull
    private String name;

    @NotBlank(message = "El apellido no puede estar vacio.")
    @NotNull
    private String lastName;

    @Past(message = "La fecha de nacimiento debe ser en el pasado.")
    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    private LocalDate birthdate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
}
