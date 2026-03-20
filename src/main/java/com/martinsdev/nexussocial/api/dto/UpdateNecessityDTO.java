package com.martinsdev.nexussocial.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateNecessityDTO(@NotBlank String title,
                                 @NotBlank String description,
                                 @NotNull Integer requiredQuantity,
                                 @NotNull Integer reachedQuantity) {
}
