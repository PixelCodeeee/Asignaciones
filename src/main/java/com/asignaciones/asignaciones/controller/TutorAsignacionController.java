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

import com.asignaciones.asignaciones.entity.TutorEntity;
import com.asignaciones.asignaciones.server.TutorServer;

@RestController
@RequestMapping("/api/asignaciones/tutores")
public class TutorAsignacionController {

    @Autowired
    private TutorServer service;

    @GetMapping
    public ResponseEntity<List<TutorEntity>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/sin-asignacion")
    public ResponseEntity<List<TutorEntity>> getWithout() {
        return ResponseEntity.ok(service.findAllWithoutAssignment());
    }

    @GetMapping("/con-asignacion")
    public ResponseEntity<List<TutorEntity>> getWith() {
        return ResponseEntity.ok(service.findAllWithAnyAssignment());
    }

    @PostMapping("/{tutorId}/asignar/{materiaId}")
    public ResponseEntity<TutorEntity> asignar(@PathVariable Integer tutorId, @PathVariable Integer materiaId) {
        return ResponseEntity.ok(service.assign(tutorId, materiaId));
    }

    @PutMapping("/{tutorId}/asignacion/{asignacionId}/editar/{nuevaMateriaId}")
    public ResponseEntity<?> editar(@PathVariable Integer tutorId, @PathVariable Integer asignacionId,
            @PathVariable Integer nuevaMateriaId) {
        return service.editAssignment(tutorId, asignacionId, nuevaMateriaId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{tutorId}/asignacion/{asignacionId}")
    public ResponseEntity<?> baja(@PathVariable Integer tutorId, @PathVariable Integer asignacionId) {
        return service.softDeleteAssignment(tutorId, asignacionId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
