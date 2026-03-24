package com.martinsdev.nexussocial.api.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateDonationDTO (@NotNull Long id,
                                 @NotNull Integer donatedQuantity){
}
