package hotel.driver;

import hotel.common.JobPathConfig;
import hotel.occupancy.HotelOccupancyMapper;
import hotel.occupancy.HotelOccupancyReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 任务2 Driver：各城市各酒店出租率统计。
 */
public class HotelOccupancyDriver {

    public static void main(String[] args) throws Exception {
        String ordersInputPath = args.length > 0 ? args[0] : JobPathConfig.HOTEL_ORDERS_INPUT;
        String roomsInputPath = args.length > 1 ? args[1] : JobPathConfig.HOTEL_ROOMS_INPUT;
        String outputPath = args.length > 2 ? args[2] : JobPathConfig.HOTEL_OCCUPANCY_OUTPUT;

        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration, "hotel occupancy by city and hotel");
        job.setJarByClass(HotelOccupancyDriver.class);

        job.setMapperClass(HotelOccupancyMapper.class);
        job.setReducerClass(HotelOccupancyReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.addInputPath(job, new Path(ordersInputPath));
        FileInputFormat.addInputPath(job, new Path(roomsInputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        System.exit(job.waitForCompletion(true) ? 0 : 2);
    }
}
