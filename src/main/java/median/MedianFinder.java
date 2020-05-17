package median;

import java.util.Arrays;


public class MedianFinder {

  private static final int CHUNK_SIZE = 5;

  public double find(double[] input) {
    return _find(input, input.length / 2);
  }

  protected double _find(double[] input, int ordinal) {
    double[][] chunks = chunk(input);
    sortChunks(chunks);

    double[] medians = getMedians(chunks);

    double pivot;

    if (medians.length <= 5) {
      Arrays.sort(medians);
      pivot = medians[medians.length / 2];
    } else {
      pivot = _find(medians, medians.length / 2);
    }

    double[] lowThanPivot = getLowThanPivot(input, pivot);
    double[] greaterThanPivot = getGreaterThanPivot(input, pivot);

    lowThanPivot = lowThanPivot != null ? lowThanPivot : new double[0];
    greaterThanPivot = greaterThanPivot != null ? greaterThanPivot : new double[0];

    int pivotIndex = lowThanPivot.length;

    if (pivotIndex == ordinal) {
      return pivot;
    } else if (pivotIndex < ordinal) {
      return _find(greaterThanPivot, ordinal - pivotIndex);
    } else {
      return _find(lowThanPivot, ordinal);
    }

  }

  private double[][] chunk(double[] input) {
    int chunksCount = input.length % CHUNK_SIZE == 0 ? input.length / CHUNK_SIZE : input.length / CHUNK_SIZE + 1;
    double[][] chunks = new double[chunksCount][];
    for (int i = 0; i < chunksCount; i++) {
      int chunkStart = i * CHUNK_SIZE;
      int chunkStop = Math.min((i + 1) * CHUNK_SIZE, input.length);
      chunks[i] = Arrays.copyOfRange(input, chunkStart, chunkStop);
    }

    return chunks;
  }

  private void sortChunks(double[][] chunks) {
    for (double[] chunk : chunks) {
      Arrays.sort(chunk);
    }
  }

  private double[] getMedians(double[][] chunks) {
    double[] medians = new double[chunks.length];
    for (int i = 0; i < medians.length; i++) {
      medians[i] = chunks[i][chunks[i].length / 2];
    }
    return medians;
  }

  private double[] getLowThanPivot(double[] input, double pivot) {
    return Arrays.stream(input).filter(element -> Double.compare(element, pivot) < 0).toArray();
  }

  private double[] getGreaterThanPivot(double[] input, double pivot) {
    return Arrays.stream(input).filter(element -> Double.compare(element, pivot) >= 0).toArray();
  }

}
