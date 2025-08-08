package com.xportel.shipmenttracker.repository;

import com.xportel.shipmenttracker.domain.Shipment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages in-memory storage of shipments.
 * This class is thread-safe.
 */
@Repository
public class ShipmentRepository {

    // Using ConcurrentHashMap for thread-safe in-memory storage.
    // The key is the orderId (String) and the value is the Shipment object.
    private final Map<String, Shipment> shipmentStore = new ConcurrentHashMap<>();

    /**
     * Saves or updates a shipment in the store.
     * @param shipment The shipment to save.
     * @return The saved shipment.
     */
    public Shipment save(Shipment shipment) {
        shipmentStore.put(shipment.getOrderId(), shipment);
        return shipment;
    }

    /**
     * Finds a shipment by its orderId.
     * @param orderId The ID of the order to find.
     * @return An Optional containing the shipment if found, otherwise empty.
     */
    public Optional<Shipment> findById(String orderId) {
        // Wraps the result in an Optional to avoid NullPointerExceptions.
        return Optional.ofNullable(shipmentStore.get(orderId));
    }

    /**
     * Returns a list of all shipments.
     * @return A new, unmodifiable list containing all shipments.
     */
    public List<Shipment> findAll() {
        // Return an unmodifiable copy to protect the original data.
        return List.copyOf(shipmentStore.values());
    }

    /**
     * Checks if a shipment with the given orderId exists.
     * @param orderId The ID to check.
     * @return true if it exists, false otherwise.
     */
    public boolean existsById(String orderId) {
        return shipmentStore.containsKey(orderId);
    }
}