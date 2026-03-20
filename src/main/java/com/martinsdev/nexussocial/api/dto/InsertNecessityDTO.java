package com.martinsdev.nexussocial.api.dto;

import com.martinsdev.nexussocial.api.model.enums.NecessityStatus;
import com.martinsdev.nexussocial.api.model.enums.UrgencyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InsertNecessityDTO (@NotNull Long idInstitution,
                                  @NotBlank String title,
                                  @NotBlank String description,
                                  @NotNull Integer requiredQuantity,
                                  @NotNull UrgencyLevel urgencyLevel,
                                  @NotNull NecessityStatus necessityStatus){
}
