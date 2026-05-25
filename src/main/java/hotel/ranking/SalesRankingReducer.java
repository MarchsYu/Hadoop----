package hotel.ranking;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * 任务1 Reducer：输出酒店维度销售数量。
 * 说明：当前输出为“可排序结果”，降序可在导出后再做一次 sort。
 */
public class SalesRankingReducer extends Reducer<Text, IntWritable, Text, NullWritable> {

    private final Text outputLine = new Text();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        int maxSales = 0;
        for (IntWritable value : values) {
            if (value.get() > maxSales) {
                maxSales = value.get();
            }
        }
        outputLine.set(key.toString() + "," + maxSales);
        context.write(outputLine, NullWritable.get());
    }
}
