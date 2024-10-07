package ch.heigvd.dai.commands;

import java.io.File;
import java.util.concurrent.Callable;

import ch.heigvd.dai.algorithms.*;
import picocli.CommandLine;

@CommandLine.Command(description = "A small CLI that compresses and deflates files.", version = "1.0.0", scope = CommandLine.ScopeType.INHERIT, mixinStandardHelpOptions = true)
public class Root implements Callable<Integer> {

  @CommandLine.Option(names = { "-c", "--create" }, description = "create a new archive")
  private boolean create;

  @CommandLine.Option(names = { "-x", "--extract" }, description = "extract from an archive")
  private boolean extract;

  @CommandLine.Option(names = { "-a",
      "--algorithm" }, description = "The loss-less algorithm to use (possible values: ${COMPLETION-CANDIDATES}).", required = true)
  protected LosslessAlgorithm.Algorithms algorithm;

  @CommandLine.Parameters(paramLabel = "FILE", description = "one or more files to archive")
  private File[] files;

  @CommandLine.Option(names = { "-o",
      "--output" }, description = "The output file or directory for both compress and deflate commands", required = true)
  private String output;

  public Integer call() {
    // TODO: Use factory in the future
    LosslessAlgorithm compression_algorithm = switch (algorithm) {
      case LZW -> new LZW();
      case RLE -> new RLE();
    };

    if (create && extract) {
      // TODO: throw specific exception
      throw new RuntimeException("Cannot use compress and extract flags at the same time.");
    }

    if (create) {
      // TODO: for now, only the first file is archived is tmp!
      // In the future, only .tar files will be passed to compress()
      compression_algorithm.compress(files[0].getAbsolutePath(), output);
    }

    if (extract) {
      // TODO: for now, only the first file is extracted is tmp!
      // In the future, only .tar files will be passed to extract()
      compression_algorithm.extract(files[0].getAbsolutePath(), output);
    }

    return 0;
  }

}
