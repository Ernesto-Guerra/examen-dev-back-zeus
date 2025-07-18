package com.examendevbackzeus.empleados.repository;

import com.examendevbackzeus.empleados.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Long> {

}
