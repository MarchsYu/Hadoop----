package hotel.occupancy;

import hotel.common.JobPathConfig;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * 任务3 Reducer：计算城市整体出租率。
 */
public class CityOccupancyReducer extends Reducer<Text, Text, Text, NullWritable> {

    private final Text outputLine = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        long totalRentedNights = 0L;
        long totalRoomCount = 0L;

        for (Text value : values) {
            String[] parts = value.toString().split(",", -1);
            if (parts.length < 2) {
                continue;
            }
            totalRentedNights += parseLong(parts[0]);
            totalRoomCount += parseLong(parts[1]);
        }

        if (totalRoomCount <= 0L) {
            return;
        }

        long totalAvailableNights = totalRoomCount * JobPathConfig.STAT_CYCLE_DAYS;
        BigDecimal rate = BigDecimal.valueOf(totalRentedNights)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalAvailableNights), 2, RoundingMode.HALF_UP);

        outputLine.set(key.toString() + "," + totalRentedNights + "," + totalAvailableNights + "," + rate);
        context.write(outputLine, NullWritable.get());
    }

    private long parseLong(String raw) {
        try {
            return Long.parseLong(raw.trim());
        } catch (NumberFormatException ex) {
            return 0L;
        }
    }
}
