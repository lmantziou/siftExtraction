package vLAD;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.FileSystem;
//import org.apache.hadoop.fs.Path;

//import utilities.Result;

//import com.aliasi.util.BoundedPriorityQueue;

/**
 * All methods which aggregate a set of local image descriptors should extend this abstract class.
 * 
 * @author Eleftherios Spyromitros-Xioufis
 * 
 */
public abstract class Aggregator {

	/**
	 * The codebook (centroids) used to aggregate the vectors. Each centroid is stored in a different row.
	 */
	protected double[][] codebook;

	/**
	 * The number of centroids in the codebook.
	 */
	protected int numCentroids;

	/**
	 * The dimensionality of the local descriptors ( should be equal to the dimensionality of each centroid).
	 */
	protected int descriptorLength;

	public int getNumCentroids() {
		return numCentroids;
	}

	public void setNumCentroids(int numCentroids) {
		this.numCentroids = numCentroids;
	}

	public int getDescriptorLength() {
		return descriptorLength;
	}

	public void setDescriptorLength(int descriptorLength) {
		this.descriptorLength = descriptorLength;
	}

	/**
	 * This method performs some general checks before calling the aggregateInternal method which is
	 * implemented by each aggregator.
	 * 
	 * @param descriptors
	 *            a set of local descriptors
	 * @return a vector which aggregates the local descriptors
	 * @throws Exception
	 */
	public double[] aggregate(double[][] descriptors) throws Exception {
		if (descriptors.length > 0) {
			if (descriptors[0].length != descriptorLength) {
				throw new Exception("Descriptor length is incompatible with codebook centroid length!");
			}
		}
		return aggregateInternal(descriptors);
	}

	/**
	 * This method should be overridden by all aggregators.
	 * 
	 * @param descriptors
	 * @return
	 */
	protected abstract double[] aggregateInternal(double[][] descriptors) throws Exception;

	protected Aggregator() {

	}

	/**
	 * The constructor.
	 * 
	 * @param codebook
	 */
	protected Aggregator(double[][] codebook) {
		this.codebook = codebook;
		this.numCentroids = codebook.length;
		this.descriptorLength = codebook[0].length;
	}

	/**
	 * Returns the index of the centroid which is closer to the given descriptor.
	 * 
	 * @param descriptor
	 * @return
	 */
	protected int computeNearestCentroid(double[] descriptor) {
		int centroidIndex = -1;
		double minDistance = Double.MAX_VALUE;
		for (int i = 0; i < numCentroids; i++) {
			double distance = 0;
			for (int j = 0; j < descriptorLength; j++) {
				distance += (codebook[i][j] - descriptor[j]) * (codebook[i][j] - descriptor[j]);
				// when distance becomes greater than minDistance
				// break the inner loop and check the next centroid!!!
				if (distance >= minDistance) {
					break;
				}
			}
			if (distance < minDistance) {
				minDistance = distance;
				centroidIndex = i;
			}
		}
		return centroidIndex;
	}

	/**
	 * Returns the indices of the k centroids which are closer to the given descriptor. Can be used for soft
	 * quantization. Fast implementation with a bounded priority queue. TO DO: early stopping!
	 * 
	 * @param descriptor
	 * @param k
	 * @return
	 */
//	protected int[] computeKNearestCentroids(double[] descriptor, int k) {
//		BoundedPriorityQueue<Result> bpq = new BoundedPriorityQueue<Result>(new Result(), k);
//
//		double minDistance = Double.MAX_VALUE;
//		for (int i = 0; i < numCentroids; i++) {
//			double distance = 0;
//			boolean skip = false;
//			for (int j = 0; j < descriptorLength; j++) {
//				distance += (codebook[i][j] - descriptor[j]) * (codebook[i][j] - descriptor[j]);
//				if (distance > minDistance) {
//					skip = true;
//					break;
//				}
//			}
//			if (skip) {
//				continue;
//			}
//			bpq.offer(new Result(i, distance));
//			if (i >= k) {
//				minDistance = bpq.last().getDistance();
//			}
//		}
//		int[] nn = new int[k];
//		for (int i = 0; i < k; i++) {
//			nn[i] = bpq.poll().getInternalId();
//		}
//		return nn;
//	}

	/**
	 * Reads the codebook from the given file and returns it as two-dimensional double array.
	 * 
	 * @param codebookFileName
	 *            the name of the file containing the codebook
	 * @throws IOException
	 */
	public static double[][] readCodebookFile(String codebookFileName, int numCentroids, int centroidLength) throws IOException {
		double[][] codebook = new double[numCentroids][centroidLength];
		// load the codebook
		BufferedReader in = new BufferedReader(new FileReader(codebookFileName));
		String line;
		int counter = 0;
		while ((line = in.readLine()) != null) {
			String[] centerStrings = line.split(",");
			for (int i = 0; i < centerStrings.length; i++) {
				codebook[counter][i] = Double.parseDouble(centerStrings[i]);
			}
			counter++;
		}
		in.close();
		return codebook;
	}

	/**
	 * Reads the codebook from the given file in hdfs and returns it as two-dimensional double array.
	 * 
	 * @param codebookFileName
	 *            the name of the hdfs file containing the codebook
	 * @throws IOException
	 */
//	public static double[][] readCodebookFile(Configuration conf, Path codebookFileName, int numCentroids, int centroidLength) throws IOException {
//
//		double[][] codebook = new double[numCentroids][centroidLength];
//
//		// load the codebook from hdfs
//		FileSystem hdfs = FileSystem.get(conf);
//		DataInputStream d = new DataInputStream(hdfs.open(codebookFileName));
//		BufferedReader in = new BufferedReader(new InputStreamReader(d));
//
//		String line;
//		int counter = 0;
//		while ((line = in.readLine()) != null) {
//			String[] centerStrings = line.split(",");
//			for (int i = 0; i < centerStrings.length; i++) {
//				codebook[counter][i] = Double.parseDouble(centerStrings[i]);
//			}
//			counter++;
//		}
//		in.close();
//		return codebook;
//	}
}
