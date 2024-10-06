package ch.heigvd.dai.algorithms;

public abstract class LosslessAlgorithm {
  static public enum Algorithms {
    LZW,
    RLE
  };

  abstract public void compress(String archive, String output);

  abstract public void extract(String archive, String output);
}
