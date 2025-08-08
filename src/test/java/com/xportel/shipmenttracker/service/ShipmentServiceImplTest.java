package com.xportel.shipmenttracker.service;

import com.xportel.shipmenttracker.domain.Shipment;
import com.xportel.shipmenttracker.domain.ShipmentStatus;
import com.xportel.shipmenttracker.exception.InvalidStatusTransitionException;
import com.xportel.shipmenttracker.repository.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ShipmentServiceImpl class.
 * This class tests the business logic in isolation from the web layer and database.
 */
@ExtendWith(MockitoExtension.class)
class ShipmentServiceImplTest {

    // @Mock creates a fake version of the ShipmentRepository.
    // We can control its behavior to simulate different scenarios (e.g., shipment found, not found).
    @Mock
    private ShipmentRepository shipmentRepository;

    // @InjectMocks creates a real instance of our service class
    // and injects the mocked dependencies (like shipmentRepository) into it.
    @InjectMocks
    private ShipmentServiceImpl shipmentService;

    /**
     * Test case for a valid status transition (e.g., PENDING -> DISPATCHED).
     * It should succeed and save the updated shipment.
     */
    @Test
    void updateShipmentStatus_shouldSucceed_forValidTransition() {
        // ARRANGE: Set up the test conditions.
        String orderId = "ORD-123";
        Shipment existingShipment = Shipment.create(orderId, "Los Angeles", "New York"); // Initial status is PENDING

        // Tell our fake repository how to behave: when findById is called with this orderId, return our test shipment.
        when(shipmentRepository.findById(orderId)).thenReturn(Optional.of(existingShipment));

        // ACT: Call the method we are testing.
        shipmentService.updateShipmentStatus(orderId, ShipmentStatus.DISPATCHED);

        // ASSERT: Verify the outcome is correct.
        // 1. Check that the shipment's status was correctly updated.
        assertEquals(ShipmentStatus.DISPATCHED, existingShipment.getStatus());
        // 2. Verify that the repository's save() method was called exactly one time.
        verify(shipmentRepository, times(1)).save(existingShipment);
    }

    /**
     * Test case for an invalid status transition (e.g., PENDING -> IN_TRANSIT).
     * It should throw an InvalidStatusTransitionException and not save anything.
     */
    @Test
    void updateShipmentStatus_shouldFail_forInvalidTransition() {
        // ARRANGE
        String orderId = "ORD-456";
        Shipment existingShipment = Shipment.create(orderId, "Chicago", "Miami"); // Status is PENDING
        when(shipmentRepository.findById(orderId)).thenReturn(Optional.of(existingShipment));

        // ACT & ASSERT: We expect this action to throw an exception.
        // The assertThrows block confirms that the specified exception is thrown.
        InvalidStatusTransitionException exception = assertThrows(
                InvalidStatusTransitionException.class, // The expected exception type
                () -> shipmentService.updateShipmentStatus(orderId, ShipmentStatus.IN_TRANSIT) // The code that should throw it
        );

        // Optional: Check that the error message is what we expect.
        assertTrue(exception.getMessage().contains("Invalid status transition from PENDING to IN_TRANSIT"));

        // ASSERT that the save method was NEVER called because the transition was invalid.
        verify(shipmentRepository, never()).save(any(Shipment.class));
    }

    /**
     * Test case for trying to update a shipment that is already delivered.
     * It should throw an exception and not save anything.
     */
    @Test
    void updateShipmentStatus_shouldFail_whenTransitioningFromDelivered() {
        // ARRANGE
        String orderId = "ORD-789";
        Shipment existingShipment = Shipment.create(orderId, "Seattle", "Boston");
        existingShipment.setStatus(ShipmentStatus.DELIVERED); // Manually set status to DELIVERED for the test.
        when(shipmentRepository.findById(orderId)).thenReturn(Optional.of(existingShipment));

        // ACT & ASSERT
        InvalidStatusTransitionException exception = assertThrows(
                InvalidStatusTransitionException.class,
                () -> shipmentService.updateShipmentStatus(orderId, ShipmentStatus.PENDING)
        );

        assertEquals("Cannot update status of a delivered shipment.", exception.getMessage());
        verify(shipmentRepository, never()).save(any(Shipment.class));
    }
}
