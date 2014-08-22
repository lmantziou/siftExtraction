package matlab.featureExtraction;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import siftExtraction.sift;

import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import util.io.IOUtil;
import vLAD.Aggregator;
import vLAD.Normalization;
import vLAD.PrincipalComponentAnalysis;
import vLAD.ReadSpatialPyramids;
import vLAD.VladAggregator;

/**
 * 
 * @author lmantziou
 *
 * compute SIFT descriptor using vlfeat matlab library and extract spatial pyramids vector
 * as described in 
 * @inproceedings{Lazebnik:2006:BBF:1153171.1153549,
 *  author = {Lazebnik, Svetlana and Schmid, Cordelia and Ponce, Jean},
 *   title = {Beyond Bags of Features: Spatial Pyramid Matching for Recognizing Natural Scene Categories},
 *   booktitle = {Proceedings of the 2006 IEEE Computer Society Conference on Computer Vision and Pattern Recognition - Volume 2},
 *   series = {CVPR '06},
 *   year = {2006},
 *   isbn = {0-7695-2597-0},
 *   pages = {2169--2178},
 *   numpages = {10},
 *   url = {http://dx.doi.org/10.1109/CVPR.2006.68}
 *   
 *   IMPORTANT INFORMATION: The siftExtraction.jar is built with vlfeat0.9.13 version. To run this program you need to copy the *.dll
 *   (or *.so for linux) files from ../vlfeat-0.9.13-bin/vlfeat-0.9.13-bin/vlfeat-0.9.13/toolbox/mex/mexw64 (or mexa64 for linux)
 *   and paste them to hidden temp path ("c:/Users/"username"/Local/"username"/mcrcachev7.17/"package_name"/..//vlfeat-0.9.13-bin/
 *   vlfeat-0.9.13-bin/vlfeat-0.9.13/toolbox/mex/mexw64 (or mexa64 for linux)")
} 
 */
public class SpatialPyramidsVlFeat {

	static sift smal = null;
	static Object[] SIFT = null;

	private static PrincipalComponentAnalysis PCA;
	private static final int codebooklength = 128;
	private static final int numCentroids = 64;
	private static final int SIFTlength = 128; //huesift=165, RGBSift & OPPSift=384
	private static int numPrincipalComponents =512;
	private static int vectorLength =8* SIFTlength*numCentroids;
	//		private static int vectorLength = numPrincipalComponents*numCentroids;
	private static VladAggregator VLAD;



	/**
	 * Transforms a set of descriptors into a vector
	 */
	public double[] transformToVector(double[][] descriptors) throws Exception {
		// next the descriptors are aggregated
		double[] vladVector = VLAD.aggregate(descriptors);
		return vladVector;
	}

	public SpatialPyramidsVlFeat(String codebookFileName) throws Exception {

		// initialize the VLAD object
		if (codebookFileName != null) {
			double[][] codebook = Aggregator.readCodebookFile(codebookFileName, numCentroids, codebooklength);
			VLAD = new VladAggregator(codebook);
		}
	}


	public static double[] pyramids(double[][] descriptors, String codebookFileName) throws Exception{

		double power = 0.5;
		/**
		 * Compute pyramids
		 * 
		 */

		ReadSpatialPyramids vecResult = ReadSpatialPyramids.ReadPyramids(descriptors, codebookFileName,SIFTlength);

		double[] vector1 = vecResult.getvector1();
		double[] vector2 = vecResult.getvector2();
		double[] vector3 = vecResult.getvector3();
		double[] vector4 = vecResult.getvector4();
		double[] vector5 = vecResult.getvector5();
		double[] vector6 = vecResult.getvector6();
		double[] vector7 = vecResult.getvector7();
		double[] vector8 = vecResult.getvector8();


		/**
		 * Normalize after VLAD encoding
		 * 
		 */
		//			vector1 = Normalization.normalizePower(vector1, power);
		//			vector2 = Normalization.normalizePower(vector2, power);
		//			vector3 = Normalization.normalizePower(vector3, power);
		//			vector4 = Normalization.normalizePower(vector4, power);
		//			vector5 = Normalization.normalizePower(vector5, power);
		//			vector6 = Normalization.normalizePower(vector6, power);
		//			vector7 = Normalization.normalizePower(vector7, power);
		//			vector8 = Normalization.normalizePower(vector8, power);


		vector1 = Normalization.normalizeL2(vector1);
		vector2 = Normalization.normalizeL2(vector2);
		vector3 = Normalization.normalizeL2(vector3);
		vector4 = Normalization.normalizeL2(vector4);
		vector5 = Normalization.normalizeL2(vector5);
		vector6 = Normalization.normalizeL2(vector6);
		vector7 = Normalization.normalizeL2(vector7);
		vector8 = Normalization.normalizeL2(vector8);


		/**
		 * Concatenate All the pyramids 
		 * to extract final Vector
		 * Standard Step
		 */

		double[] finalVector=ReadSpatialPyramids.concat(vector1, vector2, vector3, vector4, vector5, vector6, vector7, vector8);
		finalVector =Normalization.normalizePower(finalVector, power);
		finalVector =Normalization.normalizeL2(finalVector);

		return finalVector;

	}

	public static void transformPyramidsToVector(String idListFile,String directory,String codebookFileName) throws Exception {
		smal = new sift();
		List<String> ids = IOUtil.readFileToStringList(idListFile); 
		String PCAFileName = Constants.ROOT_DIRECTORY2+Constants.IMAGECLEF_SIFT_CODEBOOK+"projection_Sift_pyramids_5000_"+numPrincipalComponents+".txt";
		long start = System.currentTimeMillis();
		if (ids == null) {
			System.out.println("Either dir does not exist or is not a directory");

		} else {  	
			PCA = new PrincipalComponentAnalysis(numPrincipalComponents, 1, vectorLength);
			PCA.setPCAFromFile(PCAFileName);

			String output = "C:/lena/features/nuswide/siftPyrpca"+numPrincipalComponents+".txt";

			BufferedWriter writer = new BufferedWriter(new FileWriter(output));

			for (int j=0;j<ids.size();j++){


				System.out.println(j);

				String imageFilename = directory +ids.get(j)+".jpg";//ids.get(i)
	

				//							System.out.println(imageFilename);


				writer.append(ids.get(j));
				writer.append("\t");

				try {
					SIFT=smal.featureExtractionFunc(1, imageFilename,"true");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
				MWNumericArray MWdescriptors =(MWNumericArray) SIFT[0];

				double[][] descriptors = (double[][])MWdescriptors.toArray();
				MWArray.disposeArray(SIFT);

				if(descriptors.length==0){
					writer.append("\t"+"0");
					writer.newLine();
					continue;
				}


				// the Pyramid VLAD vector

				double[] vladVector = SpatialPyramidsVlFeat.pyramids(descriptors, codebookFileName);

				double[] pcaReducedVector = PCA.sampleToEigenSpace(vladVector);
				pcaReducedVector = Normalization.normalizeL2(pcaReducedVector);


				for (int x = 0; x < pcaReducedVector.length; x++) {
					writer.append("\t" + String.valueOf(pcaReducedVector[x]));
					//	            	System.out.print(String.valueOf(featureValues[x])+"  ");	
				}
				writer.newLine();

			}

			writer.close();

			System.out.println("ok");

			long end = System.currentTimeMillis();
			System.out.println("vlad calculation and write to file completed in " + (end - start) + " ms");

			smal.dispose();

		}	
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {	 
		/**
		 * train paramaters
		 */
		String idListFile ="\\\\iti-195/smal/dataset/NUS-Wide/NUSWIDEIDLIST.txt";
		String codebookFileName = "codebook.txt";
		String directory = "\\\\iti-195/Images/";			
			

		try {
			transformPyramidsToVector(idListFile, directory, codebookFileName);	    		
		} catch (IOException e){
			e.printStackTrace();
		}
	}	




}
