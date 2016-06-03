//import javax.naming.Context;
//import javax.security.auth.login.Configuration;
//import javax.xml.soap.Text;
//import java.io.IOException;
//import java.util.StringTokenizer;
//
///**
// * Created by jagesh on 04/23/2016.
// */
//public class WordCountExample
//{
//    public static class TokenizerMapper extends Mapper<Object, Text,Text,IntWritable>
//    {
//        private final static Intwritable one = new Intwritable(1);
//        private Text word = new Text();
//
//        public void map(Object key,Text value,Context context) throws IOException,InterruptedException
//        {
//            StringTokenizer itr = new StringTokenizer(value.toString());
//            while (itr.hasMoreElements())
//            {
//                word.set(itr.nextToken());
//                context.write(word,one);
//            }
//        }
//    }
//    public static class IntSumReducer extends Reducer<Test, IntWritable,Text,IntWritable>
//    {
//        private IntWritable result = new IntWritable();
//        public void reduce(Text key,Iterable<IntWritable> values, Context context) throws IOException,InterruptedException
//        {
//            int sum =0;
//            for (IntWritable val : values)
//            {
//                sum += val.get();
//            }
//            result.set(sum);
//            context.write(key,result);
//        }
//    }
//
//    public static void main(String[] args) throws Exception
//    {
//        Configuration conf = new Configuration();
//        Job job = Job.getInstance();
//
//    }
//}
