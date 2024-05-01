package com.codelace.codelace.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailRequestDTO {
    @NotBlank(message = "Project ID is required")
    private Long projectID;

    @NotBlank(message = "User ID is required")
    private Long userID;
}
