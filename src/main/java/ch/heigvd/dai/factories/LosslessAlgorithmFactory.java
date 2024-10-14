package ch.heigvd.dai.factories;

import ch.heigvd.dai.algorithms.*;

public class LosslessAlgorithmFactory {

  public static LosslessAlgorithm make(LosslessAlgorithm.Algorithms algorithm) {
    LosslessAlgorithm compression_algorithm = switch (algorithm) {
      case LZW -> new LZW();
      case RLE -> new RLE();
    };

    return compression_algorithm;
  }
}
