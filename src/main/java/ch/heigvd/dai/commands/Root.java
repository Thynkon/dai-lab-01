package ch.heigvd.dai.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import ch.heigvd.dai.algorithms.*;
import ch.heigvd.dai.factories.LosslessAlgorithmFactory;
import ch.heigvd.dai.wrappers.TarWrapper;
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
  private File output;

  public Integer call() {
    // TODO: Use factory in the future
    LosslessAlgorithm compression_algorithm = LosslessAlgorithmFactory.make(algorithm);

    if (create && extract) {
      // TODO: throw specific exception
      throw new RuntimeException("Cannot use compress and extract flags at the same time.");
    }

    if (create) {
      // TODO: for now, only the first file is archived is tmp!
      // In the future, only .tar files will be passed to compress()
      // compression_algorithm.compress(files[0].getAbsolutePath(), output);

      try (
          OutputStream fos = new FileOutputStream(output);
          TarArchiveOutputStream tar_stream = new TarArchiveOutputStream(fos);) {

        for (File file : this.files) {
          TarWrapper.add(tar_stream, file, "");
        }

        // write final tar file
        tar_stream.finish();
      } catch (IOException e) {
        // TODO: handle exception
      }
    }

    if (extract) {
      if (this.files.length > 1) {
        throw new IllegalArgumentException("Too many files passed to extract mode! Please, specify only one!");
      }
      // TODO: for now, only the first file is extracted is tmp!
      // In the future, only .tar files will be passed to extract()
      // compression_algorithm.extract(files[0].getAbsolutePath(), output);
      try {
        TarWrapper.extract(this.files[0], this.output);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return 0;
  }

}
