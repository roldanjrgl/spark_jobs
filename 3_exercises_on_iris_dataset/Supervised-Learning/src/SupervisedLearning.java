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


public class SupervisedLearning {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("SupervisedLearning");
        SparkContext sc = new SparkContext(conf);
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sc);

        JavaRDD<String> data = jsc.textFile(args[0], 1);
        
    }
}
