package com.asignaciones.asignaciones.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Maestro
 * Representa un profesor y sus asignaciones a grupos y materias
 * 
 * MICROSERVICES PATTERN:
 * - Maestro data comes from usuarios-service (port 8081) with rol=MAESTRO
 * - This service only manages GRUPO-MATERIA ASSIGNMENTS
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "maestro_asignacion")
public class MaestroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    /**
     * Reference to usuario (MAESTRO) in usuarios-service
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
     * Current assignment (convenience fields)
     * Can be derived from active assignments in ListaConMaestroGrupoMateria
     */
    @Column(name = "grupo_actual_id")
    private Integer grupoActualId;
    
    @Column(name = "materia_actual_id")
    private Integer materiaActualId;
    
    /**
     * List of grupo-materia assignments
     * One maestro can teach multiple grupo-materia combinations
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "maestro_id")
    private List<MaestroGrupoMateriaEntity> listaConMaestroGrupoMateria = new ArrayList<>();
    
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