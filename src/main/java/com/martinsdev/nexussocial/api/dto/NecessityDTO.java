package com.martinsdev.nexussocial.api.dto;

import com.martinsdev.nexussocial.api.model.enums.NecessityStatus;
import com.martinsdev.nexussocial.api.model.enums.UrgencyLevel;

public record NecessityDTO(Long id,
                           String title,
                           String description,
                           Integer requiredQuantity,
                           Integer reachedQuantity,
                           UrgencyLevel urgencyLevel,
                           NecessityStatus necessityStatus) {
}
