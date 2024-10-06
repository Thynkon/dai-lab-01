package ch.heigvd.dai.algorithms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LZW extends LosslessAlgorithm {
  private Map<String, Integer> dictionary;
  private int initial_size;

  public LZW() {
    this(256);
  }

  public LZW(int initial_size) {
    this.initial_size = initial_size;
    this.dictionary = new HashMap<>(this.initial_size);
  }

  @Override
  public void compress(String input, String output) {
    for (int i = 0; i < this.initial_size; i++) {
      this.dictionary.put("" + (char) i, i);
    }

    // w is the next char
    // each time, we peek the next char to check if it already is in the dictionnary
    String w = "";
    try (
        Reader reader = new FileReader(input, StandardCharsets.UTF_8);
        BufferedReader buffered_reader = new BufferedReader(reader);
        Writer writer = new FileWriter(output, StandardCharsets.UTF_8);
        BufferedWriter buffered_writer = new BufferedWriter(writer)) {

      int c;
      while ((c = buffered_reader.read()) != -1) {
        char current_char = (char) c;
        String wc = w + current_char;

        if (this.dictionary.containsKey(wc)) {
          w = wc;
        } else {
          buffered_writer.write(this.dictionary.get(w) + "");

          this.dictionary.put(wc, this.dictionary.size());
          // Reset w to the current character
          w = "" + current_char;
        }
      }

      if (!w.isEmpty()) {
        buffered_writer.write(this.dictionary.get(w) + "");
      }

      // make sure everything is written
      buffered_writer.flush();
    } catch (IOException e) {
      System.err.println("Got error: " + e.getMessage());
    }
  }

  @Override
  void extract(String input, String output) {
  };
}
