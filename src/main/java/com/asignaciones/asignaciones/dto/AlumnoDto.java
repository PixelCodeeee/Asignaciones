package com.asignaciones.asignaciones.dto;

import java.util.List;

import lombok.Data;

@Data
public class AlumnoDto {
	private Integer id;
	// nombre puede ser rellenado por el microservicio de alumnos cuando se integre
	private String nombre;
	private String correo;
	private String matricula;
	private boolean activo = true;
	private Integer grupoActualId;
	private List<GrupoAsignacionDto> grupos;

	@Data
	public static class GrupoAsignacionDto {
		private Integer id; // id de la asignación local (AlumnoGrupoEntity.id)
		private Integer grupoId;
		private boolean activo = true;
	}

}
