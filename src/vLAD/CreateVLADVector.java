package vLAD;

import matlab.featureExtraction.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;
import util.io.IOUtil;
import vLAD.Normalization;
import vLAD.Aggregator;
import vLAD.VladAggregator;


/**
 * 
 * @author lmantziou
 * 
 * @author Eleftherios Spyromitros-Xioufis
 *
 */

public class CreateVLADVector {

	/**
	 * Compute Sift with encoding VLAD and optional PCA
	 * 
	 *    IMPORTANT INFORMATION: The siftExtraction.jar is built with vlfeat0.9.13 version. To run this program you need to copy the *.dll
	 *   (or *.so for linux) files from ../vlfeat-0.9.13-bin/vlfeat-0.9.13-bin/vlfeat-0.9.13/toolbox/mex/mexw64 (or mexa64 for linux)
	 *   and paste them to hidden temp path ("c:/Users/"username"/Local/"username"/mcrcachev7.17/"package_name"/..//vlfeat-0.9.13-bin/
	 *   vlfeat-0.9.13-bin/vlfeat-0.9.13/toolbox/mex/mexw64 (or mexa64 for linux)")
	 */

	private static final int codebooklength = 64;
	private static final int numCentroids = 64;
	private static final int SIFTlength = 64; //huesift=165, RGBSift & OPPSift=384
	private static int numPrincipalComponents =512;
	private static int vectorLength = SIFTlength*numCentroids;
	private static VladAggregator VLAD;
	private static PrincipalComponentAnalysis PCA;

	private static boolean setNorm;
	private static boolean setPCAatInitialSift;



	/**
	 *  vectorFile
	 * 
	 * reads a file which contains the descriptor and creates the VLAD vector 
	 * Optional--> setNorm==true Do L2 normalization before creates the VLAD
	 * 
	 * @param filename
	 * @param codebookFileName
	 * @return
	 * @throws Exception
	 */


	public static double[] vectorFile(String filename,String codebookFileName) throws Exception{	

		ReadFile vectorResult = ReadFile.vectorFile(filename);
		//		ReadFile vectorResult = ReadFile.vectorFileVLFeat(filename); //if keypoint extracted from vlfeat


		double[][] sift = vectorResult.getSift();
		//		System.out.println("sift before "+ sift[0][0]);
		int numVectors = vectorResult.getnumVectors();
		int descriptorLength = vectorResult.getdescriptorLength();
		/**
		 * L2 Norm at initial SIFT
		 */

		if(setNorm==true){
			for (int j=0;j<numVectors;j++){
				sift[j] =Normalization.normalizeL2(sift[j]);
			}
		}

		/**
		 * PCA @ Initial Sift depends on numPrincipalComponents variable (ex. from 128 to 80)
		 */
		if(setPCAatInitialSift==true){
			for(int j = 0;j<vectorResult.getnumVectors();j++){
				if (numPrincipalComponents < SIFTlength) {
					sift[j] = PCA.sampleToEigenSpace(sift[j]);    
					sift[j] =Normalization.normalizeL2(sift[j]);
				} 
			}
			descriptorLength =numPrincipalComponents;
		}

		List<double[]> win = new ArrayList<double[]>();
		for(int i =0;i<numVectors;i++){
			double[] vec = new double[descriptorLength];

			for(int j=0; j<descriptorLength;j++) {
				vec[j] = sift[i][j];
			}
			win.add(vec);
		}
		double[][] window = new double[win.size()][];
		for(int i = 0;i<win.size();i++){
			window[i] = win.get(i);
		}

		// ready to compute VLAD vector/Image
		CreateVLADVector vectorizer = new CreateVLADVector(codebookFileName);

		//double[][] image = "images/test.txt";
		double[] vector = vectorizer.transformToVector(window);
		double power = 0.5;
		vector = Normalization.normalizePower(vector, power);
		vector =Normalization.normalizeL2(vector);

		return vector;
	}



	public static double[] vectorFile(double[][] sift,String codebookFileName) throws Exception{	

		int numVectors = sift.length;
		int descriptorLength = SIFTlength;
		/**
		 * L2 Norm at initial SIFT
		 */

		if(setNorm==true){
			for (int j=0;j<numVectors;j++){
				sift[j] =Normalization.normalizeL2(sift[j]);
			}
		}

		/**
		 * PCA @ Initial Sift depends on numPrincipalComponents variable (ex. from 128 to 80)
		 */
		if(setPCAatInitialSift==true){
			for(int j = 0;j<numVectors;j++){
				if (numPrincipalComponents < SIFTlength) {
					sift[j] = PCA.sampleToEigenSpace(sift[j]);    
					sift[j] =Normalization.normalizeL2(sift[j]);
				} 
			}
			descriptorLength =numPrincipalComponents;
		}

		List<double[]> win = new ArrayList<double[]>();
		for(int i =0;i<numVectors;i++){
			double[] vec = new double[descriptorLength];

			for(int j=0; j<descriptorLength;j++) {
				vec[j] = sift[i][j];
			}
			win.add(vec);
		}
		double[][] window = new double[win.size()][];
		for(int i = 0;i<win.size();i++){
			window[i] = win.get(i);
		}

		// ready to compute VLAD vector/Image
		CreateVLADVector vectorizer = new CreateVLADVector(codebookFileName);

		//double[][] image = "images/test.txt";
		double[] vector = vectorizer.transformToVector(window);
		double power = 0.5;
		vector = Normalization.normalizePower(vector, power);
		vector =Normalization.normalizeL2(vector);

		return vector;
	}

	/**
	 *  prepareSIFTVLADFeatures
	 *  
	 *  reads descriptors/Image, compute VLAD vector and write it to a file
	 *  
	 * Optional --> setPCAatInitialSift==true -->DO PCA at initial sift before VLAD encoding
	 * @param idListFile
	 * @param directory
	 * @param codebookFileName
	 * @param outputFeatureFile
	 * @throws Exception
	 */


	public static void prepareSIFTVLADFeatures(String idListFile, String directory, String codebookFileName, String ProjectionFile, String outputFeatureFile) throws Exception {

		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFeatureFile));
		List<String> ids = IOUtil.readFileToStringList(idListFile); 
		if (ids == null) {
			System.out.println("Either dir does not exist or is not a directory");

		} else {
			System.out.println("vlad calculation started!");
			long start = System.currentTimeMillis();     

			if(setPCAatInitialSift==true){
				/**
				 * load projection matrix
				 */
				PrincipalComponentAnalysis PCA = new PrincipalComponentAnalysis(numPrincipalComponents, 1, SIFTlength);
				PCA.setPCAFromFile(ProjectionFile);

			}

			PrincipalComponentAnalysis PCA = new PrincipalComponentAnalysis(numPrincipalComponents, 1, vectorLength);
			PCA.setPCAFromFile(ProjectionFile);

			for (int i=0; i<ids.size(); i++) {
				System.out.print(i);
				System.out.println(i);


				//	                        String[] ids1 = ids.get(i).split("\\");
				//	            		    String part1 = ids1[1];
				//	            			String[] values1 = part1.trim().split("\t");   

				String filename = directory+ ids.get(i);
				//	                  String filename = "D:/Image_Clef_2012/dataset/train_features_Sift_DenseSampling/0a478b1fa7226d82995c0192ea.bin";

				// the VLAD vector
				double[] vladVector =  CreateVLADVector.vectorFile(filename, codebookFileName);

				double[] pcaReducedVector = PCA.sampleToEigenSpace(vladVector);
				pcaReducedVector = Normalization.normalizeL2(pcaReducedVector);

				writer.append(ids.get(i));
				writer.append("\t");
				for (int x = 0; x < vladVector.length; x++) {
					writer.append("\t" + String.valueOf(vladVector[x]));
					//	                      System.out.print(String.valueOf(featureValues[x])+"  ");
				}
				//	                  System.out.println();
				writer.newLine();
			}
			writer.close();
			System.out.println("ok");
			long end = System.currentTimeMillis();
			System.out.println("vlad calculation and write to file completed in " + (end - start) + " ms");
		}

	}   



	/**
	 *  prepareSIFTVLADFeatures
	 *  
	 *  reads the vlad vector and do PCA at it
	 *  
	 * @param idListFile
	 * @param directory
	 * @param filename
	 * @param outputFeatureFile
	 * @throws Exception
	 */
	public static void prepareSIFTVLADPCAFeatures(String idListFile, String filename,String PCAFileName, String outputFeatureFile) throws Exception {
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFeatureFile));
		List<String> ids = IOUtil.readFileToStringList(idListFile); 
		System.out.println("vlad with pca calculation started!");
		long start = System.currentTimeMillis();
		//		String filename = directory +"sift_dense_VLAD_test.txt";
		BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
		String strLine = null;
		int i=0;
		//        String PCAFileName =Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_OPPSIFT_CODEBOOK+"projection_OppSift_10000_"+ numPrincipalComponents + ".txt";
		PCA = new PrincipalComponentAnalysis(numPrincipalComponents, 1, vectorLength);
		PCA.setPCAFromFile(PCAFileName);
		while(( strLine = reader.readLine())!=null){
			System.out.println(i);	    	  
			String[] vector = strLine.split("\t\t");
			String part1 = vector[1];
			String[] values1 = part1.trim().split("\t");
			double[] vladVector = new double[vectorLength];
			//			System.out.println(values1.length);
			for (int j = 0; j < values1.length; j++) {
				//				System.out.print(values1[j]);
				vladVector[j] = Double.parseDouble(values1[j].trim());
			}

			/**
			 * Do PCA
			 */    

			if (numPrincipalComponents < vectorLength) {
				vladVector = PCA.sampleToEigenSpace(vladVector);
				vladVector =Normalization.normalizeL2(vladVector);
			} 
			writer.append(ids.get(i));
			writer.append("\t");
			for (int x = 0; x < vladVector.length; x++) {
				writer.append("\t" + String.valueOf(vladVector[x]));
				//	           	System.out.print(String.valueOf(featureValues[x])+"  ");
			}  
			//	        System.out.println();
			writer.newLine();
			i++;

		}
		reader.close();
		writer.close();
		System.out.println("ok");
		long end = System.currentTimeMillis();

		System.out.println("vlad and PCA calculation and write to file completed in " + (end - start) + " ms");
	}


	/**
	 * @param args
	 * @throws Exception
	 */

	public static void main(String[] args) throws Exception {	 
		/**
		 * train paramaters
		 */
		//		String idListFile = Constants.ROOT_DIRECTORY + Constants.IMAGECLEF_Image_15K_IDFILE + ".txt";
		////		String codebookFileName = Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_OPPSIFT_CODEBOOK +"codebookRGB"+numPrincipalComponents+".arff";
		////		String codebookFileName =Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_SIFT_CODEBOOK +"codebookSift_withl2.arff";
		//		String codebookFileName = Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_OPPSIFT_CODEBOOK +"codebook_rgbsift"+".txt";
		//		String directory = Constants.ROOT_DIRECTORY2 +Constants.IMAGECLEF_RGBSIFT_DENSE_TRAIN;
		//		String outputFile = Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_RGBSIFT_DENSE_VLAD+"RGBsift_dense_VLAD_train"+".txt";
		//		String PCAoutputFile = Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_RGBSIFT_DENSE_VLAD+"RGBSift_dense_VLAD_pca"+numPrincipalComponents+"_AfterVL_train"+"_new"+".txt";
		//		String ProjectionFile = Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_RGBSIFT_CODEBOOK+"projection_RGBsift_10000_"+ numPrincipalComponents +".txt";


		/**
		 * test parameters
		 */
		//		String idListFile = Constants.ROOT_DIRECTORY + Constants.IMAGECLEF_Image_10K_IDFILE + ".txt";
		//		String codebookFileName = Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_RGBSIFT_CODEBOOK +"codebookRGB"+numPrincipalComponents+".arff";
		//		String directory = Constants.ROOT_DIRECTORY2 +Constants.IMAGECLEF_RGBSIFT_DENSE_TEST;
		//		String outputFile = Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_RGBSIFT_DENSE_VLAD+"RGBsift_dense_VLAD_test"+".txt";
		////		String PCAdirectory =  Constants.ROOT_DIRECTORY2 +Constants.IMAGECLEF_RGBSIFT_DENSE_VLAD;
		//		String PCAoutputFile = Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_RGBSIFT_DENSE_VLAD+"RGBsift_dense_VLAD_pca"+numPrincipalComponents+"_BeforeVL_test"+"_new"+".txt";
		//		String ProjectionFile = Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_RGBSIFT_CODEBOOK+"projection_RGBsift_10000_"+ numPrincipalComponents +".txt";





		String idListFile = "D:/shared/VBS2014/SURF/proc_64/ids.txt";
		String codebookFileName = "D:/shared/VBS2014/SURF/proc_64/archive/sample-100000seed-0.csv_codebook-64A-64C-100I-1S_l2.arff";
		String directory = "D:/shared/VBS2014/SURF/proc_64/archive/";
		String ProjectionFile = "D:/lena/Codes/Feature Extractions/Surf Descriptors/pca.txt";
		String outputFeatureFile = "D:/shared/VBS2014/SURF/proc_64/archive/vectors/";

		try {
			//    		setPCAatInitialSift=false;
			//    		setNorm = false;
			//    		prepareSIFTVLADFeatures(idListFile, directory,codebookFileName, ProjectionFile, PCAoutputFile);
			//    		prepareSIFTVLADPCAFeatures(idListFile, outputFile, ProjectionFile, PCAoutputFile);

			prepareSIFTVLADFeatures( idListFile,  directory,  codebookFileName,  ProjectionFile,  outputFeatureFile);

		} catch (IOException e){
			e.printStackTrace();
		}


	}




	/**
	 * Initializes an ImageToVectorSimple object which is able to transform an input image into a VLAD+SURF
	 * vector
	 * 
	 * @param codebookFileName
	 *            the file containing the centroids
	 */
	public CreateVLADVector(String codebookFileName) throws Exception {

		// initialize the VLAD object
		if (codebookFileName != null) {
			double[][] codebook = Aggregator.readCodebookFile(codebookFileName, numCentroids, codebooklength);
			VLAD = new VladAggregator(codebook);

		}
	}

	/**
	 * Transforms a set of descriptors into a vector
	 */
	public double[] transformToVector(double[][] descriptors) throws Exception {
		// next the descriptors are aggregated
		double[] vladVector = VLAD.aggregate(descriptors);
		//		double power = 0.5;
		//		vladVector = Normalization.normalizePower(vladVector, power);
		//		vladVector = Normalization.normalizeL2(vladVector);

		// print the VLAD vector
		//		 System.out.println("VLAD vector:\n" + Arrays.toString(vladVector));
		return vladVector;
	}

}
