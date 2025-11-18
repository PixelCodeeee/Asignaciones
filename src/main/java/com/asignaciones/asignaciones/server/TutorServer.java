package com.asignaciones.asignaciones.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asignaciones.asignaciones.entity.TutorEntity;
import com.asignaciones.asignaciones.entity.TutorMateriaEntity;
import com.asignaciones.asignaciones.repository.TurorRepository;

@Service
public class TutorServer {

	@Autowired
	private TurorRepository repo;

	public List<TutorEntity> findAll() {
		return repo.findAll();
	}

	public List<TutorEntity> findAllWithAnyAssignment() {
		List<TutorEntity> all = repo.findAll();
		List<TutorEntity> result = new ArrayList<>();
		for (TutorEntity t : all) {
			if (t.getListaConTutorMateria() != null) {
				boolean anyActive = t.getListaConTutorMateria().stream().anyMatch(x -> x.isActivo());
				if (anyActive) result.add(t);
			}
		}
		return result;
	}

	public List<TutorEntity> findAllWithoutAssignment() {
		List<TutorEntity> all = repo.findAll();
		List<TutorEntity> result = new ArrayList<>();
		for (TutorEntity t : all) {
			boolean hasActive = false;
			if (t.getListaConTutorMateria() != null) {
				hasActive = t.getListaConTutorMateria().stream().anyMatch(x -> x.isActivo());
			}
			if (!hasActive && t.isActivo()) result.add(t);
		}
		return result;
	}

	public TutorEntity assign(Integer tutorId, Integer materiaId) {
		TutorEntity tutor = repo.findById(tutorId).orElseGet(() -> {
			TutorEntity n = new TutorEntity();
			n.setId(tutorId);
			return n;
		});

	TutorMateriaEntity tm = new TutorMateriaEntity();
	tm.setMateriaId(materiaId);
	tm.setActivo(true);

		if (tutor.getListaConTutorMateria() == null) tutor.setListaConTutorMateria(new ArrayList<>());
		tutor.getListaConTutorMateria().add(tm);
		tutor.setActivo(true);
		// actualizar conveniencia
		tutor.setGrupoActualId(null); // por ahora no asignamos grupo directo aquí
		return repo.save(tutor);
	}

	public Optional<TutorEntity> editAssignment(Integer tutorId, Integer asignacionId, Integer nuevaMateriaId) {
		Optional<TutorEntity> op = repo.findById(tutorId);
		if (op.isEmpty()) return Optional.empty();
		TutorEntity t = op.get();
		if (t.getListaConTutorMateria() != null) {
			for (TutorMateriaEntity tm : t.getListaConTutorMateria()) {
				if (tm.getId() == asignacionId && tm.isActivo()) {
					tm.setMateriaId(nuevaMateriaId);
					repo.save(t);
					return Optional.of(t);
				}
			}
		}
		return Optional.empty();
	}

	public Optional<TutorEntity> softDeleteAssignment(Integer tutorId, Integer asignacionId) {
		Optional<TutorEntity> op = repo.findById(tutorId);
		if (op.isEmpty()) return Optional.empty();
		TutorEntity t = op.get();
		if (t.getListaConTutorMateria() != null) {
			for (TutorMateriaEntity tm : t.getListaConTutorMateria()) {
				if (tm.getId() == asignacionId && tm.isActivo()) {
					tm.setActivo(false);
					repo.save(t);
					return Optional.of(t);
				}
			}
		}
		return Optional.empty();
	}

}
