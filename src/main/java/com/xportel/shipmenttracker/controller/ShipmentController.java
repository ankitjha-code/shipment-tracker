package com.xportel.shipmenttracker.controller;

import com.xportel.shipmenttracker.controller.dto.CreateShipmentRequest;
import com.xportel.shipmenttracker.controller.dto.UpdateStatusRequest;
import com.xportel.shipmenttracker.domain.Shipment;
import com.xportel.shipmenttracker.domain.ShipmentStatus;
import com.xportel.shipmenttracker.exception.ShipmentNotFoundException;
import com.xportel.shipmenttracker.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/shipments") // Base path for all endpoints in this controller
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping
    public ResponseEntity<Shipment> createShipment(@Valid @RequestBody CreateShipmentRequest request) {
        Shipment createdShipment = shipmentService.createShipment(
                request.orderId(),
                request.origin(),
                request.destination()
        );
        return new ResponseEntity<>(createdShipment, HttpStatus.CREATED); // Return 201 Created
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Shipment> getShipmentByOrderId(@PathVariable String orderId) {
        return shipmentService.getShipmentByOrderId(orderId)
                .map(ResponseEntity::ok) // If found, wrap in ResponseEntity with 200 OK
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found with orderId: " + orderId));
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<Shipment> updateShipmentStatus(@PathVariable String orderId, @Valid @RequestBody UpdateStatusRequest request) {
        Shipment updatedShipment = shipmentService.updateShipmentStatus(orderId, request.status());
        return ResponseEntity.ok(updatedShipment);
    }

    @GetMapping
    public ResponseEntity<List<Shipment>> getAllShipments(
            @RequestParam(required = false) ShipmentStatus status,
            @RequestParam(required = false) String origin
    ) {
        List<Shipment> shipments = shipmentService.getAllShipments(
                Optional.ofNullable(status),
                Optional.ofNullable(origin)
        );
        return ResponseEntity.ok(shipments);
    }
}