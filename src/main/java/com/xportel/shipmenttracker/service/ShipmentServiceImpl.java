package com.xportel.shipmenttracker.service;

import com.xportel.shipmenttracker.domain.Shipment;
import com.xportel.shipmenttracker.domain.ShipmentStatus;
import com.xportel.shipmenttracker.exception.InvalidStatusTransitionException;
import com.xportel.shipmenttracker.exception.ShipmentNotFoundException;
import com.xportel.shipmenttracker.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor // Lombok annotation for clean constructor injection
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    // The core business rule: defines valid status transitions.
    private static final Map<ShipmentStatus, ShipmentStatus> VALID_TRANSITIONS = Map.of(
            ShipmentStatus.PENDING, ShipmentStatus.DISPATCHED,
            ShipmentStatus.DISPATCHED, ShipmentStatus.IN_TRANSIT,
            ShipmentStatus.IN_TRANSIT, ShipmentStatus.DELIVERED
    );

    @Override
    public Shipment createShipment(String orderId, String origin, String destination) {
        if (shipmentRepository.existsById(orderId)) {
            // Using a standard exception here. In a larger app, this might be custom.
            throw new IllegalArgumentException("Shipment with orderId '" + orderId + "' already exists.");
        }
        Shipment newShipment = Shipment.create(orderId, origin, destination);
        return shipmentRepository.save(newShipment);
    }

    @Override
    public Optional<Shipment> getShipmentByOrderId(String orderId) {
        return shipmentRepository.findById(orderId);
    }

    @Override
    public List<Shipment> getAllShipments(Optional<ShipmentStatus> statusFilter, Optional<String> originFilter) {
        Stream<Shipment> shipmentStream = shipmentRepository.findAll().stream();

        if (statusFilter.isPresent()) {
            shipmentStream = shipmentStream.filter(s -> s.getStatus() == statusFilter.get());
        }
        if (originFilter.isPresent() && !originFilter.get().isBlank()) {
            shipmentStream = shipmentStream.filter(s -> s.getOrigin().equalsIgnoreCase(originFilter.get()));
        }
        return shipmentStream.toList();
    }

    @Override
    public Shipment updateShipmentStatus(String orderId, ShipmentStatus newStatus) {
        Shipment shipment = shipmentRepository.findById(orderId)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with orderId '" + orderId + "' not found."));

        validateTransition(shipment.getStatus(), newStatus);

        shipment.setStatus(newStatus);
        shipment.setUpdatedAt(LocalDateTime.now());
        return shipmentRepository.save(shipment);
    }

    private void validateTransition(ShipmentStatus currentStatus, ShipmentStatus newStatus) {
        if (currentStatus == ShipmentStatus.DELIVERED) {
            throw new InvalidStatusTransitionException("Cannot update status of a delivered shipment.");
        }

        ShipmentStatus allowedNextStatus = VALID_TRANSITIONS.get(currentStatus);

        if (allowedNextStatus != newStatus) {
            throw new InvalidStatusTransitionException(
                    "Invalid status transition from " + currentStatus + " to " + newStatus +
                            ". Allowed next status is " + allowedNextStatus + "."
            );
        }
    }
}