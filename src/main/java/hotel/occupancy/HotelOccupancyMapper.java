package hotel.occupancy;

import hotel.common.CsvCleaner;
import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 任务2 Mapper：同时处理订单与客房数据。
 * - 订单记录输出 O:入住天数
 * - 客房记录输出 R:客房数量
 */
public class HotelOccupancyMapper extends Mapper<LongWritable, Text, Text, Text> {

    private final Text outputKey = new Text();
    private final Text outputValue = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString().trim();
        if (line.isEmpty() || line.startsWith("订单编号") || line.startsWith("酒店所属城市")) {
            return;
        }

        String[] fields = line.split(",", -1);
        if (fields.length == 8) {
            handleOrderRecord(fields, context);
        } else if (fields.length == 4) {
            handleRoomRecord(fields, context);
        }
    }

    private void handleOrderRecord(String[] fields, Context context) throws IOException, InterruptedException {
        String userId = fields[2].trim();
        String city = fields[3].trim();
        String hotelName = fields[4].trim();
        String roomType = fields[5].trim();
        int stayDays = CsvCleaner.parsePositiveInt(fields[7]);

        if (CsvCleaner.isBlank(userId) || CsvCleaner.isBlank(city) || CsvCleaner.isBlank(hotelName)
                || CsvCleaner.isBlank(roomType) || stayDays < 0) {
            return;
        }

        outputKey.set(city + "," + hotelName + "," + roomType);
        outputValue.set("O:" + stayDays);
        context.write(outputKey, outputValue);
    }

    private void handleRoomRecord(String[] fields, Context context) throws IOException, InterruptedException {
        String city = fields[0].trim();
        String hotelName = fields[1].trim();
        String roomType = fields[2].trim();
        int roomCount = CsvCleaner.parsePositiveInt(fields[3]);
        if (CsvCleaner.isBlank(city) || CsvCleaner.isBlank(hotelName) || CsvCleaner.isBlank(roomType) || roomCount < 0) {
            return;
        }

        outputKey.set(city + "," + hotelName + "," + roomType);
        outputValue.set("R:" + roomCount);
        context.write(outputKey, outputValue);
    }
}
