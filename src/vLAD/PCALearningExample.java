package vLAD;

import java.io.BufferedWriter;
import java.io.FileWriter;

//import data_structures.VladArray;
import vLAD.PrincipalComponentAnalysis;

/**
 * This example shows how to learn a PCA projection matrix from an independent set of training vectors.
 * 
 * @author Eleftherios Spyromitros-Xioufis
 * 
 */
public class PCALearningExample {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// first argument is the location of the index which contains the training vectors
		String indexLocation = args[0]; // "C:/wamp/www/CIS-final/images/surf/BDB_4096_SUM+PL/";

		// second argument is the number of vectors used to learn the PCA
		int numTrainVectors = Integer.parseInt(args[1]); // 10000;

		// third argument is the number of elements in each vector
		int vectorLength = Integer.parseInt(args[2]); // 4096;

		// final argument is the desired number of principal components to be kept
		int numPrincipalComponents = Integer.parseInt(args[3]); // 1024;

		PrincipalComponentAnalysis PCA = new PrincipalComponentAnalysis(numPrincipalComponents, numTrainVectors, vectorLength);
		PCA.setCompact(false);

		// we need to parse the training vectors file and add the vectors into the PCA class
//		VladArray vladArray = new VladArray(vectorLength, numTrainVectors, indexLocation, false);
		for (int i = 0; i < numTrainVectors; i++) {
//			PCA.addSample(vladArray.getVladVector(i));
		}

		// now we are able to perform SVD and compute the eigenvectors!
		System.out.println("PCA calculation started!");
		long start = System.currentTimeMillis();
		PCA.computeBasis();
		long end = System.currentTimeMillis();

		System.out.println("PCA calculation completed in " + (end - start) + " ms");

		// now we can save the principal components in a file
		String PCAfile = indexLocation + "/pca_" + numTrainVectors + "_" + numPrincipalComponents + "_" + (end - start) + "ms.txt";
		BufferedWriter out = new BufferedWriter(new FileWriter(PCAfile));
		// the first line of the file contains the training sample means per component
		double[] mean = PCA.getMean();
		for (int i = 0; i < mean.length; i++) {
			out.write(mean[i] + " ");
		}
		out.write("\n");

		// the second line of the file contains the eigenvalues in descending order
		double[] eigenvalues = PCA.getEigenValues();
		for (int i = 0; i < numPrincipalComponents; i++) {
			out.write(eigenvalues[i] + " ");
		}
		out.write("\n");

		// the next lines of the file contain the eigenvectors in descending eigenvalue order
		for (int i = 0; i < numPrincipalComponents; i++) {
			double[] component = PCA.getBasisVector(i);
			for (int j = 0; j < component.length; j++) {
				out.write(component[j] + " ");
			}
			out.write("\n");
		}
		out.close();
	}

}
