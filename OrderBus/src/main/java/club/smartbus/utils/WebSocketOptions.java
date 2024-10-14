package club.smartbus.utils;

/**
 * Enum representing the different WebSocket message options for bus-related operations.
 * These options define the types of actions that can be communicated between clients (passengers and drivers) via WebSocket.
 */
public enum WebSocketOptions {
    /**
     * Represents the option for a passenger to request a bus.
     * This option is used when a passenger sends a WebSocket message to request a bus to pick them up.
     */
    REQUEST_BUS,

    /**
     * Represents the option for canceling a ride.
     * This option is used when either a passenger or driver cancels an active ride request.
     */
    CANCELING_RIDE,

    /**
     * Represents the option for a driver to accept a ride request.
     * This option is used when a driver confirms that they will pick up the passenger at the requested station.
     */
    ACCEPTING_RIDE,

    /**
     * Represents the option for updating the driver's current route step.
     * This option is used when a driver sends an update about their current position or progress along the route.
     */
    UPDATE_ROUTE_STEP
}
