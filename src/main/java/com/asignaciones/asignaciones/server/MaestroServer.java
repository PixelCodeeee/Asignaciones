package com.asignaciones.asignaciones.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asignaciones.asignaciones.entity.MaestroEntity;
import com.asignaciones.asignaciones.entity.MaestroGrupoMateriaEntity;
import com.asignaciones.asignaciones.repository.MaestrosRepository;

@Service
public class MaestroServer {

	@Autowired
	private MaestrosRepository repo;

	public List<MaestroEntity> findAll() {
		return repo.findAll();
	}

	public List<MaestroEntity> findAllWithAnyAssignment() {
		List<MaestroEntity> all = repo.findAll();
		List<MaestroEntity> result = new ArrayList<>();
		for (MaestroEntity m : all) {
			if (m.getListaConMaestroGrupoMateria() != null) {
				boolean anyActive = m.getListaConMaestroGrupoMateria().stream().anyMatch(x -> x.isActivo());
				if (anyActive) result.add(m);
			}
		}
		return result;
	}

	public List<MaestroEntity> findAllWithoutAssignment() {
		List<MaestroEntity> all = repo.findAll();
		List<MaestroEntity> result = new ArrayList<>();
		for (MaestroEntity m : all) {
			boolean hasActive = false;
			if (m.getListaConMaestroGrupoMateria() != null) {
				hasActive = m.getListaConMaestroGrupoMateria().stream().anyMatch(x -> x.isActivo());
			}
			if (!hasActive && m.isActivo()) result.add(m);
		}
		return result;
	}

	public MaestroEntity assign(Integer maestroId, Integer grupoId, Integer materiaId) {
		MaestroEntity maestro = repo.findById(maestroId).orElseGet(() -> {
			MaestroEntity n = new MaestroEntity();
			n.setId(maestroId);
			return n;
		});

	MaestroGrupoMateriaEntity mm = new MaestroGrupoMateriaEntity();
	mm.setGrupoId(grupoId);
	mm.setMateriaId(materiaId);
	mm.setActivo(true);

		if (maestro.getListaConMaestroGrupoMateria() == null) maestro.setListaConMaestroGrupoMateria(new ArrayList<>());
		maestro.getListaConMaestroGrupoMateria().add(mm);
		maestro.setActivo(true);
		// actualizar conveniencia
		maestro.setGrupoActualId(grupoId);
		maestro.setMateriaActualId(materiaId);
		return repo.save(maestro);
	}

	public Optional<MaestroEntity> editAssignment(Integer maestroId, Integer asignacionId, Integer nuevoGrupoId, Integer nuevaMateriaId) {
		Optional<MaestroEntity> op = repo.findById(maestroId);
		if (op.isEmpty()) return Optional.empty();
		MaestroEntity m = op.get();
		if (m.getListaConMaestroGrupoMateria() != null) {
			for (MaestroGrupoMateriaEntity mm : m.getListaConMaestroGrupoMateria()) {
				if (mm.getId() == asignacionId && mm.isActivo()) {
					mm.setGrupoId(nuevoGrupoId);
					mm.setMateriaId(nuevaMateriaId);
					// actualizar conveniencia si corresponde
					if (m.getGrupoActualId() != null && m.getGrupoActualId().equals(mm.getGrupoId())) {
						m.setGrupoActualId(nuevoGrupoId);
					}
					if (m.getMateriaActualId() != null && m.getMateriaActualId().equals(mm.getMateriaId())) {
						m.setMateriaActualId(nuevaMateriaId);
					}
					repo.save(m);
					return Optional.of(m);
				}
			}
		}
		return Optional.empty();
	}

	public Optional<MaestroEntity> softDeleteAssignment(Integer maestroId, Integer asignacionId) {
		Optional<MaestroEntity> op = repo.findById(maestroId);
		if (op.isEmpty()) return Optional.empty();
		MaestroEntity m = op.get();
		if (m.getListaConMaestroGrupoMateria() != null) {
			for (MaestroGrupoMateriaEntity mm : m.getListaConMaestroGrupoMateria()) {
				if (mm.getId() == asignacionId && mm.isActivo()) {
					mm.setActivo(false);
					// si era la asignación actual, limpiar campos de conveniencia
					if (m.getGrupoActualId() != null && m.getGrupoActualId().equals(mm.getGrupoId())) {
						m.setGrupoActualId(null);
					}
					if (m.getMateriaActualId() != null && m.getMateriaActualId().equals(mm.getMateriaId())) {
						m.setMateriaActualId(null);
					}
					repo.save(m);
					return Optional.of(m);
				}
			}
		}
		return Optional.empty();
	}

}
