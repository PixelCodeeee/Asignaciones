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

import com.asignaciones.asignaciones.entity.MaestroEntity;
import com.asignaciones.asignaciones.server.MaestroServer;

@RestController
@RequestMapping("/api/asignaciones/maestros")
public class MaestroAsignacionController {

    @Autowired
    private MaestroServer service;

    @GetMapping
    public ResponseEntity<List<MaestroEntity>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/sin-asignacion")
    public ResponseEntity<List<MaestroEntity>> getWithout() {
        return ResponseEntity.ok(service.findAllWithoutAssignment());
    }

    @GetMapping("/con-asignacion")
    public ResponseEntity<List<MaestroEntity>> getWith() {
        return ResponseEntity.ok(service.findAllWithAnyAssignment());
    }

    @PostMapping("/{maestroId}/asignar/{grupoId}/{materiaId}")
    public ResponseEntity<MaestroEntity> asignar(@PathVariable Integer maestroId, @PathVariable Integer grupoId,
            @PathVariable Integer materiaId) {
        return ResponseEntity.ok(service.assign(maestroId, grupoId, materiaId));
    }

    @PutMapping("/{maestroId}/asignacion/{asignacionId}/editar/{nuevoGrupoId}/{nuevaMateriaId}")
    public ResponseEntity<?> editar(@PathVariable Integer maestroId, @PathVariable Integer asignacionId,
            @PathVariable Integer nuevoGrupoId, @PathVariable Integer nuevaMateriaId) {
        return service.editAssignment(maestroId, asignacionId, nuevoGrupoId, nuevaMateriaId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{maestroId}/asignacion/{asignacionId}")
    public ResponseEntity<?> baja(@PathVariable Integer maestroId, @PathVariable Integer asignacionId) {
        return service.softDeleteAssignment(maestroId, asignacionId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
