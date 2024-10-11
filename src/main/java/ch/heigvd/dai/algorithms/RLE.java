package ch.heigvd.dai.algorithms;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class RLE extends LosslessAlgorithm {
  @Override
  public void compress(String archive, String output) {
    try (Reader reader = new FileReader(archive, StandardCharsets.US_ASCII);
        BufferedReader rBuf = new BufferedReader(reader);
        Writer writer = new FileWriter(output, StandardCharsets.US_ASCII);
        BufferedWriter wBuf = new BufferedWriter(writer);) {

      int counter = 1;
      int expected = rBuf.read();
      int cur;

      // TODO: Try to cleanup by extracting repeated parts and using something else
      // than a while loop

      while ((cur = rBuf.read()) != -1) {

        if (Character.isDigit(cur)) {
          // TODO: support numeric values by including an escape character?
          throw new RLEException("RLE encoding cannot compress files containing numeric values");
        }

        // Check for repetitions
        if (cur == expected) {
          ++counter;
          continue;
        }

        // Write to the output with "nc" where n is the number of occurences and c the
        // repeated character. (just write the char if it's not repeated)
        if (counter > 1) {
          wBuf.append(String.valueOf(counter));
        }
        wBuf.append((char) expected);
        expected = cur;
        counter = 1;
      }

      // Since we are using a while loop, we do need to write after exiting the loop
      if (expected != -1) {
        if (counter > 1) {
          wBuf.append(String.valueOf(counter));
        }
        wBuf.append((char) expected);
      }

      wBuf.flush();
    } catch (IOException err) {
      System.err.println("Exception: " + err);
    }
  };

  @Override
  public void extract(String archive, String output) {
    try (Reader reader = new FileReader(archive, StandardCharsets.US_ASCII);
        BufferedReader rBuf = new BufferedReader(reader);
        Writer writer = new FileWriter(output, StandardCharsets.US_ASCII);
        BufferedWriter wBuf = new BufferedWriter(writer);) {

      // Using a string builder since we don't seem to have an analog to `counter <<
      // rBuf` which would directly parse an int
      StringBuilder counter = new StringBuilder();
      int cur;

      while ((cur = rBuf.read()) != -1) {
        if (Character.isDigit((char) cur)) {
          counter.append((char) cur);
          continue;
        }

        // If the counter is empty, that means the char isn't repeated.
        if (counter.isEmpty()) {
          wBuf.write(cur);
          continue;
        }

        // Write the character n times
        wBuf.append(new StringBuilder().repeat((char) cur, Integer.parseInt(counter.toString())));
        counter.setLength(0);
      }

      wBuf.flush();

    } catch (IOException err) {
      System.err.println("Exception: " + err);
    }
  };
}
