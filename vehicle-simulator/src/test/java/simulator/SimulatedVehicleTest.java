package simulator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulatedVehicleTest {

    @Test
    void keepsVehicleIdentityAndStateTogether() {
        VehicleState state = newState();
        SimulatedVehicle vehicle = new SimulatedVehicle(5L, "test-only-placeholder", state);

        assertEquals(5L, vehicle.getVehicleId());
        assertEquals("test-only-placeholder", vehicle.getApiKey());
        assertSame(state, vehicle.getState());
    }

    @Test
    void updatingOneVehicleDoesNotChangeAnotherVehicle() {
        SimulatedVehicle first = new SimulatedVehicle(5L, "test-only-first", newState());
        SimulatedVehicle second = new SimulatedVehicle(6L, "test-only-second", newState());

        for (int i = 0; i < 6; i++) {
            first.getState().update();
        }

        assertNotSame(first.getState(), second.getState());
        assertTrue(first.getState().isIgnitionStatus());
        assertEquals(5.0, first.getState().getSpeed());
        assertFalse(second.getState().isIgnitionStatus());
        assertEquals(0.0, second.getState().getSpeed());
        assertEquals(0, second.getState().getElapsedSeconds());
        assertEquals(37.3947, second.getState().getLatitude());
    }

    private VehicleState newState() {
        return new VehicleState(37.3947, 127.1112, 0.0, 80.0, 30.0, false, 0);
    }
}
