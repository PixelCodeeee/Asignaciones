package com.asignaciones.asignaciones.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad TutorMateria
 * Representa la asignación de un tutor a una materia
 * 
 * MICROSERVICES PATTERN:
 * - Materia from materias-service (port 8084)
 * - References by materiaId only
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tutor_materia")
public class TutorMateriaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    /**
     * Reference to Materia in materias-service
     */
    @Column(name = "materia_id", nullable = false)
    private Integer materiaId;
    
    /**
     * Cached materia info for display
     */
    @Column(name = "nombre_materia", length = 200)
    private String nombreMateria;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (activo == null) {
            activo = true;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}