package com.ts.blog.batch;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;
import com.google.devtools.common.options.OptionsParser;
import com.ts.blog.batch.functions.CategoryAssignment;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.function.Function;

/**
 * This Spark App shows how to connect run Kie rules as a part of Spark
 * @author Rahul
 * @since 1.0
 *
 */
public class CategoryAssignmentApp {

    public static void main(String[] args) {
        SparkSession session = SparkSession.builder().master("local[*]").appName("CategoryAssignmentApp").
                getOrCreate();

        //Parse supplied arguments
        Options options = new Options().argsParser.apply(args);


        //Read CSV file using input arguments
        Dataset<Row> ds = session.read().option("header", "true").csv(options.input);
        //See csv content
        ds.show(20);

        //convert dataset to javaRDD and use mapPartition to process each record using kie api and assign Category
        //Use mapPartitions function to process entire partition records, to avoid loading kie rules again and again for each record.

        ds.javaRDD().mapPartitions(new CategoryAssignment("com.ts.blog.kie", "Blog"))
                //Use save instead of collect if you have too many records.
                .collect().forEach(s-> System.out.println(s));
    }


    /**
     * Options class to parse input arguments
     */
    public static class Options extends OptionsBase{
        public static OptionsParser parser = OptionsParser.newOptionsParser(Options.class);

        public static Function<String[],Options> argsParser = (args) -> {
            parser.parseAndExitUponError(args);
            return parser.getOptions(Options.class);
        };

        @Option(
                name="help",
                abbrev = 'h',
                category = "Startup",
                help = "Print usage info",
                defaultValue = "true"

        )
        public boolean help;


        @Option(
                name="input",
                abbrev = 'i',
                category = "Startup",
                help = "input path",
                defaultValue = ""

        )
        public String input;

        @Option(
                name="output",
                abbrev = 'o',
                help = "output path",
                category = "Startup",
                defaultValue = ""

        )
        public String output;


    }
}
