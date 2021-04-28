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
//import org.apache.spark.mllib.linalg.Matrix;
//import org.apache.spark.mllib.linalg.Vector;
//import org.apache.spark.mllib.linalg.Vectors;
//import org.apache.spark.mllib.linalg.distributed.RowMatrix;

import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;
import scala.Tuple2;

public class PCA {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("PCA");
        SparkContext sc = new SparkContext(conf);
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sc);

        JavaRDD<String> matrix = jsc.textFile(args[0], 1);
        
        
        JavaRDD<String> rows = matrix.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) {
                return Arrays.stream(s.split(" ")).iterator();
            }
        });
        
        
        // print matrix
        System.out.println("Values of matrix:");
        for (String row : rows.collect()){
        	System.out.println("row: " + row);
        }
        
        /*
        // reference: https://stackoverflow.com/questions/31834825/iterate-through-a-java-rdd-by-row
        JavaRDD <double[]> rowsDoubles = rows.map(new Function<String, double[]>(){
        	@Override
        	public double[] call(String row){
//        		String rowSplitted = row.split("\\t");
        		double[] rowDouble = new double[row.length()];
        		for (int i = 0; i < row.length(); ++i) {
        			rowDouble[i] = (double) Double.parseDouble(row);
        		}
        		return rowDouble;
        	}
        });
        
        System.out.println("######################################################");
        System.out.println("Printing info:");
        System.out.println("######################################################");
        System.out.println("rowsDoublesCount = " + rowsDoubles.count());
        System.out.println("######################################################");
//        List<double[]> listDoubleRows = rowsDoubles.collect();

        
        System.out.println("######################################################");
        System.out.println("Printing matrix:");
        System.out.println("######################################################");
        // attempt to print_1
//        for (double[] test: listDouble) {
////        	System.out.println(test);
//        	for (int i = 0; i < test.length; i++) {
//        		System.out.print(test[i] + " ");
//        	}
//        	System.out.println("");
//        }
        
        // attempt to print_2
//        System.out.println("Values of matrix:");
        for (double[] rowDoubles : rowsDoubles.collect()){
        	System.out.println("row: " + rowDoubles[0]);
        }
        System.out.println("######################################################");

        */
        jsc.stop();
    }
}