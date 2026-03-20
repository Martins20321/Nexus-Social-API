package com.martinsdev.nexussocial.api.dto;

import jakarta.validation.constraints.NotNull;

public record InsertDonationDTO(@NotNull Long idNecessity,
                                @NotNull Long idDonor,
                                @NotNull Integer donatedQuantity) {
}
