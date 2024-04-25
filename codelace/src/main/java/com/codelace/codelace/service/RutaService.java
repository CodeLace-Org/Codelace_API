package com.codelace.codelace.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.mapper.RutaMapper;
import com.codelace.codelace.model.dto.RutaRequestDTO;
import com.codelace.codelace.model.dto.RutaResponseDTO;
import com.codelace.codelace.model.entity.Ruta;
import com.codelace.codelace.repository.RutaRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RutaService {
	private final RutaRepository rutaRepository;
	private final RutaMapper rutaMapper;

	// Method that returns all the routes
	public List<RutaResponseDTO> getAllRutas() {
		List<Ruta> rutas = rutaRepository.findAll();
		return rutaMapper.convertToListDTO(rutas);
	}

	// Method that returns a route by its id
	public RutaResponseDTO getRutaById(Long id) {
		Ruta ruta = rutaRepository.findById(id)
				.orElseThrow(
						() -> new RuntimeException("Ruta not found"));
		return rutaMapper.convertToDTO(ruta);
	}

	// Method that creates a route
	public RutaResponseDTO createRuta(RutaRequestDTO rutaRequestDTO) {
		Ruta ruta = rutaMapper.convertToEntity(rutaRequestDTO);
		rutaRepository.save(ruta);
		return rutaMapper.convertToDTO(ruta);
	}

	// Method that updates a route
	public RutaResponseDTO updateRuta(Long id, RutaRequestDTO rutaRequestDTO) {
		Ruta ruta = rutaRepository.findById(id)
				.orElseThrow(
						() -> new RuntimeException("Ruta not found"));
		ruta.setNombre(rutaRequestDTO.getNombre());
		ruta.setDescripcion(rutaRequestDTO.getDescripcion());
		ruta.setIcono(rutaRequestDTO.getIcono());
		rutaRepository.save(ruta);
		return rutaMapper.convertToDTO(ruta);
	}

	// Method that deletes a route
	public void deleteRuta(Long id) {
		Ruta ruta = rutaRepository.findById(id)
				.orElseThrow(
						() -> new RuntimeException("Ruta not found"));
		rutaRepository.delete(ruta);
	}

}
