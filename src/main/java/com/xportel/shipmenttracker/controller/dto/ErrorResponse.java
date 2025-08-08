package com.xportel.shipmenttracker.controller.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        int statusCode,
        String message,
        String path,
        LocalDateTime timestamp
) {}