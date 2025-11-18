package com.asignaciones.asignaciones.dto;

import java.util.List;

import lombok.Data;

@Data
public class MaestroDto {
	private Integer id;
	private String nombre; // rellenado por microservicio de maestros cuando esté disponible
	private String correo;
	private String numeroIdentificacion;
	private boolean activo = true;
	private Integer grupoActualId;
	private Integer materiaActualId;
	private List<AsignacionMaestroDto> asignaciones;

	@Data
	public static class AsignacionMaestroDto {
		private Integer id; // id local de asignación
		private Integer grupoId;
		private Integer materiaId;
		private boolean activo = true;
	}

}
