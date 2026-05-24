package hotel.mapper;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 入住率分析 Mapper（基础占位类）。
 * 当前仅做最基础的输入解析和示例输出，后续再完善业务逻辑。
 */
public class OccupancyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private static final IntWritable ONE = new IntWritable(1);
    private final Text outputKey = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString().trim();
        if (line.isEmpty() || line.startsWith("日期")) {
            return;
        }

        String[] fields = line.split(",");
        if (fields.length < 5) {
            return;
        }

        // 先按日期输出占位统计结果，后续可扩展为“日期+房型”等多维统计
        String date = fields[0].trim();
        String occupiedFlag = fields[3].trim();

        if ("是".equals(occupiedFlag)) {
            outputKey.set(date);
            context.write(outputKey, ONE);
        }
    }
}
