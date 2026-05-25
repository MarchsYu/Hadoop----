package hotel.driver;

import hotel.common.JobPathConfig;
import hotel.occupancy.CityOccupancyMapper;
import hotel.occupancy.CityOccupancyReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 任务3 Driver：各城市整体出租率统计。
 */
public class CityOccupancyDriver {

    public static void main(String[] args) throws Exception {
        String inputPath = args.length > 0 ? args[0] : JobPathConfig.HOTEL_OCCUPANCY_OUTPUT;
        String outputPath = args.length > 1 ? args[1] : JobPathConfig.CITY_OCCUPANCY_OUTPUT;

        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration, "city occupancy summary");
        job.setJarByClass(CityOccupancyDriver.class);

        job.setMapperClass(CityOccupancyMapper.class);
        job.setReducerClass(CityOccupancyReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        System.exit(job.waitForCompletion(true) ? 0 : 2);
    }
}
