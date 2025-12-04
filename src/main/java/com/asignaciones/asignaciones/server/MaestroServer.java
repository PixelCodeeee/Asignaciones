package com.asignaciones.asignaciones.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asignaciones.asignaciones.entity.MaestroEntity;
import com.asignaciones.asignaciones.entity.MaestroGrupoMateriaEntity;
import com.asignaciones.asignaciones.repository.MaestrosRepository;

@Service
public class MaestroServer {

	@Autowired
	private MaestrosRepository repo;

	/**
	 * Find all maestros
	 */
	public List<MaestroEntity> findAll() {
		return repo.findAll();
	}

	/**
	 * Find maestros with at least one active assignment
	 */
	public List<MaestroEntity> findAllWithAnyAssignment() {
		List<MaestroEntity> all = repo.findAll();
		List<MaestroEntity> result = new ArrayList<>();
		for (MaestroEntity m : all) {
			if (m.getListaConMaestroGrupoMateria() != null && !m.getListaConMaestroGrupoMateria().isEmpty()) {
				boolean anyActive = m.getListaConMaestroGrupoMateria().stream()
						.anyMatch(x -> x.getActivo() != null && x.getActivo());
				if (anyActive) {
					result.add(m);
				}
			}
		}
		return result;
	}

	/**
	 * Find maestros without active assignment
	 */
	public List<MaestroEntity> findAllWithoutAssignment() {
		List<MaestroEntity> all = repo.findAll();
		List<MaestroEntity> result = new ArrayList<>();
		for (MaestroEntity m : all) {
			boolean hasActive = false;
			if (m.getListaConMaestroGrupoMateria() != null && !m.getListaConMaestroGrupoMateria().isEmpty()) {
				hasActive = m.getListaConMaestroGrupoMateria().stream()
						.anyMatch(x -> x.getActivo() != null && x.getActivo());
			}
			if (!hasActive && m.getActivo() != null && m.getActivo()) {
				result.add(m);
			}
		}
		return result;
	}

	/**
	 * Assign maestro to grupo + materia
	 * @param maestroId - ID from usuarios-service (usuario with rol=MAESTRO)
	 * @param grupoId - ID from grupo-service
	 * @param materiaId - ID from materias-service
	 */
	@Transactional
	public MaestroEntity assign(Integer maestroId, Integer grupoId, Integer materiaId) {
		// Find or create maestro assignment record
		MaestroEntity maestro = repo.findById(maestroId).orElseGet(() -> {
			MaestroEntity newMaestro = new MaestroEntity();
			newMaestro.setUsuarioId(maestroId);
			newMaestro.setActivo(true);
			// TODO: Fetch and cache data from usuarios-service
			// For now, leave nombre, correo, numeroIdentificacion as null
			return newMaestro;
		});

		// Create new grupo-materia assignment
		MaestroGrupoMateriaEntity mm = new MaestroGrupoMateriaEntity();
		mm.setGrupoId(grupoId);
		mm.setMateriaId(materiaId);
		mm.setActivo(true);
		// TODO: Fetch and cache data from grupo-service and materias-service
		// mm.setNombreGrupo(...);
		// mm.setCodigoGrupo(...);
		// mm.setNombreMateria(...);

		// Add assignment to list
		if (maestro.getListaConMaestroGrupoMateria() == null) {
			maestro.setListaConMaestroGrupoMateria(new ArrayList<>());
		}
		maestro.getListaConMaestroGrupoMateria().add(mm);
		
		// Update convenience fields
		maestro.setGrupoActualId(grupoId);
		maestro.setMateriaActualId(materiaId);
		maestro.setActivo(true);
		
		return repo.save(maestro);
	}

	/**
	 * Edit existing assignment
	 */
	@Transactional
	public Optional<MaestroEntity> editAssignment(Integer maestroId, Integer asignacionId, 
	                                               Integer nuevoGrupoId, Integer nuevaMateriaId) {
		Optional<MaestroEntity> op = repo.findById(maestroId);
		if (op.isEmpty()) {
			return Optional.empty();
		}
		
		MaestroEntity m = op.get();
		if (m.getListaConMaestroGrupoMateria() != null) {
			for (MaestroGrupoMateriaEntity mm : m.getListaConMaestroGrupoMateria()) {
				if (mm.getId() != null && mm.getId().equals(asignacionId) && 
				    mm.getActivo() != null && mm.getActivo()) {
					
					// Update IDs
					mm.setGrupoId(nuevoGrupoId);
					mm.setMateriaId(nuevaMateriaId);
					// TODO: Update cached data from services
					
					// Update convenience fields if this was the current assignment
					if (m.getGrupoActualId() != null && m.getGrupoActualId().equals(mm.getGrupoId())) {
						m.setGrupoActualId(nuevoGrupoId);
					}
					if (m.getMateriaActualId() != null && m.getMateriaActualId().equals(mm.getMateriaId())) {
						m.setMateriaActualId(nuevaMateriaId);
					}
					
					return Optional.of(repo.save(m));
				}
			}
		}
		return Optional.empty();
	}

	/**
	 * Soft delete assignment
	 */
	@Transactional
	public Optional<MaestroEntity> softDeleteAssignment(Integer maestroId, Integer asignacionId) {
		Optional<MaestroEntity> op = repo.findById(maestroId);
		if (op.isEmpty()) {
			return Optional.empty();
		}
		
		MaestroEntity m = op.get();
		if (m.getListaConMaestroGrupoMateria() != null) {
			for (MaestroGrupoMateriaEntity mm : m.getListaConMaestroGrupoMateria()) {
				if (mm.getId() != null && mm.getId().equals(asignacionId) && 
				    mm.getActivo() != null && mm.getActivo()) {
					
					// Mark as inactive
					mm.setActivo(false);
					
					// Clear convenience fields if this was the current assignment
					if (m.getGrupoActualId() != null && m.getGrupoActualId().equals(mm.getGrupoId())) {
						m.setGrupoActualId(null);
					}
					if (m.getMateriaActualId() != null && m.getMateriaActualId().equals(mm.getMateriaId())) {
						m.setMateriaActualId(null);
					}
					
					return Optional.of(repo.save(m));
				}
			}
		}
		return Optional.empty();
	}
}