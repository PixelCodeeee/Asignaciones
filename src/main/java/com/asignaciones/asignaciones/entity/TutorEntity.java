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
@Table(name = "Tutor_Grupo")
public class TutorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private boolean activo = true;
    // Datos personales
    private String nombre;
    private String correo;
    private String numeroIdentificacion;

    // Conveniencia: grupo actual que atiende (puede derivarse de ListaConTutorMateria/otras reglas)
    private Integer grupoActualId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tutor_id")
    private List<TutorMateriaEntity> ListaConTutorMateria;
}
