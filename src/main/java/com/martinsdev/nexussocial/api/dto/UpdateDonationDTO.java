package com.martinsdev.nexussocial.api.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateDonationDTO (@NotNull Integer donatedQuantity){
}
