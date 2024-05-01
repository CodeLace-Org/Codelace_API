package com.codelace.codelace.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscriptionResponseDTO {

    private Long id;
    
    private Long studentId;

    private Long routeId;

}
