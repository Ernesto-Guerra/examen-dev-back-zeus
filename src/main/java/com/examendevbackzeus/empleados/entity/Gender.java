package com.examendevbackzeus.empleados.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "genders")
public class Gender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Gender(){}
    public Gender(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
