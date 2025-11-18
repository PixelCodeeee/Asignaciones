package com.asignaciones.asignaciones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asignaciones.asignaciones.entity.AlumnoEntity;
import com.asignaciones.asignaciones.server.AlumnoServer;

@RestController
@RequestMapping("/api/asignaciones/alumnos")
public class AlumnoAsignacionController {

    @Autowired
    private AlumnoServer service;

    @GetMapping
    public ResponseEntity<List<AlumnoEntity>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/sin-grupo")
    public ResponseEntity<List<AlumnoEntity>> getWithoutGroup() {
        return ResponseEntity.ok(service.findAllWithoutGroup());
    }

    @GetMapping("/con-grupo")
    public ResponseEntity<List<AlumnoEntity>> getWithGroup() {
        return ResponseEntity.ok(service.findAllWithAnyAssignment());
    }

    @PostMapping("/{alumnoId}/asignar/{grupoId}")
    public ResponseEntity<AlumnoEntity> asignarGrupo(@PathVariable Integer alumnoId, @PathVariable Integer grupoId) {
        return ResponseEntity.ok(service.assignGroup(alumnoId, grupoId));
    }

    @PutMapping("/{alumnoId}/asignacion/{asignacionId}/editar/{nuevoGrupoId}")
    public ResponseEntity<?> editarAsignacion(@PathVariable Integer alumnoId, @PathVariable Integer asignacionId,
            @PathVariable Integer nuevoGrupoId) {
        return service.editAssignment(alumnoId, asignacionId, nuevoGrupoId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{alumnoId}/asignacion/{asignacionId}")
    public ResponseEntity<?> bajaLogicaAsignacion(@PathVariable Integer alumnoId, @PathVariable Integer asignacionId) {
        return service.softDeleteAssignment(alumnoId, asignacionId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
