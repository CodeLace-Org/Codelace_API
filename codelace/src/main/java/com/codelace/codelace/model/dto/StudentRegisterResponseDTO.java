package com.codelace.codelace.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRegisterResponseDTO {
    private Long id;
    private String username;
    private String status;
    private String description;
    private byte[] profile_picture;
    private SubscriptionResponseDTO subscription;
}
