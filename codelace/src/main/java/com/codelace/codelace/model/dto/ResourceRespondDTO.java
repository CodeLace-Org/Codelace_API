package com.codelace.codelace.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRespondDTO {
    private Long id;
    private String title;
    private String link;
    private Long projectId;
}
