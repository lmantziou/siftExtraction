package vLAD;

import matlab.featureExtraction.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import util.io.IOUtil;

public class SamplingForCodebookPreparation {
	/**
	 * 
	 * 
	 * Takes sample keypoints from 10k Flickr dataset to produce the projection matrix 
	 * or the appropriate dataset (normalized or PCA sift) in order to produce the codebook
	 * 
	 */

	private static int numPrincipalComponents =80;
	private static final int SIFTlength = 128; //huesift=165, RGBSift & OPPSift=384
	private static PrincipalComponentAnalysis PCA;
	private static double percent = 0.022;
	private static boolean setPCA;
	private static boolean setNorm;

	/**
	 * SampleKeypoints
	 * 
	 * take some sample keypoints according to a ratio
	 * from a file which extracted from colodescriptor.exe 
	 * and write them back to a file
	 * 
	 * @param idListFile
	 * @param directory
	 * @param outputFile
	 * @throws Exception
	 */
	public static void SampleKeypoints(String idListFile, String directory,String outputFile)throws Exception{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		List<String> ids = IOUtil.readFileToStringList(idListFile); 
	    if (ids == null) {
	    	System.out.println("Either dir does not exist or is not a directory");
	    	
	    } else {
	    	for (int i=0; i<ids.size(); i++) {
	    		System.out.println(i);
	            String filename = directory + ids.get(i) + ".bin";
	            //Sift Vector @ 128-d,165-d,384-d 
	            ReadFile vectorResult = ReadFile.vectorFile(filename);
	           
	            int numOfKeypoints = vectorResult.getnumVectors();
	    		
	    		int numOfSample = (int) (numOfKeypoints*percent);
	    		double[][] keypoints  = vectorResult.getSift();
	    		
	    		for(int x=0;x<numOfKeypoints; x+=numOfSample){
	    			for (int j=0;j<vectorResult.getdescriptorLength();j++){
	    				writer.append(String.valueOf(keypoints[x][j])+"\t");
	    			}
	    			writer.newLine();	    			
	    		}	
	    	}
	    	writer.close();
	    }	    
	}




	/**
	 * readSamples
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static double[][] readSamples(String filename) throws IOException{
		BufferedReader inArff = new BufferedReader(new FileReader(new File(filename)));

		GeneralFunctions countNumVectors = new GeneralFunctions();
		int numVectors = countNumVectors.count(filename);
		double[][] sample = new double[numVectors][SIFTlength];
		int a = 0;
		String line = null;
		
		while (( line = inArff.readLine())!=null)
		{
			System.out.println(a);
			String[] values1 = line.trim().split("\t");
			for (int j = 0; j < values1.length; j++) {	
				sample[a][j] = Double.parseDouble(values1[j]);			
			}
		a++;
	
		}
		inArff.close();

		return sample;
		
	}
		
	/**
	 * PrepareSiftForCodebook
	 * 
	 * @param filename
	 * @param ProjectionFile
	 * @param outputFeatureFile
	 * @throws Exception
	 * 
	 * setPCA == true computes PCA @ initial SIFT to use it as input for computing new CODEBOOK
	 * OR
	 * setNorm ==true computes l2 Normalization @ initial SIFT to use it as input for computing new CODEBOOK
	 */
	
	
	public static void PrepareSiftForCodebook(String filename, String ProjectionFile, String outputFeatureFile) throws Exception{
		
		double[][] sample= readSamples(filename);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFeatureFile));
		
	    /**
	     * load arff header
	     */
	    String arffDir="D:/lena/Codes/MyCodes/LaplacianEigenmaps_Code/dataset/Flickr10k/Sift_PCA/arrf_header_sift"+numPrincipalComponents+".txt";
	    BufferedReader reader = new BufferedReader(new FileReader(new File(arffDir)));
	    String attributeline = null;
		while (( attributeline = reader.readLine())!=null){
			writer.append(attributeline);
			writer.newLine();
		}
		reader.close();
	    
		if(setPCA==true){
			/**
			 * load projection matrix
			 */
			PrincipalComponentAnalysis PCA = new PrincipalComponentAnalysis(numPrincipalComponents, 1, SIFTlength);
		    PCA.setPCAFromFile(ProjectionFile);
		    
			for(int i = 0;i<sample.length;i++){
//				System.out.println(Arrays.toString(sample[i]));
				if (numPrincipalComponents < SIFTlength) {
					sample[i] = PCA.sampleToEigenSpace(sample[i]);    
					sample[i] =Normalization.normalizeL2(sample[i]);
					writer.append(Arrays.toString(sample[i]).split("[\\[\\]]")[1]+",");
//					System.out.println(Arrays.toString(sample[i]).split("[\\[\\]]")[1]+" ");
					
				} 
				writer.newLine();
			}
		}
		
		if(setNorm==true){			
			/**
			 * Do normalization @ Initial sift
			 */
			for(int i = 0;i<sample.length;i++){
				sample[i] =Normalization.normalizeL2(sample[i]);
				writer.append(Arrays.toString(sample[i]).split("[\\[\\]]")[1]+",");
				writer.newLine();
			}	
		}
				
//	    System.out.println();
	    writer.close();			
	}

		
	
	public static void main(String[] args) throws Exception {	 
		/**
		 * FLICKR10K PARAMS
		 */
		String idListFile = Constants.ROOT_DIRECTORY2 + Constants.FLICKR_Image_10K_IDFILE + "Flickr_images_ID10K.txt";
		String directory = Constants.ROOT_DIRECTORY2 +Constants.FLICKR_SIFT_DENSE;
		String PCAFileName =Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_SIFT_CODEBOOK+"projection_Sift_10000_"+ numPrincipalComponents + ".txt";
		
		
		String outputFile = Constants.ROOT_DIRECTORY2+Constants.FLICKR_SIFT_PCA+"Sift_sample_keypoints"+".txt";
//		String PCAoutputFile = Constants.ROOT_DIRECTORY2+Constants.FLICKR_SIFT_PCA+"flickr_sift_10000_"+numPrincipalComponents+"_BeforeVL"+".arff";
		String PCAoutputFile = Constants.ROOT_DIRECTORY2+Constants.FLICKR_SIFT_PCA+"test"+".arff";
		String NormoutputFile = Constants.ROOT_DIRECTORY2+Constants.FLICKR_SIFT_PCA+"flickr_sift_10000_withL2"+".arff";
		try {
	
//			SampleKeypoints(idListFile,directory,outputFile);
//			SampleKeypointsforPyramids (idListFile,directory,outputFile);
			
			
			setPCA = true; // output-->PCAoutputFile
			setNorm = false; // output-->NormoutputFile
			PrepareSiftForCodebook(outputFile, PCAFileName,NormoutputFile ); 
			
			/**
			 * as back up work ok
			 */
//			PCAATSift(outputFile, PCAFileName, PCAoutputFile);
//			NormalizationATSift(outputFile,NormoutputFile);

	    } catch (IOException e){
	    	e.printStackTrace();
	    }		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	public static void PCAATSift( String filename, String ProjectionFile, String outputFeatureFile) throws Exception { 
		BufferedReader inArff = new BufferedReader(new FileReader(new File(filename)));
		int numVectors = 460043;
		int vectorLength = SIFTlength;
		double[][] sample = new double[numVectors][vectorLength];
		int a = 0;
		String line = null;
		while (( line = inArff.readLine())!=null)
		{
			String[] values1 = line.trim().split("\t");
			/**
			 * load keypoints
			 */
			for (int j = 0; j < vectorLength; j++) {		
				sample[a][j] = Double.parseDouble(values1[j]);
//				System.out.print(sample[a][j]+" ");				
			}
		a++;
		}
		inArff.close();
		
		/**
		 * load projection matrix
		 */
		PrincipalComponentAnalysis PCA = new PrincipalComponentAnalysis(numPrincipalComponents, 1, vectorLength);
	    PCA.setPCAFromFile(ProjectionFile);
	    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFeatureFile));
	    /**
	     * load arff header
	     */
	    String arffDir="D:/lena/Codes/MyCodes/LaplacianEigenmaps_Code/dataset/Flickr10k/Sift_PCA/arrf_header_sift"+numPrincipalComponents+".txt";
	    BufferedReader reader = new BufferedReader(new FileReader(new File(arffDir)));
	    String attributeline = null;
		while (( attributeline = reader.readLine())!=null){
			writer.append(attributeline);
			writer.newLine();
		}
		reader.close();
	    
		for(int i = 0;i<numVectors;i++){
//			System.out.println(Arrays.toString(sample[i]));
			if (numPrincipalComponents < vectorLength) {
				sample[i] = PCA.sampleToEigenSpace(sample[i]);    
				sample[i] =Normalization.normalizeL2(sample[i]);
				writer.append(Arrays.toString(sample[i]).split("[\\[\\]]")[1]+",");
//				System.out.println(Arrays.toString(sample[i]).split("[\\[\\]]")[1]+" ");
				
			} 
			writer.newLine();
		}
//	    System.out.println();
	    writer.close();		
} 	
	
	/**
	 * 
	 * @param filename
	 * @param ProjectionFile
	 * @param outputFeatureFile
	 * @throws Exception
	 * 
	 * computes l2 Normalization @ initial SIFT to use it as input for computing new CODEBOOK
	 */

	public static void NormalizationATSift( String filename, String outputFeatureFile) throws Exception { 
		BufferedReader inArff = new BufferedReader(new FileReader(new File(filename)));
		int numVectors = 460043;
		int vectorLength = SIFTlength;
		double[][] sample = new double[numVectors][vectorLength];
		int a = 0;
		String line = null;
		while (( line = inArff.readLine())!=null)
		{
			String[] values1 = line.trim().split("\t");
			/**
			 * load keypoints
			 */
			for (int j = 0; j < vectorLength; j++) {		
				sample[a][j] = Double.parseDouble(values1[j]);
//				System.out.print(sample[a][j]+" ");				
			}
		a++;
		}
		inArff.close();
	
	    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFeatureFile));
	    /**
	     * load arff header
	     */
	    String arffDir="D:/lena/Codes/MyCodes/LaplacianEigenmaps_Code/dataset/Flickr10k/Sift_PCA/arrf_header_sift"+".txt";
	    BufferedReader reader = new BufferedReader(new FileReader(new File(arffDir)));
	    String attributeline = null;
		while (( attributeline = reader.readLine())!=null){
			writer.append(attributeline);
			writer.newLine();
		}
		reader.close();
	    
		for(int i = 0;i<numVectors;i++){
//			System.out.println(Arrays.toString(sample[i]));
				sample[i] =Normalization.normalizeL2(sample[i]);
				writer.append(Arrays.toString(sample[i]).split("[\\[\\]]")[1]+",");
//				System.out.println(Arrays.toString(sample[i]).split("[\\[\\]]")[1]+" ");
			writer.newLine();
		}
//	    System.out.println();
	    writer.close();		
}
	

}
