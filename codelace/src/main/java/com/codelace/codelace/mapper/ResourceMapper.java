package com.codelace.codelace.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.ResourceRequestDTO;
import com.codelace.codelace.model.dto.ResourceRespondDTO;
import com.codelace.codelace.model.entity.Resource;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ResourceMapper {
	private final ModelMapper modelMapper;

    public Resource convertToEntity(ResourceRequestDTO resourceRequestDTO){
        return modelMapper.map(resourceRequestDTO, Resource.class);
    }

    public ResourceRespondDTO convertToDTO(Resource resource){
        return modelMapper.map(resource, ResourceRespondDTO.class);
    }

    public List<ResourceRespondDTO> convertToListDTO(List<Resource> resource) {
        return resource.stream()
                .map(this::convertToDTO)
                .toList();
    }
}
