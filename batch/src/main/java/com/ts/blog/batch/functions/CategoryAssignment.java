package com.ts.blog.batch.functions;

import com.ts.blog.batch.CategoryAssignmentApp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Row;
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
import java.util.Iterator;
import java.util.List;

/**
 *
 * CategoryAssignment class converts each Row to kieFact (Blog) and assign Category based on the rules.
 *
 * @see KieSession
 * @see
 */
public class CategoryAssignment implements FlatMapFunction<Iterator<Row>, Object> {

    private final String factPkg;
    private final String factClass;

   public CategoryAssignment(String factPkg,String factClass){
        this.factPkg=factPkg;
       this.factClass=factClass;
    }
    @Override
    public Iterator<Object> call(Iterator<Row> rowIte) throws Exception {

        //Initlized kie base
        Logger LOGGER = LogManager.getLogger(CategoryAssignmentApp.class);
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        KieBaseConfiguration config = kieServices.newKieBaseConfiguration();
        KieBase kieBase = kieContainer.newKieBase(config);


        //Get Blog type from kie base

        FactType type = kieBase.getFactType(factPkg, factClass);
        if (type == null) {
            throw new RuntimeException("Kie Fact not found: com.ts.blog.kie.Blog ");
        }
        List<FactField> blogField = type.getFields();
        //System.out.println(blogField);
        List output = new ArrayList();
        //Iterate through each record and assign category
        while (rowIte.hasNext()) {
            Row row = rowIte.next();
            Object blog = type.newInstance();
            /**
             * Populate each field value to object
             */
            blogField.forEach(field -> {
                try {
                    if (field.getIndex() < row.size()) {
                        String value = row.getString(field.getIndex());
                        Object finalVal = null;
                        //Based on value class type process it
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            /**
             * Fetch Kie session from kiebase
             */
            KieSession kieSession = kieBase.newKieSession();
            kieSession.insert(blog);
            kieSession.setGlobal("LOGGER", LOGGER);
            //All set to fire rules
            kieSession.fireAllRules();
            kieSession.dispose();
            output.add(blog);
        }
        return output.iterator();
    }
}

