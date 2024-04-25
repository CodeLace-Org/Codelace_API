package com.codelace.codelace.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.RutaRequestDTO;
import com.codelace.codelace.model.dto.RutaResponseDTO;
import com.codelace.codelace.model.entity.Ruta;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RutaMapper {
	private final ModelMapper modelMapper;

	public Ruta convertToEntity(RutaRequestDTO rutaRequestDTO) {
		return modelMapper.map(rutaRequestDTO, Ruta.class);
	}

	public RutaResponseDTO convertToDTO(Ruta ruta) {
		return modelMapper.map(ruta, RutaResponseDTO.class);
	}

	public List<RutaResponseDTO> convertToListDTO(List<Ruta> rutas) {
		return rutas.stream()
				.map(this::convertToDTO)
				.toList();
	}
}
