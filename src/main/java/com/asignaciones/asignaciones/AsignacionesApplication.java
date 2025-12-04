package com.asignaciones.asignaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Microservicio de Asignaciones
 * Gestiona las asignaciones de:
 * - Alumnos a Grupos
 * - Maestros a Grupos y Materias
 * - Tutores a Materias
 * 
 * Migrado a:
 * - Clever Cloud MySQL (base de datos compartida)
 * - Eureka Service Discovery
 * - API Gateway compatible
 * 
 * @version 2.0 - MySQL & Eureka Integration
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AsignacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsignacionesApplication.class, args);
		
		System.out.println("\n╔════════════════════════════════════════════════════════════╗");
		System.out.println("║                                                            ║");
		System.out.println("║        MICROSERVICIO DE ASIGNACIONES INICIADO              ║");
		System.out.println("║                                                            ║");
		System.out.println("║  Servicio: asignaciones-service                            ║");
		System.out.println("║  Puerto: 8086                                              ║");
		System.out.println("║  Database: Clever Cloud MySQL                              ║");
		System.out.println("║  Eureka: http://localhost:8761                             ║");
		System.out.println("║                                                            ║");
		System.out.println("║  === ALUMNOS → GRUPOS ===                                  ║");
		System.out.println("║  Base: http://localhost:8086/api/asignaciones/alumnos      ║");
		System.out.println("║  • GET    /api/asignaciones/alumnos                        ║");
		System.out.println("║  • GET    /api/asignaciones/alumnos/sin-grupo              ║");
		System.out.println("║  • GET    /api/asignaciones/alumnos/con-grupo              ║");
		System.out.println("║  • POST   /{alumnoId}/asignar/{grupoId}                    ║");
		System.out.println("║  • PUT    /{alumnoId}/asignacion/{id}/editar/{grupoId}     ║");
		System.out.println("║  • DELETE /{alumnoId}/asignacion/{id}                      ║");
		System.out.println("║                                                            ║");
		System.out.println("║  === MAESTROS → GRUPOS + MATERIAS ===                      ║");
		System.out.println("║  Base: http://localhost:8086/api/asignaciones/maestros     ║");
		System.out.println("║  • GET    /api/asignaciones/maestros                       ║");
		System.out.println("║  • GET    /api/asignaciones/maestros/sin-asignacion        ║");
		System.out.println("║  • GET    /api/asignaciones/maestros/con-asignacion        ║");
		System.out.println("║  • POST   /{maestroId}/asignar/{grupoId}/{materiaId}       ║");
		System.out.println("║  • PUT    /{maestroId}/asignacion/{id}/editar/{g}/{m}      ║");
		System.out.println("║  • DELETE /{maestroId}/asignacion/{id}                     ║");
		System.out.println("║                                                            ║");
		System.out.println("║  === TUTORES → MATERIAS ===                                ║");
		System.out.println("║  Base: http://localhost:8086/api/asignaciones/tutores      ║");
		System.out.println("║  • GET    /api/asignaciones/tutores                        ║");
		System.out.println("║  • GET    /api/asignaciones/tutores/sin-asignacion         ║");
		System.out.println("║  • GET    /api/asignaciones/tutores/con-asignacion         ║");
		System.out.println("║  • POST   /{tutorId}/asignar/{materiaId}                   ║");
		System.out.println("║  • PUT    /{tutorId}/asignacion/{id}/editar/{materiaId}    ║");
		System.out.println("║  • DELETE /{tutorId}/asignacion/{id}                       ║");
		System.out.println("║                                                            ║");
		System.out.println("║  Vía API Gateway:                                          ║");
		System.out.println("║  http://localhost:8080/asignaciones-service/api/...        ║");
		System.out.println("║                                                            ║");
		System.out.println("║  📊 18 Endpoints Total - Multi-Service References ✓        ║");
		System.out.println("║  🎓 UTEQ - Sistema de Asistencias 2025                     ║");
		System.out.println("║                                                            ║");
		System.out.println("╚════════════════════════════════════════════════════════════╝\n");
	}

}