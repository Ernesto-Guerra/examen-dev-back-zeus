package com.examendevbackzeus.empleados.dto;

import com.examendevbackzeus.empleados.entity.Gender;
import com.examendevbackzeus.empleados.entity.Job;
import com.fasterxml.jackson.annotation.JsonFormat;

public class EmployeeDetail {
    private Long id;
    private String name;

    private String last_name;

    // Mantenemos el formato indicado en la respuesta del ejercicio
    @JsonFormat(pattern = "dd-MM-yyyy")
    private String birthdate;
    private Job job;
    private Gender gender;

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

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
