package ch.heigvd.dai.algorithms;

public abstract class LosslessAlgorithm {
  static public enum Algorithms {
    LZW,
    RLE
  };

  abstract void compress(String archive, String output);

  abstract void extract(String archive, String output);
}
