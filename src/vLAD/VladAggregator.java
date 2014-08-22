package vLAD;

/**
 * This class computes raw VLAD vectors. The produced vectors should be power and L2 normalized afterwards.
 * 
 * @author Eleftherios Spyromitros-Xioufis
 */
public class VladAggregator extends Aggregator {

	/**
	 * The default constructor calls the super constructor.
	 * 
	 * @param codebook
	 */
	public VladAggregator(double[][] codebook) {
		super(codebook);
	}

	/**
	 * Takes as input 2-dimensional double array which contains the set of local descriptors for an image.
	 * Returns the VLAD vector representation of the image using the codebook supplied in the constructor.
	 * 
	 * @param descriptors
	 * @return
	 * @throws Exception
	 */
	public double[] aggregateInternal(double[][] descriptors) {
		// when there are 0 local descriptors extracted
		if (descriptors.length == 0) {
			return new double[numCentroids * descriptorLength];
		}

		double[] vlad = new double[numCentroids * descriptorLength];
		for (double[] descriptor : descriptors) {
			int nnIndex = computeNearestCentroid(descriptor);
			for (int i = 0; i < descriptorLength; i++) {
				vlad[nnIndex * descriptorLength + i] += descriptor[i] - codebook[nnIndex][i];
			}
		}
		return vlad;
	}
}
