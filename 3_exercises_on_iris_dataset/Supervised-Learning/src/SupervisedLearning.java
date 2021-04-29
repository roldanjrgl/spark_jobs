import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
//import java.util.Vector;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;


import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.linalg.distributed.RowMatrix;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;
import org.apache.spark.api.java.JavaPairRDD;
import scala.Tuple2;

import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.util.MLUtils;


public class SupervisedLearning {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("SupervisedLearning");
        SparkContext sc = new SparkContext(conf);
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sc);

        JavaRDD<String> data = jsc.textFile(args[0], 1);

        // reference: https://stackoverflow.com/questions/39530775/apache-spark-mllib-getting-labeledpoint-from-data-java
        JavaRDD<LabeledPoint> processedData = data.map(new Function<String, LabeledPoint>(){
        	public LabeledPoint call(String row) {
        		String[] attributes = row.split(",");
        		Double label;
        		if (attributes[attributes.length - 1].contains("setosa")) label = 0.0; 
        		else if (attributes[attributes.length - 1].contains("versicolor")) label = 1.0; 
        		else if (attributes[attributes.length - 1].contains("virginica")) label = 2.0; 
        		else label = 0.0;
        		return new LabeledPoint(label, Vectors.dense(Double.parseDouble(attributes[0]), 
        													 Double.parseDouble(attributes[1]), 
        													 Double.parseDouble(attributes[2]), 
        													 Double.parseDouble(attributes[3])));
        	}
        });
        processedData.cache();
        
        System.out.println("######################################################");
        System.out.println("Printing JavaRDD<LabeledPoint> rowProcessed:");
        System.out.println("------------------------------------------------------");
        // print matrix
        for (LabeledPoint rowProcessed : processedData.collect()){
        	System.out.println("rowProcessed: " + rowProcessed);
        }
        System.out.println("######################################################");

       
        // Split initial RDD into two... [60% training data, 40% testing data].
        JavaRDD<LabeledPoint>[] splits = processedData.randomSplit(new double[] {0.6, 0.4}, 11L);
        JavaRDD<LabeledPoint> training = splits[0].cache();
        JavaRDD<LabeledPoint> test = splits[1];

        
     // Run training algorithm to build the model.
        LogisticRegressionModel model = new LogisticRegressionWithLBFGS()
          .setNumClasses(10)
          .run(training.rdd());

        // Compute raw scores on the test set.
        JavaPairRDD<Object, Object> predictionAndLabels = test.mapToPair(p ->
          new Tuple2<>(model.predict(p.features()), p.label()));

        // Get evaluation metrics.
        MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabels.rdd());
        double accuracy = metrics.accuracy();
        System.out.println("######################################################");
        System.out.println("------------------------------------------------------");
        System.out.println("Accuracy = " + accuracy);
        System.out.println("######################################################");
    }
}
