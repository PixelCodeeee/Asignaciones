package com.asignaciones.asignaciones.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asignaciones.asignaciones.entity.AlumnoEntity;
import com.asignaciones.asignaciones.entity.AlumnoGrupoEntity;
import com.asignaciones.asignaciones.repository.AlumnosRepository;

@Service
public class AlumnoServer {

	@Autowired
	private AlumnosRepository repo;

	public List<AlumnoEntity> findAll() {
		return repo.findAll();
	}

	public List<AlumnoEntity> findAllWithAnyAssignment() {
		List<AlumnoEntity> all = repo.findAll();
		List<AlumnoEntity> result = new ArrayList<>();
		for (AlumnoEntity a : all) {
			if (a.getListaConAlumnoGrupo() != null) {
				boolean anyActive = a.getListaConAlumnoGrupo().stream().anyMatch(g -> g.isActivo());
				if (anyActive) result.add(a);
			}
		}
		return result;
	}

	public List<AlumnoEntity> findAllWithoutGroup() {
		List<AlumnoEntity> all = repo.findAll();
		List<AlumnoEntity> result = new ArrayList<>();
		for (AlumnoEntity a : all) {
			boolean hasActive = false;
			if (a.getListaConAlumnoGrupo() != null) {
				hasActive = a.getListaConAlumnoGrupo().stream().anyMatch(g -> g.isActivo());
			}
			if (!hasActive && a.isActivo()) result.add(a);
		}
		return result;
	}

	public AlumnoEntity assignGroup(Integer alumnoId, Integer grupoId) {
		AlumnoEntity alumno = repo.findById(alumnoId).orElseGet(() -> {
			AlumnoEntity n = new AlumnoEntity();
			n.setId(alumnoId);
			return n;
		});

	AlumnoGrupoEntity ag = new AlumnoGrupoEntity();
	ag.setGrupoId(grupoId);
	ag.setActivo(true);

		if (alumno.getListaConAlumnoGrupo() == null) alumno.setListaConAlumnoGrupo(new ArrayList<>());
		alumno.getListaConAlumnoGrupo().add(ag);
		alumno.setActivo(true);
		// actualizar grupo actual
		alumno.setGrupoActualId(grupoId);
		return repo.save(alumno);
	}

	public Optional<AlumnoEntity> editAssignment(Integer alumnoId, Integer asignacionId, Integer nuevoGrupoId) {
		Optional<AlumnoEntity> op = repo.findById(alumnoId);
		if (op.isEmpty()) return Optional.empty();
		AlumnoEntity a = op.get();
		if (a.getListaConAlumnoGrupo() != null) {
			for (AlumnoGrupoEntity ag : a.getListaConAlumnoGrupo()) {
				if (ag.getId() == asignacionId && ag.isActivo()) {
					ag.setGrupoId(nuevoGrupoId);
					// si la asignación editada coincide con el grupoActual, actualizar
					if (a.getGrupoActualId() != null && a.getGrupoActualId().equals(ag.getGrupoId())) {
						a.setGrupoActualId(nuevoGrupoId);
					}
					repo.save(a);
					return Optional.of(a);
				}
			}
		}
		return Optional.empty();
	}

	public Optional<AlumnoEntity> softDeleteAssignment(Integer alumnoId, Integer asignacionId) {
		Optional<AlumnoEntity> op = repo.findById(alumnoId);
		if (op.isEmpty()) return Optional.empty();
		AlumnoEntity a = op.get();
		if (a.getListaConAlumnoGrupo() != null) {
			for (AlumnoGrupoEntity ag : a.getListaConAlumnoGrupo()) {
				if (ag.getId() == asignacionId && ag.isActivo()) {
					ag.setActivo(false);
					// si era el grupo actual, despejar
					if (a.getGrupoActualId() != null && a.getGrupoActualId().equals(ag.getGrupoId())) {
						a.setGrupoActualId(null);
					}
					repo.save(a);
					return Optional.of(a);
				}
			}
		}
		return Optional.empty();
	}

}
