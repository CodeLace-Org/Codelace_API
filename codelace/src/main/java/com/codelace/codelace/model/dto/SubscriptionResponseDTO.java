package com.codelace.codelace.model.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponseDTO {
    
    private Long id;

    private Long planId;

    private String planType;

    private LocalDate beginDate;

    private LocalDate endDate;

    private Boolean active;
}
