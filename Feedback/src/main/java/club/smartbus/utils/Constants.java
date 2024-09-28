package club.smartbus.utils;

/**
 * A utility class that holds constant values used throughout the application.
 *
 * <p>These constants are mainly used for pagination purposes, defining default page size and page number.
 * These values ensure consistent behavior when no specific values are provided by the client.
 */
public class Constants {

    /**
     * The default page size for pagination.
     *
     * <p>This value defines how many items are displayed per page when pagination parameters are not explicitly provided.
     * The default value is set to "30".
     */
    public static final String DEFAULT_PAGE_SIZE = "30";

    /**
     * The default page number for pagination.
     *
     * <p>This value specifies the initial page number when pagination parameters are not explicitly provided.
     * The default value is set to "0", meaning the first page.
     */
    public static final String DEFAULT_PAGE = "0";
}
