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
import org.apache.spark.mllib.linalg.SingularValueDecomposition;

import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;
import scala.Tuple2;



public class SVD {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("SVD");
        SparkContext sc = new SparkContext(conf);
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sc);

        JavaRDD<String> matrix = jsc.textFile(args[0], 1);
        
        JavaRDD<String> rows = matrix.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) {
                return Arrays.stream(s.split(" ")).iterator();
            }
        });

        // reference: https://stackoverflow.com/questions/31834825/iterate-through-a-java-rdd-by-row
        JavaRDD <double[]> rowsDoubles = rows.map(new Function<String, double[]>(){
        	@Override
        	public double[] call(String row){
        		String[] rowSplitted = row.split(",");
        		double[] rowDouble = new double[rowSplitted.length];
        		for (int i = 0; i < rowSplitted.length; ++i) {
        			rowDouble[i] = (double) Double.parseDouble(rowSplitted[i]);
        		}
        		return rowDouble;
        	}
        });
        
        System.out.println("######################################################");
        System.out.println("Printing info:");
        System.out.println("------------------------------------------------------");
        System.out.println("rowsDoublesCount = " + rowsDoubles.count());
        System.out.println("######################################################");
        
        List<double[]> rowsDoublesCollected = rowsDoubles.collect();
        
//      List<Vector> data = Arrays.asList();
        List<Vector> data = new ArrayList<Vector>();
//      List<Vector> data = new List<Vector>();
        for (double[] rowDoubleCollected: rowsDoublesCollected) {
        	Vector dv = Vectors.dense(rowDoubleCollected);
//      	data.add(Vectors.dense(rowDoubleCollected));
        	data.add(dv);
        }
	  
        JavaRDD<Vector> rowsMatrix = jsc.parallelize(data);
        
        // Create a RowMatrix from JavaRDD<Vector>.
        RowMatrix mat = new RowMatrix(rowsMatrix.rdd());

        
        // Compute the top 5 singular values and corresponding singular vectors.
        SingularValueDecomposition<RowMatrix, Matrix> svd = mat.computeSVD(5, true, 1.0E-9d);
        RowMatrix U = svd.U(); // The U factor is a RowMatrix.
        Vector s = svd.s(); // The singular values are stored in a local dense vector.
        Matrix V = svd.V(); // The V factor is a local dense matrix.
        
        Vector[] collectPartitions = (Vector[]) U.rows().collect();
        System.out.println("U factor is:");
        for (Vector vector : collectPartitions) {
            System.out.println("\t" + vector);
        }
        System.out.println("Singular values are: " + s);
        System.out.println("V factor is:\n" + V);

        jsc.stop();

    }
}
