package com.codelace.codelace.model.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponseDTO {
    private Long id;
    private String type;
    private BigDecimal price;
    private String description;
}
