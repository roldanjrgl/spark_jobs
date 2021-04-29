#######################################
# General instructions 
#######################################
* Open main folder in Eclipse
* Make sure to use JavaSE-1.8 as execution enviroment



#######################################
# PCA on matrix.csv input
#######################################
spark-submit --class PCA --master yarn --deploy-mode client --executor-memory 1G PCA-Matrix-0.0.1-SNAPSHOT-jar-with-dependencies.jar matrixInput/matrix.csv > pca_on_matrix_input.txt
 


