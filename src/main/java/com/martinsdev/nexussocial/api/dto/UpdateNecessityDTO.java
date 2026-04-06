package com.martinsdev.nexussocial.api.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateNecessityDTO(String title,
                                 String description,
                                 Integer requiredQuantity,
                                 @NotNull Integer reachedQuantity) {
}
