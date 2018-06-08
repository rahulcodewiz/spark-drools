package com.ts.blog.batch;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;
import com.google.devtools.common.options.OptionsParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.status.api.v1.SimpleDateParam;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.definition.type.FactField;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * @author Rahul
 * @since 1.0
 *
 */
public class CategoryAssignmentApp {

    public static void main(String[] args) {
        SparkSession session = SparkSession.builder().master("local[*]").appName("CategoryAssignmentApp").
                getOrCreate();

        Options options = Options.argsParser.apply(args);

        Dataset<Row> ds = session.read().option("header","true").csv(options.input);
        ds.show();
        ds.javaRDD().mapPartitions(rowIte->{
            //Initlized kie base
            Logger LOGGER = LogManager.getLogger(CategoryAssignmentApp.class);
            KieServices kieServices=KieServices.Factory.get();
            KieContainer kieContainer =kieServices.newKieClasspathContainer();
            KieBaseConfiguration config = kieServices.newKieBaseConfiguration();
            KieBase kieBase =  kieContainer.newKieBase(config);


            //Get Blog type from kie base

            FactType type = kieBase.getFactType("com.ts.blog.kie","Blog");
            if(type == null){
                throw new RuntimeException("Kie Fact not found: com.ts.blog.kie.Blog ");
            }
            List<FactField> blogField = type.getFields();
            //System.out.println(blogField);
            List output = new ArrayList();
            //Iterate through each record and assign category
            while(rowIte.hasNext()){
               Row row =  rowIte.next();
                Object blog = type.newInstance();
                blogField.forEach(field->{
                    try {
                        if( field.getIndex()<row.size()) {
                            String value = row.getString(field.getIndex());
                            Object finalVal = null;
                            if (field.getType().equals(Date.class)) {
                                finalVal = new SimpleDateFormat("MM/dd/yyyy").parse(value);
                            } else if (field.getType().equals(Integer.class)) {
                                finalVal = Integer.valueOf(value);
                            } else {
                                finalVal = value;
                            }
                            if (finalVal != null) {
                                type.set(blog, field.getName(), finalVal);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });

                KieSession kieSession = kieBase.newKieSession();

                kieSession.insert(blog);
                kieSession.setGlobal("LOGGER",LOGGER);
                kieSession.fireAllRules();
                kieSession.dispose();
                output.add(blog);
            }
            return output.iterator();
        }).collect().forEach(e->System.out.println(e));
    }



    public static class Options extends OptionsBase{
        public static OptionsParser parser = OptionsParser.newOptionsParser(Options.class);

        public static Function<String[],Options> argsParser = (args) -> {
            parser.parseAndExitUponError(args);
            return parser.getOptions(Options.class);
        };

        @Option(
                name="help",
                abbrev = 'h',
                help = "Print usage info",
                defaultValue = "true"

        )
        public boolean help;


        @Option(
                name="input",
                abbrev = 'i',
                help = "Print usage info",
                defaultValue = "true"

        )
        public String input;

        @Option(
                name="output",
                abbrev = 'o',
                help = "Print usage info",
                defaultValue = "true"

        )
        public String output;


    }
}
