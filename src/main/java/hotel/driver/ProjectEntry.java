package hotel.driver;

/**
 * 项目统一入口说明类。
 * 课程演示时建议直接运行 scripts/run_all.sh。
 */
public class ProjectEntry {

    public static void main(String[] args) {
        System.out.println("hotel-room-occupancy-analysis");
        System.out.println("请使用以下 Driver 之一执行任务：");
        System.out.println("1) hotel.driver.SalesRankingDriver");
        System.out.println("2) hotel.driver.HotelOccupancyDriver");
        System.out.println("3) hotel.driver.CityOccupancyDriver");
        System.out.println("或直接执行：bash scripts/run_all.sh");
    }
}
