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
@Table(name = "Maestro_Ma_Grupo")
public class MaestroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private boolean activo = true;

    // Datos personales
    private String nombre;
    private String correo;
    private String numeroIdentificacion;

    // Conveniencia: grupo y materia actual (pueden derivarse de ListaConMaestroGrupoMateria)
    private Integer grupoActualId;
    private Integer materiaActualId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "maestro_id")
    private List<MaestroGrupoMateriaEntity> ListaConMaestroGrupoMateria;

}
