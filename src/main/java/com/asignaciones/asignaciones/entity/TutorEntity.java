package com.asignaciones.asignaciones.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Tutor
 * Representa un tutor y sus asignaciones a materias
 * 
 * MICROSERVICES PATTERN:
 * - Tutor data comes from usuarios-service (port 8081) with rol=TUTOR
 * - This service only manages MATERIA ASSIGNMENTS
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tutor_asignacion")
public class TutorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    /**
     * Reference to usuario (TUTOR) in usuarios-service
     */
    @Column(name = "usuario_id", nullable = false)
    private Integer usuarioId;
    
    /**
     * Cached data from usuarios-service
     */
    @Column(name = "nombre", length = 200)
    private String nombre;
    
    @Column(name = "correo", length = 200)
    private String correo;
    
    @Column(name = "numero_identificacion", length = 50)
    private String numeroIdentificacion;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    /**
     * Current group (convenience field)
     */
    @Column(name = "grupo_actual_id")
    private Integer grupoActualId;
    
    /**
     * List of materia assignments
     * One tutor can assist with multiple materias
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tutor_id")
    private List<TutorMateriaEntity> listaConTutorMateria = new ArrayList<>();
    
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