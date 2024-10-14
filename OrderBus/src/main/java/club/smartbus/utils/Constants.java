package club.smartbus.utils;

/**
 * A utility class containing constant values used throughout the application.
 * These constants are used for pagination, WebSocket retries, and service URLs.
 */
public class Constants {

    /**
     * The default number of results to return per page for pagination.
     * This value is typically used when fetching lists of data with pagination.
     */
    public static final String DEFAULT_PAGE_SIZE = "30";

    /**
     * The default page number to start from when fetching paginated data.
     * This value represents the starting page for most paginated requests.
     */
    public static final String DEFAULT_PAGE = "0";

    /**
     * The default value for whether to order a bus. This is used in situations
     * where a boolean flag is needed for ordering bus-related services.
     */
    public static final Boolean DEFAULT_ORDER_BUS = false;

    /**
     * The base URL for the route service.
     * This URL is used by the application to send requests to the route service for bus route-related operations.
     */
    public static final String ROUTES_SERVICE_URL = "http://localhost:10094/route";

    /**
     * The maximum number of retries allowed for sending messages via WebSocket protocol.
     * This is used in situations where the system attempts to resend a WebSocket message if the initial send fails.
     */
    public static final int MAX_RETRIES = 3;
}
