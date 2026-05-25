package hotel.driver;

import hotel.common.JobPathConfig;
import hotel.ranking.SalesRankingMapper;
import hotel.ranking.SalesRankingReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 任务1 Driver：酒店销售数量排名。
 */
public class SalesRankingDriver {

    public static void main(String[] args) throws Exception {
        String inputPath = args.length > 0 ? args[0] : JobPathConfig.HOTEL_INFO_INPUT;
        String outputPath = args.length > 1 ? args[1] : JobPathConfig.RANKING_OUTPUT;

        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration, "hotel sales ranking");
        job.setJarByClass(SalesRankingDriver.class);

        job.setMapperClass(SalesRankingMapper.class);
        job.setReducerClass(SalesRankingReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        System.exit(job.waitForCompletion(true) ? 0 : 2);
    }
}
