package hotel.reducer;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * 入住率分析 Reducer（基础占位类）。
 * 当前仅做简单累加，后续将补充出租率计算逻辑。
 */
public class OccupancyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    private final IntWritable outputValue = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable value : values) {
            sum += value.get();
        }
        outputValue.set(sum);
        context.write(key, outputValue);
    }
}
