
package com.asignaciones.asignaciones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asignaciones.asignaciones.server.AlumnoServer;

@RestController
@RequestMapping("/api/asignaciones")
public class AsignacionController {

    @Autowired
    private AlumnoServer serviceA;

}
