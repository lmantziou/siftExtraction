package vLAD;

import java.util.ArrayList;
import java.util.List;

import matlab.featureExtraction.SpatialPyramidsVlFeat;

public class ReadSpatialPyramids {
	static double[] vector1;
	static double[] vector2;
	static double[] vector3;
	static double[] vector4;
	static double[] vector5;
	static double[] vector6;
	static double[] vector7;
	static double[] vector8;
	static double[] pyramidVector;
	private static PrincipalComponentAnalysis PCA;

	public ReadSpatialPyramids(double[] pyramidVector,double[] vector1,double[] vector2,double[] vector3,double[] vector4,double[] vector5,double[] vector6,double[] vector7,double[] vector8) {
		this.pyramidVector = pyramidVector;
		this.vector1= vector1;
		this.vector2=vector2;
		this.vector3 = vector3;
		this.vector4 = vector4;
		this.vector5 = vector5;
		this.vector6 = vector6;
		this.vector7 = vector7;
		this.vector8 = vector8;	
	}	
		
	public double[] getpyramidVector(){
		return pyramidVector;
	}
	
	public double[] getvector1(){
		return vector1;
	}
	public double[] getvector2(){
		return vector2;
	}
	public double[] getvector3(){
		return vector3;
	}
	public double[] getvector4(){
		return vector4;
	}
	public double[] getvector5(){
		return vector5;
	}
	public double[] getvector6(){
		return vector6;
	}
	public double[] getvector7(){
		return vector7;
	}
	public double[] getvector8(){
		return vector8;
	}

	public static ReadSpatialPyramids ReadPyramids(String filename,String codebookFileName, boolean PCAatSift) throws Exception{
		
		ReadFile vectorResult = ReadFile.vectorFile(filename);
		int[][] coordinates = vectorResult.getCoordinates();
		double[][] sift = vectorResult.getSift();
		int numVectors = vectorResult.getnumVectors();
		int descriptorLength = vectorResult.getdescriptorLength();
		
		if(PCAatSift==true){
			PCA = SpatialPyramids.getPCA();
			for(int x=0;x<numVectors;x++){
				sift[x] = PCA.sampleToEigenSpace(sift[x]);
				sift[x]= Normalization.normalizeL2(sift[x]);
			}
			// descriptorLength after PCA to 80 or 40
			descriptorLength=SpatialPyramids.getnumPrincipalComponents();
		}
		
		int meanX = (coordinates[numVectors-1][0]-coordinates[0][0])/2;
		int meanY = (coordinates[numVectors-1][1]-coordinates[0][1])/2;
		int avY = (coordinates[numVectors-1][1]-coordinates[0][1])/3;
		//System.out.println(coordinates[numVectors-1][1]+" - "+ coordinates[0][1] + "=" +meanY);
		//System.out.println(coordinates[numVectors-1][0]+" - "+ coordinates[0][0] + "=" +meanX);
		
		List<double[]> win1 = new ArrayList<double[]>();
		List<double[]> win2 = new ArrayList<double[]>();
		List<double[]> win3 = new ArrayList<double[]>();
		List<double[]> win4 = new ArrayList<double[]>();
		List<double[]> win5 = new ArrayList<double[]>();
		List<double[]> win6 = new ArrayList<double[]>();
		List<double[]> win7 = new ArrayList<double[]>();
		
		
		for(int i =0;i<numVectors;i++){
			double[] vec1 = new double[descriptorLength];
			double[] vec2 = new double[descriptorLength];
			if(coordinates[i][0]<=meanX && coordinates[i][1]<=meanY){
			    for(int j=0; j<descriptorLength;j++) {
			        vec1[j] = sift[i][j];
			    }
			    win1.add(vec1);
			} else if(coordinates[i][0]>meanX && coordinates[i][1]<meanY){
			    for(int j=0; j<descriptorLength;j++) {
			        vec1[j] = sift[i][j];
			    }
			    win2.add(vec1);
			} else 	if(coordinates[i][0]>meanX && coordinates[i][1]>meanY){
			    for(int j=0; j<descriptorLength;j++) {
			        vec1[j] = sift[i][j];
			    }
			    win3.add(vec1);
			} else 	if(coordinates[i][0]<meanX && coordinates[i][1]>meanY){
			    for(int j=0; j<descriptorLength;j++) {
			        vec1[j] = sift[i][j];
			    }
			    win4.add(vec1);
			}
			if(coordinates[i][1]<avY){
				for(int j=0; j<descriptorLength;j++) {
			        vec2[j] = sift[i][j];
			    }
			    win5.add(vec2);		
			} else if(coordinates[i][1]>=avY && coordinates[i][1]<=2*avY){
				for(int j=0; j<descriptorLength;j++) {
			        vec2[j] = sift[i][j];
			    }
			    win6.add(vec2);
				
			}else if(coordinates[i][1]>2*avY){
				for(int j=0; j<descriptorLength;j++) {
			        vec2[j] = sift[i][j];
			    }
			    win7.add(vec2);
			}
		
		}
		
		double[][] window1 = new double[win1.size()][];
		for(int i = 0;i<win1.size();i++){
			window1[i] = win1.get(i);
		}
		
		double[][] window2 = new double[win2.size()][];
		for(int i = 0;i<win2.size();i++){
			window2[i] = win2.get(i);
		}
		double[][] window3 = new double[win3.size()][];
		for(int i = 0;i<win3.size();i++){
			window3[i] = win3.get(i);
		}
		double[][] window4 = new double[win4.size()][];
		for(int i = 0;i<win4.size();i++){
			window4[i] = win4.get(i);
		}
		double[][] window5 = new double[win5.size()][];
		for(int i = 0;i<win5.size();i++){
			window5[i] = win5.get(i);
		}
		double[][] window6 = new double[win6.size()][];
		for(int i = 0;i<win6.size();i++){
			window6[i] = win6.get(i);
		}
		double[][] window7 = new double[win7.size()][];
		for(int i = 0;i<win7.size();i++){
			window7[i] = win7.get(i);
		}
		
		
		SpatialPyramids vectorizer = new SpatialPyramids(codebookFileName);
		
		//double[][] image = "images/test.txt";
		vector1 = vectorizer.transformToVector(window1);
		vector2 = vectorizer.transformToVector(window2);
		vector3 = vectorizer.transformToVector(window3);
		vector4 = vectorizer.transformToVector(window4);
		vector5 = vectorizer.transformToVector(window5);
		vector6 = vectorizer.transformToVector(window6);
		vector7 = vectorizer.transformToVector(window7);
		
		vector8 =new double[vector1.length];
		for(int f=0;f<vector1.length;f++){
			vector8[f] = vector1[f] + vector2[f]+ vector3[f]+ vector4[f];
		}
		pyramidVector=concat(vector1, vector2, vector3, vector4, vector5, vector6, vector7, vector8);
		
		return new ReadSpatialPyramids(pyramidVector,vector1,vector2,vector3,vector4,vector5,vector6,vector7,vector8);

}
	
	
	/**
	 * from vlfeat-matlab extraction pyramids vectors
	 * 
	 * @param filename
	 * @param codebookFileName
	 * @param PCAatSift
	 * @return
	 * @throws Exception
	 */
public static ReadSpatialPyramids ReadPyramids(double[][] sift,String codebookFileName, int descriptorLength) throws Exception{
	
	
		int numVectors = sift.length;

//		System.out.println("numvectors"+numVectors);
		
		double meanX = (sift[numVectors-1][0]-sift[0][0])/2;
		double meanY = (sift[numVectors-1][1]-sift[0][1])/2;
		double avY = (sift[numVectors-1][1]-sift[0][1])/3;
		
//		System.out.println(sift[numVectors-1][1]+" - "+ sift[0][1] + "=" +meanY);
//		System.out.println(sift[numVectors-1][0]+" - "+ sift[0][0] + "=" +meanX);
		
		List<double[]> win1 = new ArrayList<double[]>();
		List<double[]> win2 = new ArrayList<double[]>();
		List<double[]> win3 = new ArrayList<double[]>();
		List<double[]> win4 = new ArrayList<double[]>();
		List<double[]> win5 = new ArrayList<double[]>();
		List<double[]> win6 = new ArrayList<double[]>();
		List<double[]> win7 = new ArrayList<double[]>();
		
		
		for(int i =0;i<numVectors;i++){
			double[] vec1 = new double[descriptorLength];
			double[] vec2 = new double[descriptorLength];
			if(sift[i][0]<=meanX && sift[i][1]<=meanY){
			    for(int j=0; j<descriptorLength;j++) {
			        vec1[j] = sift[i][j];
			    }
			    win1.add(vec1);
			} else if(sift[i][0]>meanX && sift[i][1]<meanY){
			    for(int j=0; j<descriptorLength;j++) {
			        vec1[j] = sift[i][j];
			    }
			    win2.add(vec1);
			} else 	if(sift[i][0]>meanX && sift[i][1]>meanY){
			    for(int j=0; j<descriptorLength;j++) {
			        vec1[j] = sift[i][j];
			    }
			    win3.add(vec1);
			} else 	if(sift[i][0]<meanX && sift[i][1]>meanY){
			    for(int j=0; j<descriptorLength;j++) {
			        vec1[j] = sift[i][j];
			    }
			    win4.add(vec1);
			}
			if(sift[i][1]<avY){
				for(int j=0; j<descriptorLength;j++) {
			        vec2[j] = sift[i][j];
			    }
			    win5.add(vec2);		
			} else if(sift[i][1]>=avY && sift[i][1]<=2*avY){
				for(int j=0; j<descriptorLength;j++) {
			        vec2[j] = sift[i][j];
			    }
			    win6.add(vec2);
				
			}else if(sift[i][1]>2*avY){
				for(int j=0; j<descriptorLength;j++) {
			        vec2[j] = sift[i][j];
			    }
			    win7.add(vec2);
			}
		
		}
		
		double[][] window1 = new double[win1.size()][];
		for(int i = 0;i<win1.size();i++){
			window1[i] = win1.get(i);
		}
		
		double[][] window2 = new double[win2.size()][];
		for(int i = 0;i<win2.size();i++){
			window2[i] = win2.get(i);
		}
		double[][] window3 = new double[win3.size()][];
		for(int i = 0;i<win3.size();i++){
			window3[i] = win3.get(i);
		}
		double[][] window4 = new double[win4.size()][];
		for(int i = 0;i<win4.size();i++){
			window4[i] = win4.get(i);
		}
		double[][] window5 = new double[win5.size()][];
		for(int i = 0;i<win5.size();i++){
			window5[i] = win5.get(i);
		}
		double[][] window6 = new double[win6.size()][];
		for(int i = 0;i<win6.size();i++){
			window6[i] = win6.get(i);
		}
		double[][] window7 = new double[win7.size()][];
		for(int i = 0;i<win7.size();i++){
			window7[i] = win7.get(i);
		}
		
		
		SpatialPyramidsVlFeat vectorizer = new SpatialPyramidsVlFeat(codebookFileName);
		
		//double[][] image = "images/test.txt";
		vector1 = vectorizer.transformToVector(window1);
		vector2 = vectorizer.transformToVector(window2);
		vector3 = vectorizer.transformToVector(window3);
		vector4 = vectorizer.transformToVector(window4);
		vector5 = vectorizer.transformToVector(window5);
		vector6 = vectorizer.transformToVector(window6);
		vector7 = vectorizer.transformToVector(window7);
		
		vector8 =new double[vector1.length];
		for(int f=0;f<vector1.length;f++){
			vector8[f] = vector1[f] + vector2[f]+ vector3[f]+ vector4[f];
		}
		pyramidVector=concat(vector1, vector2, vector3, vector4, vector5, vector6, vector7, vector8);
		
		return new ReadSpatialPyramids(pyramidVector,vector1,vector2,vector3,vector4,vector5,vector6,vector7,vector8);

}
	
	
	

	/**
	 * Concatenate the 8 spatial pyramids into 1
	 * @param V1
	 * @param V2
	 * @param V3
	 * @param V4
	 * @param V5
	 * @param V6
	 * @param V7
	 * @param V8
	 * @return
	 */
	public static double[] concat(double[] V1, double[] V2,double[] V3,double[] V4,double[] V5,double[] V6,double[] V7,double[] V8){
	   
		int v1Len = V1.length;
		int v2Len = V2.length;
		int v3Len = V3.length;
		int v4Len = V4.length;
		int v5Len = V5.length;
		int v6Len = V6.length;
		int v7Len = V7.length;
		int v8Len = V8.length;
		double[] V= new double[v1Len+v2Len+v3Len+v4Len+v5Len+v6Len+v7Len+v8Len];
		System.arraycopy(V1, 0, V, 0, v1Len);
		System.arraycopy(V2, 0, V, v1Len, v2Len);
		System.arraycopy(V3, 0, V, v1Len+v2Len,v3Len);
		System.arraycopy(V4, 0, V, v1Len+v2Len+v3Len,v4Len);
		System.arraycopy(V5, 0, V, v1Len+v2Len+v3Len+v4Len,v5Len);
		System.arraycopy(V6, 0, V, v1Len+v2Len+v3Len+v4Len+v5Len,v6Len);
		System.arraycopy(V7, 0, V, v1Len+v2Len+v3Len+v4Len+v5Len+v6Len,v7Len);
		System.arraycopy(V8, 0, V, v1Len+v2Len+v3Len+v4Len+v5Len+v6Len+v7Len,v8Len);
		return V;
	}
	
}

