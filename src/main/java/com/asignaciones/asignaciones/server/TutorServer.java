package com.asignaciones.asignaciones.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asignaciones.asignaciones.entity.TutorEntity;
import com.asignaciones.asignaciones.entity.TutorMateriaEntity;
import com.asignaciones.asignaciones.repository.TurorRepository;

@Service
public class TutorServer {

	@Autowired
	private TurorRepository repo;

	/**
	 * Find all tutors
	 */
	public List<TutorEntity> findAll() {
		return repo.findAll();
	}

	/**
	 * Find tutors with at least one active assignment
	 */
	public List<TutorEntity> findAllWithAnyAssignment() {
		List<TutorEntity> all = repo.findAll();
		List<TutorEntity> result = new ArrayList<>();
		for (TutorEntity t : all) {
			if (t.getListaConTutorMateria() != null && !t.getListaConTutorMateria().isEmpty()) {
				boolean anyActive = t.getListaConTutorMateria().stream()
						.anyMatch(x -> x.getActivo() != null && x.getActivo());
				if (anyActive) {
					result.add(t);
				}
			}
		}
		return result;
	}

	/**
	 * Find tutors without active assignment
	 */
	public List<TutorEntity> findAllWithoutAssignment() {
		List<TutorEntity> all = repo.findAll();
		List<TutorEntity> result = new ArrayList<>();
		for (TutorEntity t : all) {
			boolean hasActive = false;
			if (t.getListaConTutorMateria() != null && !t.getListaConTutorMateria().isEmpty()) {
				hasActive = t.getListaConTutorMateria().stream()
						.anyMatch(x -> x.getActivo() != null && x.getActivo());
			}
			if (!hasActive && t.getActivo() != null && t.getActivo()) {
				result.add(t);
			}
		}
		return result;
	}

	/**
	 * Assign tutor to materia
	 * @param tutorId - ID from usuarios-service (usuario with rol=TUTOR)
	 * @param materiaId - ID from materias-service
	 */
	@Transactional
	public TutorEntity assign(Integer tutorId, Integer materiaId) {
		// Find or create tutor assignment record
		TutorEntity tutor = repo.findById(tutorId).orElseGet(() -> {
			TutorEntity newTutor = new TutorEntity();
			newTutor.setUsuarioId(tutorId);
			newTutor.setActivo(true);
			// TODO: Fetch and cache data from usuarios-service
			// For now, leave nombre, correo, numeroIdentificacion as null
			return newTutor;
		});

		// Create new materia assignment
		TutorMateriaEntity tm = new TutorMateriaEntity();
		tm.setMateriaId(materiaId);
		tm.setActivo(true);
		// TODO: Fetch and cache materia data from materias-service
		// tm.setNombreMateria(...);

		// Add assignment to list
		if (tutor.getListaConTutorMateria() == null) {
			tutor.setListaConTutorMateria(new ArrayList<>());
		}
		tutor.getListaConTutorMateria().add(tm);
		
		tutor.setActivo(true);
		// Note: grupoActualId not set for tutors in this flow
		
		return repo.save(tutor);
	}

	/**
	 * Edit existing assignment
	 */
	@Transactional
	public Optional<TutorEntity> editAssignment(Integer tutorId, Integer asignacionId, Integer nuevaMateriaId) {
		Optional<TutorEntity> op = repo.findById(tutorId);
		if (op.isEmpty()) {
			return Optional.empty();
		}
		
		TutorEntity t = op.get();
		if (t.getListaConTutorMateria() != null) {
			for (TutorMateriaEntity tm : t.getListaConTutorMateria()) {
				if (tm.getId() != null && tm.getId().equals(asignacionId) && 
				    tm.getActivo() != null && tm.getActivo()) {
					
					// Update materia ID
					tm.setMateriaId(nuevaMateriaId);
					// TODO: Update cached materia data from materias-service
					
					return Optional.of(repo.save(t));
				}
			}
		}
		return Optional.empty();
	}

	/**
	 * Soft delete assignment
	 */
	@Transactional
	public Optional<TutorEntity> softDeleteAssignment(Integer tutorId, Integer asignacionId) {
		Optional<TutorEntity> op = repo.findById(tutorId);
		if (op.isEmpty()) {
			return Optional.empty();
		}
		
		TutorEntity t = op.get();
		if (t.getListaConTutorMateria() != null) {
			for (TutorMateriaEntity tm : t.getListaConTutorMateria()) {
				if (tm.getId() != null && tm.getId().equals(asignacionId) && 
				    tm.getActivo() != null && tm.getActivo()) {
					
					// Mark as inactive
					tm.setActivo(false);
					
					return Optional.of(repo.save(t));
				}
			}
		}
		return Optional.empty();
	}
}