package com.martinsdev.nexussocial.api.dto;

import com.martinsdev.nexussocial.api.model.Necessity;
import com.martinsdev.nexussocial.api.model.enums.NecessityStatus;
import com.martinsdev.nexussocial.api.model.enums.UrgencyLevel;

public record NecessityDTO(Long id,
                           String title,
                           String description,
                           Integer requiredQuantity,
                           Integer reachedQuantity,
                           UrgencyLevel urgencyLevel,
                           NecessityStatus necessityStatus,
                           InstitutionDTO institutionDTO) {

    public NecessityDTO(Necessity necessity){
        this(necessity.getId(), necessity.getTitle(), necessity.getDescription(), necessity.getRequiredQuantity(), necessity.getReachedQuantity(), necessity.getUrgencyLevel(), necessity.getNecessityStatus(), new InstitutionDTO(necessity.getInstitution()));
    }
}
