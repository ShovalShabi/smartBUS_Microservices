package club.smartbus.utils;

/**
 * Enumeration representing different roles in the system.
 * <p>
 * This enum is used to differentiate between various user roles, such as:
 * </p>
 * <ul>
 *     <li>{@code ADMINISTRATOR} - Represents an administrator user who has access to system management features.</li>
 *     <li>{@code DRIVER} - Represents a driver user who operates vehicles and interacts with relevant system features.</li>
 * </ul>
 */
public enum Role {
    /**
     * Administrator role, having access to management and administrative operations.
     */
    ADMINISTRATOR,

    /**
     * Driver role, used for users who operate vehicles within the system.
     */
    DRIVER
}
