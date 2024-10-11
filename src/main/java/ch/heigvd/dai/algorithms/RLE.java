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

        // Check for repetitions
        if (cur == expected) {
          ++counter;
          continue;
        }

        // Write to the output with "nc" where n is the number of occurences and c the
        // repeated character. (just write the char if it's not repeated)
        compressWrite(wBuf, expected, counter);
        expected = cur;
        counter = 1;
      }

      // Since we are using a while loop, we do need to write after exiting the loop
      if (expected != -1) {
        compressWrite(wBuf, expected, counter);
      }

      wBuf.flush();
    } catch (IOException err) {
      System.err.println("Exception: " + err);
    }
  };

  /**
   * write a sequence of characters in RLE format. The formats are:
   * <ul>
   * <li>
   * &lt;repeat&gt;&lt;character&gt; when the character appears more than once in
   * a row
   * </li>
   * <li>
   * &lt;character&gt; when there is no repetition
   * </li>
   * <li>
   * &lt;repeat?&gt;\&lt;character&gt; when the character is a number or the
   * escaping
   * character
   * </li>
   * </ul>
   *
   * @param writer    the BufferedWriter to write to
   * @param character the character to write
   * @param repeat    the number of time the character is repeated
   * @throws IOException when there is an error with the writer
   */
  private void compressWrite(BufferedWriter writer, int character, int repeat) throws IOException {
    if (repeat > 1) {
      writer.write(String.valueOf(repeat));
    }
    if (Character.isDigit(character) || character == '\\') {
      // Escape numbers
      writer.write('\\');
    }
    writer.write(character);
  }

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
      boolean escaped = false;

      while ((cur = rBuf.read()) != -1) {
        if (!escaped && Character.isDigit((char) cur)) {
          counter.append((char) cur);
          continue;
        }

        if (!escaped && cur == '\\') {
          escaped = true;
          continue;
        }

        escaped = false;

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
