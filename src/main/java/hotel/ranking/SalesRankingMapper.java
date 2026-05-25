package hotel.ranking;

import hotel.common.CsvCleaner;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 任务1 Mapper：清洗酒店销售信息。
 * 输入：hotel_info.csv
 */
public class SalesRankingMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private final Text outputKey = new Text();
    private final IntWritable outputValue = new IntWritable();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString().trim();
        if (line.isEmpty() || line.startsWith("城市")) {
            return;
        }

        String[] fields = line.split(",", -1);
        if (fields.length < 8) {
            return;
        }

        String city = fields[0].trim();
        String hotelName = fields[1].trim();
        String hotelType = fields[2].trim();
        int salesCount = CsvCleaner.parseSalesCount(fields[7]);
        if (CsvCleaner.isBlank(city) || CsvCleaner.isBlank(hotelName) || CsvCleaner.isBlank(hotelType) || salesCount < 0) {
            return;
        }

        outputKey.set(city + "," + hotelName + "," + hotelType);
        outputValue.set(salesCount);
        context.write(outputKey, outputValue);
    }
}
