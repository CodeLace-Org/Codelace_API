package com.codelace.codelace.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutaResponseDTO {
	private Long id;
	private String nombre;
	private String descripcion;
	private String icono;
}
