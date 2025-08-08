package com.xportel.shipmenttracker.controller.dto;

import com.xportel.shipmenttracker.domain.ShipmentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
        @NotNull(message = "status is mandatory")
        ShipmentStatus status
) {}