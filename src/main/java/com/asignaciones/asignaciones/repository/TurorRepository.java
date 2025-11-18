package com.asignaciones.asignaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asignaciones.asignaciones.entity.TutorEntity;

public interface TurorRepository extends JpaRepository<TutorEntity, Integer> {
    // Additional query methods can be defined here if needed

}
