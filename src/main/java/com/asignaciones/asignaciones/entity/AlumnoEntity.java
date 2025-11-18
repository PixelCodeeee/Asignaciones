package com.asignaciones.asignaciones.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Alumno_Grupo")
public class AlumnoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private boolean activo = true;

    // Datos personales
    private String nombre;
    private String correo;
    private String matricula;

    // Grupo actual (conveniencia para consultas rápidas) - puede derivarse de ListaConAlumnoGrupo activa
    private Integer grupoActualId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "alumno_id")
    private List<AlumnoGrupoEntity> ListaConAlumnoGrupo;

}
