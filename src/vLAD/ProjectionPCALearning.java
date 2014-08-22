package vLAD;

import matlab.featureExtraction.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.io.IOUtil;
import vLAD.CreateVLADVector;

public class ProjectionPCALearning {

	/**
	 * computes the Projection Matrices doing PCA
	 * @ vlad Vectors/initial sift 
	 * OR
	 * @ pyramids
	 *
	 * 
	 */


	private static final int SIFTlength = 128; //huesift=165, RGBSift & OPPSift=384
	private static final int numCentroids = 64;
	private static int vectorLength = SIFTlength*numCentroids;
	// @ InitialSIFT --> numPrincipalComponents=80, 40
	// @VLAD --> numPrincipalComponents = 1024, 512
	private static int numPrincipalComponents = 128; 
	private static PrincipalComponentAnalysis PCA;

	private static boolean projectionAtVLAD;
	private static boolean projectionAtSift;
	private static boolean setPyramidsAtVLAD;



	/**
	 *  PrepareProjectionFile
	 *  
	 *  computes the projection matrix @ Vlad if projectionAtVLAD==true
	 *  or 
	 *  compute the projection matrix @ Initial Sift if projectionAtSift==true
	 *  
	 *  if projectionAtSift ==true and sampleKeypoints file contains INFO from Pyramids
	 *  then this code computes the projectionMatrix @ Initial Sift from pyramids
	 *  
	 * @param idListFile
	 * @param directory
	 * @param sampleKeypointsFile
	 * @param codebookFileName
	 * @param outputFile
	 * @throws Exception
	 */
	public static void PrepareProjectionFile(String idListFile,String directory,String sampleKeypointsFile,String codebookFileName,String outputFile) throws Exception {

		int numTrainVectors=0;
		/**
		 * projection @ VLAD or @ VLAD-pyramids
		 * 
		 * numPrincipalComponents = 1024 or 512
		 */
		if(projectionAtVLAD==true){
			List<double[]> vector_array = new ArrayList<double[]>();
			double[] featureValues=null;
			List<String> ids = IOUtil.readFileToStringList(idListFile); 
			if (ids == null) {
				System.out.println("Either dir does not exist or is not a directory");
			} else {	    	
				for (int i=0; i<ids.size(); i++) {
					System.out.println(i);
					String filename = directory + ids.get(i) + ".bin";
					//		            String filename = "D:/Image_Clef_2012/dataset/train_features_Sift_DenseSampling/0a478b1fa7226d82995c0192ea.bin";


					/**
					 * Compute projection matrix at VLAD sift 
					 */
					featureValues =  CreateVLADVector.vectorFile(filename, codebookFileName);
					//			            System.out.println(featureValues.length);
					vectorLength = featureValues.length;


					double[] vec = new double[featureValues.length];	        
					for (int x = 0; x < featureValues.length; x++) {
						vec[x] = featureValues[x];
						//			            System.out.print(String.valueOf(featureValues[x])+"  ");
					}
					vector_array.add(vec); 
				}



				// second argument is the number of vectors used to learn the PCA
				numTrainVectors = ids.size(); // 10000;
				// third argument is the number of elements in each vector
				//			int vectorLength = 65536; // 4096;

				// final argument is the desired number of principal components to be kept
				//			int numPrincipalComponents = 1024; // 1024;

				// COMPUTE PCA
				System.out.println("vectorLength "+vectorLength);
				PCA = new PrincipalComponentAnalysis(numPrincipalComponents, numTrainVectors, vectorLength);
				PCA.setCompact(false);

				for(int i = 0;i<vector_array.size();i++){
					PCA.addSample( vector_array.get(i));
				}
				vector_array = null;

				// we need to parse the training vectors file and add the vectors into the PCA class
				//			VladArray vladArray = new VladArray(vectorLength, numTrainVectors, indexLocation, false);
				//			for (int i = 0; i < numTrainVectors; i++) {
				//				PCA.addSample(vladArray.getVladVector(i));
				//			}
			}    
		}


		/**
		 * projection @ Sift
		 * 
		 * numPrincipalComponents = 80 or 40
		 * 
		 * if sampleKeypointsFile ==from spatial Pyramids --> computes projection for pyramids @ 80 or 40
		 */

		if(projectionAtSift==true){

			double[][] sample= SamplingForCodebookPreparation.readSamples(sampleKeypointsFile);
			numTrainVectors =sample.length;

			// COMPUTE PCA
			PrincipalComponentAnalysis PCA = new PrincipalComponentAnalysis(numPrincipalComponents, numTrainVectors, SIFTlength);
			PCA.setCompact(false);

			for(int i = 0;i<sample.length;i++){
				//				System.out.println(Arrays.toString(sample[i]));
				PCA.addSample( sample[i]);
			}
			//optional to free memory
			sample = null; 
		}

		/**
		 * do the rest
		 */

		// now we are able to perform SVD and compute the eigenvectors!
		System.out.println("PCA calculation started!");
		long start = System.currentTimeMillis();
		PCA.computeBasis();
		long end = System.currentTimeMillis();

		System.out.println("PCA calculation completed in " + (end - start) + " ms");

		// now we can save the principal components in a file
		//		String PCAfile = Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_OPPSIFT_CODEBOOK + "/pca_" + numTrainVectors + "_" + numPrincipalComponents + "_" + (end - start) + "ms.txt";
		BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
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


	public static void main(String[] args) throws Exception {	 
		String idListFile = Constants.ROOT_DIRECTORY2 + Constants.FLICKR_Image_10K_IDFILE + "Flickr_images_ID5K.txt";
		//		"Flickr_images_ID10K"
		String codebookFileName = Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_SIFT_CODEBOOK +"codebook.txt";
		String directory = Constants.ROOT_DIRECTORY2 +Constants.FLICKR_SIFT_DENSE;	

		String sampleKeypointsFile = Constants.ROOT_DIRECTORY2+Constants.FLICKR_SIFT_PCA+"Sift_sample_keypoints"+".txt";

		String outputfile = Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_SIFT_CODEBOOK +"projection_Sift_pyramids_5000_for_pyramids"+ numPrincipalComponents+"test"+".txt";

		try {
			//			projectionAtVLAD =false;
			//			projectionAtSift =true;
			/**
			 * WARNING: CHANGE FROM SpatialPyramids.java SIFTlength & codebookLength =128,165 or 386 variable
			 * WHEN setPyramidsAtVLAD = true
			 */
			//			setPyramidsAtVLAD =true;

			PrepareProjectionFile(idListFile,directory,sampleKeypointsFile,codebookFileName,outputfile);

			//			PrepareProjectionFileForPyramids(idListFile,directory, codebookFileName,outputfile);
		} catch (IOException e){
			e.printStackTrace();
		}
	}











}
