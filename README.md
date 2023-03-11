# Business Rules Engine Jboss Drools Integration with Apache Spark
![Apache Spark](img/spark_java_drools_maven.jpg)


While working on one of the business rules integration project in Spark I realized there are many moving components and it takes a while to setup the whole project structure in Spark, e.g. Project Build setup, Kie Rules & resource structure, Spark App, and Kie App et al. I attempt to share the learnings in this guide.

There are two maven modules, Keeping it separate so rules can be changed independently in runtime. 

- **Rules-core**- [drools rules](https://github.com/rahulsquid/spark-drools/tree/master/rules-core)
- **Batch**- [Spark application](https://github.com/rahulsquid/spark-drools/tree/master/batch)

### Following are the major points covered in examples:

-   **How to build kie jar using maven project**-

            rules-core modules

-   **Declare new type using drool**- 

            rules-core/src/main/resources/com.ts.sr.rules.MetaData.drl

-   **Avoid infinite loop execution when any property is updated**- 

            rules/src/main/resources/com.ts.sr.rules.MetaData.drl
-   **Setup a Jboss drools and spark maven project**- 
            batch module contains spark drools implementation
-   **Use Jboss Drools kieclasspathContainer in spark application**- 

            batch/src/main/java/com.ts.blog.batch.CategoryAssignment.java
-   **Load kjar in spark application**- batch module

-   **Use spark transformation to execute rules**- batch module CategoryAssignment execute each rule and assignment Category to each record

-   **Kie Fluent api tutorial**- TODO


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

## References:

- [Kie Drools](https://docs.jboss.org/drools/release/5.2.0.Final/drools-expert-docs/html/ch05.html)
