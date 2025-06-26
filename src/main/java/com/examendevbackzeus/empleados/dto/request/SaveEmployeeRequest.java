package com.examendevbackzeus.empleados.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public class SaveEmployeeRequest {
    @NotNull(message = "El id de genero es obligatorio.")
    @Schema(description = "ID del genero del empleado", example = "1")
    @JsonProperty("gender_id")
    private Long gender_id;

    @NotNull(message = "El id del trabajo es obligatorio.")
    @Schema(description = "ID del trabajo del empleado", example = "1")
    @JsonProperty("job_id")
    private Long job_id;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Schema(description = "Nombre del empleado", example = "Juan")
    @NotNull
    private String name;

    @NotBlank(message = "El apellido no puede estar vacío.")
    @Schema(description = "Apellido del empleado", example = "Perez")
    @NotNull
    @JsonProperty("last_name")
    private String lastName;

    @Past(message = "La fecha de nacimiento debe ser en el pasado.")
    @Schema(description = "Fecha de nacimiento del empleado", example = "1997-11-03")
    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    private LocalDate birthdate;

    public Long getGender_id() {
        return gender_id;
    }

    public void setGender_id(Long gender_id) {
        this.gender_id = gender_id;
    }

    public Long getJob_id() {
        return job_id;
    }

    public void setJob_id(Long job_id) {
        this.job_id = job_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
