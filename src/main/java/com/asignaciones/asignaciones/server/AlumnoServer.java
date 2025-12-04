package com.asignaciones.asignaciones.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asignaciones.asignaciones.entity.AlumnoEntity;
import com.asignaciones.asignaciones.entity.AlumnoGrupoEntity;
import com.asignaciones.asignaciones.repository.AlumnosRepository;

@Service
public class AlumnoServer {

	@Autowired
	private AlumnosRepository repo;

	/**
	 * Find all alumnos
	 */
	public List<AlumnoEntity> findAll() {
		return repo.findAll();
	}

	/**
	 * Find alumnos with at least one active assignment
	 */
	public List<AlumnoEntity> findAllWithAnyAssignment() {
		List<AlumnoEntity> all = repo.findAll();
		List<AlumnoEntity> result = new ArrayList<>();
		for (AlumnoEntity a : all) {
			if (a.getListaConAlumnoGrupo() != null && !a.getListaConAlumnoGrupo().isEmpty()) {
				boolean anyActive = a.getListaConAlumnoGrupo().stream()
						.anyMatch(g -> g.getActivo() != null && g.getActivo());
				if (anyActive) {
					result.add(a);
				}
			}
		}
		return result;
	}

	/**
	 * Find alumnos without active group assignment
	 */
	public List<AlumnoEntity> findAllWithoutGroup() {
		List<AlumnoEntity> all = repo.findAll();
		List<AlumnoEntity> result = new ArrayList<>();
		for (AlumnoEntity a : all) {
			boolean hasActive = false;
			if (a.getListaConAlumnoGrupo() != null && !a.getListaConAlumnoGrupo().isEmpty()) {
				hasActive = a.getListaConAlumnoGrupo().stream()
						.anyMatch(g -> g.getActivo() != null && g.getActivo());
			}
			if (!hasActive && a.getActivo() != null && a.getActivo()) {
				result.add(a);
			}
		}
		return result;
	}

	/**
	 * Assign alumno to group
	 * @param alumnoId - ID from usuarios-service (usuario with rol=ALUMNO)
	 * @param grupoId - ID from grupo-service
	 */
	@Transactional
	public AlumnoEntity assignGroup(Integer alumnoId, Integer grupoId) {
		// Find or create alumno assignment record
		AlumnoEntity alumno = repo.findById(alumnoId).orElseGet(() -> {
			AlumnoEntity newAlumno = new AlumnoEntity();
			newAlumno.setUsuarioId(alumnoId);
			newAlumno.setActivo(true);
			// TODO: Fetch and cache data from usuarios-service
			// For now, leave nombre, correo, matricula as null
			// These should be populated by calling usuarios-service API
			return newAlumno;
		});

		// Create new group assignment
		AlumnoGrupoEntity ag = new AlumnoGrupoEntity();
		ag.setGrupoId(grupoId);
		ag.setActivo(true);
		// TODO: Fetch and cache grupo data from grupo-service
		// ag.setNombreGrupo(...);
		// ag.setCodigoGrupo(...);

		// Add assignment to list
		if (alumno.getListaConAlumnoGrupo() == null) {
			alumno.setListaConAlumnoGrupo(new ArrayList<>());
		}
		alumno.getListaConAlumnoGrupo().add(ag);
		
		// Update current group
		alumno.setGrupoActualId(grupoId);
		alumno.setActivo(true);
		
		return repo.save(alumno);
	}

	/**
	 * Edit existing assignment
	 */
	@Transactional
	public Optional<AlumnoEntity> editAssignment(Integer alumnoId, Integer asignacionId, Integer nuevoGrupoId) {
		Optional<AlumnoEntity> op = repo.findById(alumnoId);
		if (op.isEmpty()) {
			return Optional.empty();
		}
		
		AlumnoEntity a = op.get();
		if (a.getListaConAlumnoGrupo() != null) {
			for (AlumnoGrupoEntity ag : a.getListaConAlumnoGrupo()) {
				if (ag.getId() != null && ag.getId().equals(asignacionId) && 
				    ag.getActivo() != null && ag.getActivo()) {
					
					// Update grupo ID
					ag.setGrupoId(nuevoGrupoId);
					// TODO: Update cached grupo data from grupo-service
					
					// Update grupoActual if this was the current assignment
					if (a.getGrupoActualId() != null && a.getGrupoActualId().equals(ag.getGrupoId())) {
						a.setGrupoActualId(nuevoGrupoId);
					}
					
					return Optional.of(repo.save(a));
				}
			}
		}
		return Optional.empty();
	}

	/**
	 * Soft delete assignment
	 */
	@Transactional
	public Optional<AlumnoEntity> softDeleteAssignment(Integer alumnoId, Integer asignacionId) {
		Optional<AlumnoEntity> op = repo.findById(alumnoId);
		if (op.isEmpty()) {
			return Optional.empty();
		}
		
		AlumnoEntity a = op.get();
		if (a.getListaConAlumnoGrupo() != null) {
			for (AlumnoGrupoEntity ag : a.getListaConAlumnoGrupo()) {
				if (ag.getId() != null && ag.getId().equals(asignacionId) && 
				    ag.getActivo() != null && ag.getActivo()) {
					
					// Mark as inactive
					ag.setActivo(false);
					
					// Clear grupoActual if this was the current assignment
					if (a.getGrupoActualId() != null && a.getGrupoActualId().equals(ag.getGrupoId())) {
						a.setGrupoActualId(null);
					}
					
					return Optional.of(repo.save(a));
				}
			}
		}
		return Optional.empty();
	}
}