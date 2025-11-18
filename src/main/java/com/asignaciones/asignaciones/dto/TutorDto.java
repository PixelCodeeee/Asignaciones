package com.asignaciones.asignaciones.dto;

import java.util.List;

import lombok.Data;

@Data
public class TutorDto {
	private Integer id;
	private String nombre; // rellenado por microservicio de tutores cuando esté disponible
	private String correo;
	private String numeroIdentificacion;
	private boolean activo = true;
	private Integer grupoActualId;
	private List<AsignacionTutorDto> asignaciones;

	@Data
	public static class AsignacionTutorDto {
		private Integer id; // id local de asignación
		private Integer materiaId;
		private boolean activo = true;
	}

}
