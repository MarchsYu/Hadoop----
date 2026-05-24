package hotel.driver;

import hotel.mapper.OccupancyMapper;
import hotel.reducer.OccupancyReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 入住率分析任务入口（基础占位类）。
 * 参数说明：args[0] 输入路径，args[1] 输出路径。
 */
public class OccupancyDriver {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("用法: OccupancyDriver <输入路径> <输出路径>");
            System.exit(1);
        }

        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration, "hotel room occupancy analysis");
        job.setJarByClass(OccupancyDriver.class);

        job.setMapperClass(OccupancyMapper.class);
        job.setReducerClass(OccupancyReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 2);
    }
}
