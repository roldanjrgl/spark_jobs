import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

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


public class FeatureSelection {
    public static void main(String[] args) {
    	System.out.println("Feature-Selection");
    	
        SparkConf conf = new SparkConf().setAppName("PCA");
        SparkContext sc = new SparkContext(conf);
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sc);

        JavaRDD<String> data = jsc.textFile(args[0], 1);

        // reference: https://stackoverflow.com/questions/39530775/apache-spark-mllib-getting-labeledpoint-from-data-java
        JavaRDD<LabeledPoint> processedData = data.map(new Function<String, LabeledPoint>(){
        	public LabeledPoint call(String row) {
        		String[] attributes = row.split(",");
        		Double label;
        		if (attributes[4] == "setosa") label = 0.0; 
        		if (attributes[4] == "versicolor") label = 1.0; 
        		if (attributes[4] == "virginica") label = 2.0; 
        		else label = 0.0;
        		return new LabeledPoint(label, Vectors.dense(Double.parseDouble(attributes[0]), 
        													 Double.parseDouble(attributes[1]), 
        													 Double.parseDouble(attributes[2]), 
        													 Double.parseDouble(attributes[3])));
        	}
        });
        
        
    }
}
