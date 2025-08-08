package com.xportel.shipmenttracker.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateShipmentRequest(
        @NotBlank(message = "orderId is mandatory")
        @Size(min = 3, max = 50, message = "orderId must be between 3 and 50 characters")
        String orderId,

        @NotBlank(message = "origin is mandatory")
        String origin,

        @NotBlank(message = "destination is mandatory")
        String destination
) {}