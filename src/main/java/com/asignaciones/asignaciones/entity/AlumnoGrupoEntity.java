package com.asignaciones.asignaciones.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad AlumnoGrupo
 * Representa la asignación de un alumno a un grupo
 * 
 * MICROSERVICES PATTERN:
 * - Grupo comes from grupo-service (port 8083)
 * - References by grupoId only (no JPA relationship)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alumno_grupo")
public class AlumnoGrupoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    /**
     * Reference to Grupo in grupo-service
     */
    @Column(name = "grupo_id", nullable = false)
    private Integer grupoId;
    
    /**
     * Cached grupo info for display
     */
    @Column(name = "nombre_grupo", length = 200)
    private String nombreGrupo;
    
    @Column(name = "codigo_grupo", length = 50)
    private String codigoGrupo;
    
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