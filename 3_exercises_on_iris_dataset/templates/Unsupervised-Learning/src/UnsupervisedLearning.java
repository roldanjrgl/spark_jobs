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
import scala.Tuple2;

import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;

public class UnsupervisedLearning {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("UnsupervisedLearning");
        SparkContext sc = new SparkContext(conf);
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sc);

        JavaRDD<String> data = jsc.textFile(args[0], 1);

        JavaRDD<Vector> parsedData = data.map(s -> {
        	  String[] sarray = s.split(",");
        	  double[] values = new double[sarray.length - 1];
        	  for (int i = 0; i < sarray.length - 1; i++) {
        	    values[i] = Double.parseDouble(sarray[i]);
        	  }
        	  return Vectors.dense(values);
        });
        
        parsedData.cache();

        System.out.println("######################################################");
        System.out.println("Printing JavaRDD<Vector> parseData:");
        System.out.println("------------------------------------------------------");
        for (Vector rowFiltered : parsedData.collect()){
        	System.out.println("rowProcessed: " + rowFiltered);
        }
        System.out.println("######################################################");

     // Cluster the data into two classes using KMeans
        int numClusters = 3;
        int numIterations = 20;
        KMeansModel clusters = KMeans.train(parsedData.rdd(), numClusters, numIterations);
       
        
        System.out.println("Cluster centers:");
        for (Vector center: clusters.clusterCenters()) {
          System.out.println(" " + center);
        }
        double cost = clusters.computeCost(parsedData.rdd());
        System.out.println("Cost: " + cost);

        // Evaluate clustering by computing Within Set Sum of Squared Errors
        double WSSSE = clusters.computeCost(parsedData.rdd());
        System.out.println("Within Set Sum of Squared Errors = " + WSSSE);

        
        JavaRDD<Integer> predictedClasses = clusters.predict(parsedData);
        System.out.println("######################################################");
        System.out.println("Printing JavaRDD<Integer> predictedClass:");
        System.out.println("------------------------------------------------------");
        for (Integer predictedClass : predictedClasses.collect()){
        	System.out.println("rowProcessed: " + predictedClass);
        }
        System.out.println("######################################################");
        
        
    }
}
