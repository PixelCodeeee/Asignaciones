package com.asignaciones.asignaciones.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad MaestroGrupoMateria
 * Representa la asignación de un maestro a enseñar una materia en un grupo específico
 * 
 * MICROSERVICES PATTERN:
 * - Grupo from grupo-service (port 8083)
 * - Materia from materias-service (port 8084)
 * - References by IDs only
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "maestro_grupo_materia")
public class MaestroGrupoMateriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    /**
     * Reference to Grupo in grupo-service
     */
    @Column(name = "grupo_id", nullable = false)
    private Integer grupoId;
    
    /**
     * Reference to Materia in materias-service
     */
    @Column(name = "materia_id", nullable = false)
    private Integer materiaId;
    
    /**
     * Cached info for display
     */
    @Column(name = "nombre_grupo", length = 200)
    private String nombreGrupo;
    
    @Column(name = "codigo_grupo", length = 50)
    private String codigoGrupo;
    
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