package ch.heigvd.dai.commands;

import java.io.File;
import java.util.concurrent.Callable;

import ch.heigvd.dai.algorithms.LosslessAlgorithm;
import picocli.CommandLine;

@CommandLine.Command(description = "A small CLI that compresses and deflates files.", version = "1.0.0", scope = CommandLine.ScopeType.INHERIT, mixinStandardHelpOptions = true)
public class Root implements Callable<Integer> {
  public enum AlgorithmTypes {
    LWZ
  };

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
    // TODO: use a factory to instanciate the right algorithm
    return 0;
  }

}
