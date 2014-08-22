package matlab.featureExtraction;

import matlab.featureExtraction.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWNumericArray;


import siftExtraction.sift;

import util.io.IOUtil;
import vLAD.CreateVLADVector;
import vLAD.Normalization;
import vLAD.PrincipalComponentAnalysis;

public class Vlvector {

	
	static sift smal = null;
	static Object[] SIFT = null;
	
	private static int numPrincipalComponents =1024;
	private static int vectorLength = 8192;
	private static PrincipalComponentAnalysis PCA;

	/**
	 * Initializes an ImageToVectorSimple object which is able to transform an input image into a VLAD+SIFT(PCA)
	 * vector. The descriptors are extracted by MATLAB function using vlfeat
	 * 
	 * @param codebookFileName
	 *            the file containing the centroids
	 * @param PCAFileName
	 *            the file containing the PCA projection matrix
	 * @throws Exception
	 */

	/**
	 * 
	 * @param idListFile
	 * @param directory
	 * @param codebookFileName
	 * @throws Exception
	 */
	/**
	 * Transforms the image into a vector
	 * Initializes an ImageToVectorSimple object which is able to transform an input image into a VLAD+SIFT
	 * vector
	 */	
	public static void transformToVector(String idListFile,String directory,String codebookFileName) throws Exception {
		smal = new sift();
		String output = "C:/Users/lmantziou/Desktop/voc2012/siftpca"+numPrincipalComponents+"_train.txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		List<String> ids = IOUtil.readFileToStringList(idListFile); 
		String PCAFileName =  Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_SIFT_CODEBOOK+"projection_Sift_10000_"+ numPrincipalComponents +".txt";
		if (ids == null) {
			System.out.println("Either dir does not exist or is not a directory");
		} else {  
	        PCA = new PrincipalComponentAnalysis(numPrincipalComponents, 1, vectorLength);
	        PCA.setPCAFromFile(PCAFileName);
			long start = System.currentTimeMillis();
			for (int j=0;j<ids.size();j++){
				System.out.println(j);

				String imageFilename = directory +ids.get(j)  + ".jpg";//ids.get(i)

				try {
					SIFT=smal.featureExtractionFunc(1, imageFilename,"false");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MWNumericArray MWdescriptors =(MWNumericArray) SIFT[0];
				double[][] descriptors = (double[][])MWdescriptors.toArray();

				// the VLAD vector
				double[] vladVector =  CreateVLADVector.vectorFile(descriptors, codebookFileName);

				//PCA
				double[] pcaReducedVector = PCA.sampleToEigenSpace(vladVector);
				pcaReducedVector = Normalization.normalizeL2(pcaReducedVector);

				writer.append(ids.get(j));
				writer.append("\t");
				for (int x = 0; x < pcaReducedVector.length; x++) {
					writer.append("\t" + String.valueOf(pcaReducedVector[x]));
//					System.out.print(String.valueOf(pcaReducedVector[x])+"  ");	
				}
				writer.newLine();
				MWArray.disposeArray(SIFT);

			}  	
			smal.dispose();
			
			
			writer.close();

			System.out.println("ok");
			long end = System.currentTimeMillis();
			System.out.println("vlad calculation and write to file completed in " + (end - start) + " ms");

		}
				
	}
	
	
  
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		
		String idListFile = Constants.ROOT_DIRECTORY2+Constants.FLICKR_Image_IDFILE+ "keepcalm.txt";
		String codebookFileName = Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_SIFT_CODEBOOK +"codebook"+".txt";

		String directory = "\\\\iti-195"+File.separator+"Images"+File.separator;
          
	
		transformToVector(idListFile,directory,codebookFileName);
	}
}


