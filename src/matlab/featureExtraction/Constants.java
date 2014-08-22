/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matlab.featureExtraction;

import java.io.File;

/**
 * @author lmantziou
 * 
 */
public class Constants {
    public static String ROOT_DIRECTORY="D:" + File.separator+ "lena"+File.separator+"Competitions"+File.separator+"Image_Clef_2012" + File.separator;
//    public static String ROOT_DIRECTORY2 = "D:" + File.separator+ "lena" + File.separator + "Codes" + File.separator + "MyCodes" + File.separator+"LaplacianEigenmaps_Code" + File.separator;
    public static String ROOT_DIRECTORY2 = "\\\\iti-195"+File.separator+"smal" + File.separator;
    /**
     * Standard Parameters
     */
    public static String IMAGECLEF_Image_15K_IDFILE = "java"+ File.separator+ "imageclef_Image_ID15K";
    public static String MIRFLICKR_Image_15K_IDFILE = "java"+ File.separator+ "flickr13_Image_ID";//flickr_Image_ID10KNEW
//    public static String IMAGECLEF_Image_15K_IDFILE = "java"+ File.separator+ "images";
    public static String IMAGECLEF_Image_10K_IDFILE = "java"+ File.separator+ "imageclef_Image_ID10K";
    public static String MIRFLICKR_Image_10K_IDFILE = "java"+ File.separator+ "flickr_Image_ID10K";
    public static String IMAGECLEF_Image_25K_IDFILE = "java"+ File.separator+ "imageclef_Image_ID25K";
    public static String MIRFLICKR_Image_25K_IDFILE = "java"+ File.separator+ "flickr_Image_ID25K";
    public static String IMAGECLEF_Concept_IDFILE ="java"+ File.separator+ "imageclef_concept_ID15K";
    public static String IMAGECLEF_Concept_NameFILE ="java"+ File.separator + "imageclef_concept_Name15K";
    public static String IMAGECLEF_Concepts ="dataset"+ File.separator + "train_annotations"+ File.separator +"concepts" + File.separator;
    public static String IMAGECLEF_ImageId_15K_List = "java"+ File.separator+ "imageId_15k_List";
    public static String IMAGECLEF_ImageId_10K_List = "java"+ File.separator+ "imageId_10k_List";
    
//    public static String IMAGECLEF_TRAIN_15K_ImageIDFILE = "image_ids_15K";
    
    public static String IMAGECLEF_train_dataFILE = "imageclef_traindata_15K";
    public static String IMAGECLEF_train_idFILE = "imageclef_trainID_13KNEWWWW";
    
    /**
     * TRAIN PATHS IMAGECLEF
     */

    public static String IMAGECLEF_TRAIN_GIST_DIRECTORY="dataset"+File.separator+"train+test_features"+File.separator+"train_features_gist"+File.separator;
    public static String IMAGECLEF_TRAIN_GIST_All_DIRECTORY="results"+File.separator+"features"+File.separator;
    public static String IMAGECLEF_TRAIN_VLAD_DIRECTORY="dataset" + File.separator + "train_features_vlad" + File.separator;
    public static String IMAGECLEF_TRAIN_BOW_DIRECTORY ="dataset" + File.separator + "train_features_textual_bow" + File.separator;
    
    public static String IMAGECLEF_TRAIN_VLADDENSE_DIRECTORY ="dataset" + File.separator + "train_features_vlad+dense_sift" + File.separator;
    public static String IMAGECLEF_TRAIN_VLADDENSEHUESIFT_DIRECTORY ="dataset" + File.separator + "train_features_vlad+dense_huesift" + File.separator;
    public static String IMAGECLEF_TRAIN_VLADDENSEDENSE_RGBSIFT_DIRECTORY ="dataset" + File.separator + "train_features_vlad+dense_rgbsift" + File.separator;
    public static String IMAGECLEF_TRAIN_VLADDENSEDENSE_OPPONENTSIFT_DIRECTORY ="dataset" + File.separator + "train_features_vlad+dense_opponentsift" + File.separator;
    public static String IMAGECLEF_TRAIN_VLADDENSEDENSE_RGSIFT_DIRECTORY ="dataset" + File.separator + "train_features_vlad+dense_rgsift" + File.separator;
    public static String IMAGECLEF_TRAIN_VLADDENSEDENSE_CSIFT_DIRECTORY ="dataset" + File.separator + "train_features_vlad+dense_csift" + File.separator;
    public static String IMAGECLEF_TRAIN_VLADDENSEDENSE_HSVSIFT_DIRECTORY ="dataset" + File.separator + "train_features_vlad+dense_hsv" + File.separator;
    public static String IMAGECLEF_TRAIN_HARRISLAPLACE_HUESIFT_DIRECTORY ="dataset" + File.separator + "train_features_vlad+harris_huesift" + File.separator;
    public static String IMAGECLEF_TRAIN_HARRISLAPLACE_SIFT_DIRECTORY ="dataset" + File.separator + "train_features_vlad+harris_sift" + File.separator;
    public static String IMAGELCEF_TRAIN_LBP_DIRECTORY ="dataset" + File.separator + "train_features_lbp" + File.separator;
    
    
    public static String IMAGECLEF_TRAIN_PLSA_DIRECTORY ="dataset" + File.separator + "train_features_plsa" + File.separator; 
    public static String IMAGECLEF_TRAIN_TOPSURF_DIRECTORY="dataset"+File.separator+"train_features_topsurf"+File.separator;
    public static String IMAGELCEF_TRAIN_FEATURE_All_DIRECTORY="results"+File.separator+"features"+File.separator+"NEW"+File.separator;
    
    
    public static String IMAGECLEF_TRAIN_ANNOTATIONS_DIRECTORY="dataset"+File.separator+"train_annotations"+File.separator + "annotations" + File.separator;
    public static String IMAGECLEF_TRAIN_ANNOTATIONS_All_DIRECTORY="results"+File.separator+"features"+File.separator;
  
    public static String IMAGECLEF_TRAIN_CONCEPT_DIRECTORY="dataset"+File.separator+"train_annotations"+File.separator+"concepts"+File.separator;
    
    public static String IMAGECLEF_TRAIN_METADATA_DIRECTORY="dataset"+File.separator+"metadata"+File.separator;
    public static String IMAGECLEF_TEST_ST_METADATA_DIRECTORY="dataset"+File.separator+"test_metadata_ST"+File.separator;
    public static String IMAGECLEF_METADATA_DIRECTORY="dataset"+File.separator+"metadata"+File.separator;
    
    public static String IMAGECLEF_TEST_ANNOTATIONS_DIRECTORY="dataset"+File.separator+"test_annotations"+File.separator + "annotations" + File.separator;
    
    
    

    public static String IMAGECLEF_SIFT_CODEBOOK ="dataset" + File.separator + "sift_codebooks_64" + File.separator +"training-densesampling_sift-64"+File.separator;
    public static String IMAGECLEF_OPPSIFT_CODEBOOK ="dataset" + File.separator + "sift_codebooks_64" + File.separator +"training-densesampling_opponentsift-64"+File.separator;
    public static String IMAGECLEF_RGBSIFT_CODEBOOK ="dataset" + File.separator + "sift_codebooks_64" + File.separator +"training-densesampling_rgbsift-64"+File.separator;
    public static String IMAGECLEF_HUESIFT_CODEBOOK ="dataset" + File.separator + "sift_codebooks_64" + File.separator +"training-densesampling_huesift-64"+File.separator;
   
	public static String IMAGECLEF_SIFT_DENSE_TRAIN = "dataset"+File.separator+"ImageCLEF2012"+File.separator+"Sift_Dense"+File.separator+"CLEF_SiftDense_nozeros"+File.separator;
	public static String IMAGECLEF_SIFT_DENSE_TEST = "dataset"+File.separator+"ImageCLEF2012"+File.separator+"Sift_Dense"+File.separator+"Sift_Dense_test"+File.separator;
	public static String IMAGECLEF_SIFT_DENSE_VLAD = "dataset"+File.separator+"ImageCLEF2012"+File.separator+"Sift_Dense_VLAD"+File.separator;
	public static String IMAGECLEF_SIFT_DENSE_VLAD_PYRAMIDS = "dataset"+File.separator+"ImageCLEF2012"+File.separator+"Sift_Dense_VLAD_Pyramids"+File.separator;
	
	
	public static String IMAGECLEF_RGBSIFT_DENSE_TRAIN = "dataset"+File.separator+"ImageCLEF2012"+File.separator+"RGBSift_Dense"+File.separator+"RGBSift_DenseSampling_train"+File.separator;
	public static String IMAGECLEF_RGBSIFT_DENSE_TEST = "dataset"+File.separator+"ImageCLEF2012"+File.separator+"RGBSift_Dense"+File.separator+"RGBSift_DenseSampling_test"+File.separator;
	public static String IMAGECLEF_RGBSIFT_DENSE_VLAD = "dataset"+File.separator+"ImageCLEF2012"+File.separator+"RGBSift_Dense_VLAD"+File.separator;
	public static String IMAGECLEF_RGBSIFT_DENSE_VLAD_PYRAMIDS = "dataset"+File.separator+"ImageCLEF2012"+File.separator+"RGBSift_Dense_VLAD_Pyramids"+File.separator;
	
	public static String IMAGECLEF_OPPSIFT_DENSE_TRAIN = "dataset"+File.separator+"ImageCLEF2012"+File.separator+"OppSift_Dense"+File.separator+"OPPSift_Dense_train"+File.separator;
	public static String IMAGECLEF_OPPSIFT_DENSE_TEST = "dataset"+File.separator+"ImageCLEF2012"+File.separator+"OppSift_Dense"+File.separator+"OPPSift_Dense_test"+File.separator;
	public static String IMAGECLEF_OPPSIFT_DENSE_VLAD = "dataset"+File.separator+"ImageCLEF2012"+File.separator+"OppSift_Dense_VLAD"+File.separator;
	
	public static String IMAGECLEF_HUESIFT_DENSE_TRAIN = "dataset"+File.separator+"ImageCLEF2012"+File.separator+"HueSift_Dense"+File.separator+"HueSift_Dense_train"+File.separator;
	public static String IMAGECLEF_HUESIFT_DENSE_TEST = "dataset"+File.separator+"ImageCLEF2012"+File.separator+"HueSift_Dense"+File.separator+"HueSift_Dense_test"+File.separator;
	public static String IMAGECLEF_HUESIFT_DENSE_VLAD = "dataset"+File.separator+"ImageCLEF2012"+File.separator+"HueSift_Dense_VLAD"+File.separator;
    
	
	/**
	 * TRAIN PATHS NUS-WIDE
	 */
	
	public static String NUSWIDE_IMAGES = "D:"+File.separator+"lena"+File.separator+"Datasets"+File.separator+"NUS-Wide"+File.separator+"Nus-Wide"+File.separator+"Images"+File.separator;
	public static String NUSWIDE_SIFT_DENSE_TRAIN = "dataset"+File.separator+"NUS-Wide"+File.separator+"Sift_Dense"+File.separator+"NUSWIDE_SiftDense"+File.separator;
	public static String NUSWIDE_SIFT_DENSE_TEST = "dataset"+File.separator+"NUS-Wide"+File.separator+"Sift_Dense"+File.separator+"NUSWIDE_SiftDense"+File.separator;
	public static String NUSWIDE_SIFT_DENSE_VLAD = "dataset"+File.separator+"NUS-Wide"+File.separator+"Sift_Dense_VLAD"+File.separator;
	public static String NUSWIDE_SIFT_DENSE_VLAD_PYRAMIDS = "dataset"+File.separator+"NUS-Wide"+File.separator+"Sift_Dense_VLAD_Pyramids"+File.separator;
	
	
	
	
	public static String NUSWIDE_Image_256K_IDFILE = "dataset"+File.separator+"NUS-Wide"+File.separator;
	/**
	 * TRAIN PATHS FLICKR
	 */
	public static String FLICKR_Images_Directory = "dataset" + File.separator + "Flickr10k" + File.separator + "images_90-100" + File.separator;
	public static String FLICKR_Image_10K_IDFILE = "dataset"+ File.separator+ "Flickr10k" +File.separator;


	
	public static String FLICKR_SIFT_DENSE = "dataset"+File.separator+ "Flickr10k"+File.separator+"sift_DenseSampling"+File.separator;
	public static String FLICKR_OPPSIFT_DENSE = "dataset"+File.separator+ "Flickr10k"+File.separator+"OPP-Sift_DenseSampling"+File.separator;
	public static String FLICKR_SIFT_DENSE_PCA = "dataset"+File.separator+ "Flickr10k"+File.separator+"sift_DenseSampling"+File.separator;

	public static String FLICKR_SIFT_PCA = "dataset"+File.separator+ "Flickr10k"+File.separator+"Sift_PCA"+File.separator;

	
 
    
    public static String FLICKR_TRAIN_IMAGES_DIRECTORY="train_images"+File.separator;
    public static String FLICKR_TEST_IMAGES_DIRECTORY="test_images"+File.separator;
    
    
    
    /**
	 * TRAIN PATHS MIR-FLICKR 2013
	 */
    public static String FLICKR_Image_IDFILE = "dataset"+File.separator+"Twitter2013"+File.separator;
	public static String FLICKR13_IMAGES = "D:"+File.separator+"lena"+File.separator+"Competitions"+File.separator+"Image_Clef_2012"+File.separator+"dataset"+File.separator+"FlickrDataCollection2013"+File.separator;
	public static String FLICKR13_SIFT_DENSE_VLAD = "dataset"+File.separator+"Twitter2013"+File.separator+"Sift_Dense_VLAD"+File.separator;
	
	
	
	/**
	 * TRAIN PATHS Yahoo GC 2013
	 */
    public static String YAHOO_Image_IDFILE = "dataset"+File.separator+"YahooGC"+File.separator;
	public static String YAHOO_IMAGES = "\\\\iti-195"+File.separator+"images2"+File.separator;
	public static String YAHOO_SIFT_DENSE_VLAD = "dataset"+File.separator+"YahooGC"+File.separator+"Sift_Dense_VLAD_PCA"+File.separator;
	public static String YAHOO_OPPSIFT_DENSE_VLAD = "dataset"+File.separator+"YahooGC"+File.separator+"OppSift_Dense_VLAD_PCA"+File.separator;
	public static String YAHOO_RGBSIFT_DENSE_VLAD = "dataset"+File.separator+"YahooGC"+File.separator+"RGBSift_Dense_VLAD_PCA"+File.separator;
	public static String YAHOO_SIFTPYR_DENSE_VLAD = "dataset"+File.separator+"YahooGC"+File.separator+"Sift_Dense_VLAD_PCA_PYR"+File.separator;
	
	
	
    /**
	 * TRAIN PATHS MediaEval SED 2013
	 */
    public static String EVAL_Image_IDFILE = "dataset"+File.separator+"MediaEval"+File.separator;
	public static String EVAL_IMAGES = "\\\\iti-195"+File.separator+"SED_photos"+File.separator;
	public static String EVAL_SIFT_DENSE_VLAD = "dataset"+File.separator+"MediaEval"+File.separator+"Sift_Dense_VLAD"+File.separator;
	public static String EVAL_RGBSIFT_DENSE_VLAD = "dataset"+File.separator+"MediaEval"+File.separator+"RGBSift_Dense_VLAD"+File.separator;
	public static String EVAL_SIFTPYR_DENSE_VLAD = "dataset"+File.separator+"MediaEval"+File.separator+"SiftPYR_Dense_VLAD"+File.separator;
	public static String EVAL_TEXT = "dataset"+File.separator+"MediaEval"+File.separator;
   
    /**
     * TEST PATHS
     */
    public static String IMAGECLEF_TEST_GIST_DIRECTORY="dataset"+File.separator+"test_features_gist"+File.separator;
    public static String IMAGECLEF_TEST_GIST_All_DIRECTORY="results"+File.separator+"features"+File.separator;
    public static String IMAGECLEF_TEST_TOPSURF_DIRECTORY="dataset"+File.separator+"test_features_topsurf"+File.separator;
    public static String IMAGECLEF_TEST_METADATA_DIRECTORY="dataset"+File.separator+"test_metadata"+File.separator;
    public static String IMAGECLEF_TEST_VLAD_DIRECTORY="dataset" + File.separator + "test_features_vlad" + File.separator;
    public static String IMAGECLEF_TEST_LBP_DIRECTORY ="dataset" + File.separator + "test_features_lbp" + File.separator;
    
}
