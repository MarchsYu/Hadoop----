package hotel.occupancy;

import hotel.common.JobPathConfig;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * 任务2 Reducer：计算“城市+酒店+房型”出租率。
 */
public class HotelOccupancyReducer extends Reducer<Text, Text, Text, NullWritable> {

    private final Text outputLine = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        long rentedNights = 0L;
        long roomCount = 0L;

        for (Text value : values) {
            String raw = value.toString();
            if (raw.startsWith("O:")) {
                rentedNights += parseLong(raw.substring(2));
            } else if (raw.startsWith("R:")) {
                roomCount += parseLong(raw.substring(2));
            }
        }

        if (roomCount <= 0L) {
            return;
        }

        long totalNights = roomCount * JobPathConfig.STAT_CYCLE_DAYS;
        BigDecimal occupancyRate = BigDecimal.valueOf(rentedNights)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalNights), 2, RoundingMode.HALF_UP);

        outputLine.set(key.toString() + "," + rentedNights + "," + roomCount + "," + occupancyRate);
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
