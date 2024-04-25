package com.codelace.codelace.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RutaRequestDTO {
	@NotBlank(message = "El nombre de la ruta no puede estar vacio")
	private String nombre;

	@NotBlank(message = "La descripcion de la ruta no puede estar vacio")
	private String descripcion;

	@NotBlank(message = "El icono(svg/XML) de la ruta no puede estar vacio")
	private String icono;
}
