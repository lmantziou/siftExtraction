package vLAD;

import matlab.featureExtraction.Constants;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {

	static int[][] coordinates;
	static double[][] sift;
	static int numVectors ;
	static int descriptorLength;
	
	/**
	 * ReadFile vectorFile reads a file which contains keypoints from an image with their coordinates 
	 * as have been extracted from colordescriptor exe
	 * 
	 * @param sift
	 * @param coordinates
	 * @param numVectors --> the number of keypoints/image
	 * @param descriptorLength  -->128 for sift, 165 for huesift etc
	 */
	public ReadFile(double[][] sift, int[][] coordinates, int numVectors, int descriptorLength)  {

		this.coordinates = coordinates;
		this.sift = sift;
		this.numVectors = numVectors;
		this.descriptorLength = descriptorLength;
	}	
	
	
	public ReadFile(double[][] sift, int numVectors, int descriptorLength)  {

		this.sift = sift;
		this.numVectors = numVectors;
		this.descriptorLength = descriptorLength;
	}
	
	public double[][] getSift(){
		return sift;
	}
	public int[][] getCoordinates(){
		return coordinates;	
	}
	
	public int getnumVectors(){
		return numVectors;
	}
	
	public int getdescriptorLength(){
		return descriptorLength;
	}
		
	public static ReadFile vectorFile(String filename) throws IOException{	
		
		BufferedReader inArff = new BufferedReader(new FileReader(new File(filename)));

 		String[] numbers = new String[3];
		for (int i = 0; i < 3 ; i++) {
			numbers [i] =inArff.readLine();    
      	}

		numVectors = Integer.parseInt(numbers[2]);
		descriptorLength = Integer.parseInt(numbers[1]);
		String line = null;
		coordinates = new int[numVectors][2];
		sift = new double[numVectors][descriptorLength];
		
		int num = 0;
		while (( line = inArff.readLine())!=null)
		{
			int numCoord=0;
			String[] values = line.split(";");
			String part1 = values[1];
			String[] values1 = part1.trim().split(" ");
			String part0 = values[0];
			String[] tmp_coord = part0.split(" ");
		
			for (int j = 0; j < descriptorLength; j++) {		
				sift[num][j] = Double.parseDouble(values1[j]);
//				System.out.print(sift[a][j]+" ");	
			}
			
//			System.out.println();
				for(int k=1;k<3;k++){
					coordinates[num][numCoord] = Integer.parseInt(tmp_coord[k]);
					numCoord++;
				}
			num++;	
		}

		inArff.close();
		return new ReadFile(sift,coordinates,numVectors,descriptorLength);
	}
	
	public static ReadFile vectorFileVLFeat(String filename) throws IOException{	
		
		
		BufferedReader inArff = new BufferedReader(new FileReader(new File(filename)));
		GeneralFunctions countNumVectors = new GeneralFunctions();
		numVectors =countNumVectors.count(filename);
		descriptorLength =128;
		String line = null;
		
		sift = new double[numVectors][descriptorLength];
		
		int num = 0;
		while (( line = inArff.readLine())!=null)
		{
			String[] values = line.split("\t");
			
			for (int j = 0; j < descriptorLength; j++) {		
				sift[num][j] = Double.parseDouble(values[j+2]);
//				System.out.print(sift[num][j]+" ");	
			}
//			System.out.println();
			num++;	
		}

		inArff.close();
		return new ReadFile(sift,numVectors,descriptorLength);
	}

	
	public static void main(String[] args) throws Exception {
//		String directory = "D:/Image_Clef_2012/dataset/train_images/images/SIFT_DENSE";
		String image = "D:/lena/Codes/MyCodes/LaplacianEigenmaps_Code/dataset/Flickr10k/sift_DenseSampling/im190001.bin";
		
//		vectorFile(image);
	    ReadFile result = vectorFile(image);
	    int[][] coord = result.getCoordinates();
	    double[][] siftt = result.getSift();
	    int desc = result.getdescriptorLength();
	    int num = result.getnumVectors();
	    
	  System.out.println(coord[4401-1][1]+" - "+ coord[0][1] );
	  System.out.println(coord[4401-1][0]+" - "+ coord[0][0]);
	  System.out.println(num);
	  System.out.println(descriptorLength);
		
	}

}
