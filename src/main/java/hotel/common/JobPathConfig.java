package hotel.common;

/**
 * Hadoop 路径常量统一管理。
 * 为了方便在学校虚拟机中演示，所有默认路径都集中在此类中维护。
 */
public final class JobPathConfig {

    private JobPathConfig() {
    }

    public static final String HDFS_INPUT_BASE = "/hotel/input/raw";
    public static final String HDFS_OUTPUT_BASE = "/hotel/output";

    public static final String HOTEL_INFO_INPUT = HDFS_INPUT_BASE + "/hotel_info.csv";
    public static final String HOTEL_ORDERS_INPUT = HDFS_INPUT_BASE + "/hotel_orders.csv";
    public static final String HOTEL_ROOMS_INPUT = HDFS_INPUT_BASE + "/hotel_rooms.csv";

    public static final String RANKING_OUTPUT = HDFS_OUTPUT_BASE + "/ranking";
    public static final String HOTEL_OCCUPANCY_OUTPUT = HDFS_OUTPUT_BASE + "/hotel_occupancy";
    public static final String CITY_OCCUPANCY_OUTPUT = HDFS_OUTPUT_BASE + "/city_occupancy";

    public static final int STAT_CYCLE_DAYS = 365;
}
