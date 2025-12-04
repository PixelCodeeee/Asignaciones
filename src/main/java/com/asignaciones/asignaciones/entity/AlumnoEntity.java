package com.asignaciones.asignaciones.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Alumno
 * Representa un estudiante y sus asignaciones a grupos
 * 
 * MICROSERVICES PATTERN:
 * - Alumno data comes from usuarios-service (port 8081)
 * - This service only manages GROUP ASSIGNMENTS
 * - References usuarios-service by alumnoId (not stored here, passed in requests)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alumno_asignacion")
public class AlumnoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    /**
     * Reference to usuario (ALUMNO) in usuarios-service
     * This is the usuario.id from usuarios-service
     */
    @Column(name = "usuario_id", nullable = false)
    private Integer usuarioId;
    
    /**
     * Cached data from usuarios-service for quick display
     */
    @Column(name = "nombre", length = 200)
    private String nombre;
    
    @Column(name = "correo", length = 200)
    private String correo;
    
    @Column(name = "matricula", length = 50)
    private String matricula;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    /**
     * Current group (convenience field)
     * Can be derived from active assignments in ListaConAlumnoGrupo
     */
    @Column(name = "grupo_actual_id")
    private Integer grupoActualId;
    
    /**
     * List of group assignments
     * One alumno can have multiple group assignments (history)
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "alumno_id")
    private List<AlumnoGrupoEntity> listaConAlumnoGrupo = new ArrayList<>();
    
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