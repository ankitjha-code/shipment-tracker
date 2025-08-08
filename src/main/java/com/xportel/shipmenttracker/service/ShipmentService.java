package com.xportel.shipmenttracker.service;

import com.xportel.shipmenttracker.domain.Shipment;
import com.xportel.shipmenttracker.domain.ShipmentStatus;

import java.util.List;
import java.util.Optional;

public interface ShipmentService {
    Shipment createShipment(String orderId, String origin, String destination);
    Optional<Shipment> getShipmentByOrderId(String orderId);
    List<Shipment> getAllShipments(Optional<ShipmentStatus> status, Optional<String> origin);
    Shipment updateShipmentStatus(String orderId, ShipmentStatus newStatus);
}