package ch.heigvd.dai.algorithms;

import java.io.File;

public abstract class LosslessAlgorithm {
  static public enum Algorithms {
    LZW,
    RLE
  };

  abstract public void compress(File archive, File output);

  abstract public void extract(File archive, File output);
}
