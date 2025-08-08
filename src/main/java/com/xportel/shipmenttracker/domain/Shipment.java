package com.xportel.shipmenttracker.domain;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Represents a single shipment entity.
 */
@Data
public class Shipment {

    private String orderId;
    private String origin;
    private String destination;
    private ShipmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // A private constructor to be used by our factory method.
    private Shipment(String orderId, String origin, String destination) {
        this.orderId = orderId;
        this.origin = origin;
        this.destination = destination;
        this.status = ShipmentStatus.PENDING; // Default status on creation
        this.createdAt = LocalDateTime.now(); // Set creation timestamp
        this.updatedAt = LocalDateTime.now(); // Set initial update timestamp
    }

    // A static factory method to control object creation
    public static Shipment create(String orderId, String origin, String destination) {
        return new Shipment(orderId, origin, destination);
    }
}
