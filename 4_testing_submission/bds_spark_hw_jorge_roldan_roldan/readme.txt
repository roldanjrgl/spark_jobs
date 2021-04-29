#######################################
# General instructions 
#######################################
* Open main folder in Eclipse
* Make sure to use JavaSE-1.8 as execution enviroment
* Select Project -> Run As -> Mavel Install

#######################################
# PCA on matrix.csv input
#######################################
spark-submit --class PCA --master yarn --deploy-mode client --executor-memory 1G PCA-Matrix-0.0.1-SNAPSHOT-jar-with-dependencies.jar matrixInput/matrix.csv > pca_on_matrix_output.txt
 

#######################################
# SVD on matrix.csv input
#######################################
spark-submit --class SVD --master yarn --deploy-mode client --executor-memory 1G SVD-Matrix-0.0.1-SNAPSHOT-jar-with-dependencies.jar matrixInput/matrix.csv >  svd_on_matrix_output.txt


#######################################
# Feature Selection - Chi Sq Selector on iris.csv input
#######################################
spark-submit --class FeatureSelection --master yarn --deploy-mode client --executor-memory 1G Feature-Selection-0.0.1-SNAPSHOT-jar-with-dependencies.jar irisInput/iris_no_header.csv > output_v1_iris.txt


#######################################
# Unsupervised Learning - k means  on iris.csv input
#######################################
spark-submit --class UnsupervisedLearning --master yarn --deploy-mode client --executor-memory 1G Unsupervised-Learning-0.0.1-SNAPSHOT-jar-with-dependencies.jar irisInput/iris_no_header.csv > output_unsupervised_iris_v1.txt


#######################################
# Supervised Learning - logistic regression  on iris.csv input
#######################################
spark-submit --class SupervisedLearning --master yarn --deploy-mode client --executor-memory 1G Supervised-Learning-0.0.1-SNAPSHOT-jar-with-dependencies.jar irisInput/iris_no_header.csv > output_supervised_iris_v1.txt
