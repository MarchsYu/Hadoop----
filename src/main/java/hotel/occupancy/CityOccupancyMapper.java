package hotel.occupancy;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 任务3 Mapper：读取任务2输出，转换为城市维度聚合输入。
 */
public class CityOccupancyMapper extends Mapper<LongWritable, Text, Text, Text> {

    private final Text outputKey = new Text();
    private final Text outputValue = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString().trim();
        if (line.isEmpty()) {
            return;
        }

        // 任务2格式：城市,酒店名称,客房类型,已出租房晚数,客房数量,出租率
        String[] fields = line.split(",", -1);
        if (fields.length < 6) {
            return;
        }

        String city = fields[0].trim();
        String rentedNights = fields[3].trim();
        String roomCount = fields[4].trim();
        if (city.isEmpty() || rentedNights.isEmpty() || roomCount.isEmpty()) {
            return;
        }

        outputKey.set(city);
        outputValue.set(rentedNights + "," + roomCount);
        context.write(outputKey, outputValue);
    }
}
