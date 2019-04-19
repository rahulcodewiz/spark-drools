# spark-drools
spark-drools tutorials

The spark drools tutorial contains majorly 2 maven modules, 
the rules-core module defines all the rules and batch modules contains Spark Application to execute the Drools rule.

### **rules-core**- [drools rules](https://github.com/rahulsquid/spark-drools/tree/master/rules-core)
### **batch**- [Spark application using java](https://github.com/rahulsquid/spark-drools/tree/master/batch)

## Following are the major points covered in examples:
*   **How to build kie jar using maven project**-

            rules-core modules
*   **Declare new type using drool**- 

            rules-core/src/main/resources/com.ts.sr.rules.MetaData.drl
*   **Avoid infinite loop execution when any property is updated**- 

            rules/src/main/resources/com.ts.sr.rules.MetaData.drl
*   **Setup a Jboss drools and spark maven project**- 
            batch module contains spark drools implementation
*   **Use Jboss Drools kieclasspathContainer in spark application**- 

            batch/src/main/java/com.ts.blog.batch.CategoryAssignment.java
*   **Load kjar in spark application**- batch module
*   **Use spark transformation to execute rules**- batch module CategoryAssignment execute each rule and assignment Category to each record
*   **Kie Fluent api tutorial**- TODO


## Batch module launcher example

    <batch Zip Home>/bin/launcher.sh --help
    Options category 'Startup':
      --[no]help [-h] (a boolean; default: "true")
        Print usage info
      --input [-i] (a string; default: "")
        input path
      --output [-o] (a string; default: "")
        output path


## You may use input-data directory for test run
