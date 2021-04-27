import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.linalg.distributed.RowMatrix;
import org.apache.spark.api.java.function.*;

public class PCA {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: PCA <file>");
            System.exit(1);
        }

        SparkConf conf = new SparkConf().setAppName("PCA");
        SparkContext sc = new SparkContext(conf);
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sc);

        JavaRDD<String> matrix = jsc.textFile(args[0], 1);
        

        JavaRDD<String> words = matrix.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) {
                return Arrays.stream(s.split(" ")).iterator();
            }
        });


        /* 
        List<Vector> data = Arrays.asList(Vectors.sparse(5, new int[] { 1, 3 }, new double[] { 1.0, 7.0 }),
                Vectors.dense(2.0, 0.0, 3.0, 4.0, 5.0), Vectors.dense(4.0, 0.0, 0.0, 6.0, 7.0));

        JavaRDD<Vector> rows = jsc.parallelize(data);

        // Create a RowMatrix from JavaRDD<Vector>.
        RowMatrix mat = new RowMatrix(rows.rdd());

        // Compute the top 4 principal components.
        // Principal components are stored in a local dense matrix.
        Matrix pc = mat.computePrincipalComponents(4);

        // Project the rows to the linear space spanned by the top 4 principal
        // components.
        RowMatrix projected = mat.multiply(pc);
        
        Vector[] collectPartitions = (Vector[]) projected.rows().collect();
        System.out.println("Projected vector of principal component:");
        for (Vector vector : collectPartitions) {
            System.out.println("\t" + vector);
        }
        jsc.stop();
        */
    }
}