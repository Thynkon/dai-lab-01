package ch.heigvd.dai.algorithms;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LZW extends LosslessAlgorithm {
  enum Mode {
    COMPRESSION,
    EXTRACTION
  };

  private Map<String, Integer> compression_dictionary;
  private Map<Integer, String> extraction_dictionary;
  private int initial_size;

  public LZW() {
    this(256);
  }

  public LZW(int initial_size) {
    this.initial_size = initial_size;
    this.compression_dictionary = new HashMap<>(this.initial_size);
    this.extraction_dictionary = new HashMap<>(this.initial_size);
  }

  private void initialize_dictionary(Mode mode) {

    switch (mode) {
      case COMPRESSION:
        this.compression_dictionary.clear();
        for (int i = 0; i < this.initial_size; i++) {
          this.compression_dictionary.put(String.valueOf((char) i), i);
        }
        break;
      case EXTRACTION:
        this.extraction_dictionary.clear();
        for (int i = 0; i < this.initial_size; i++) {
          this.extraction_dictionary.put(i, String.valueOf((char) i));
        }
        break;
    }
  }

  @Override
  public void compress(File input, File output) {
    this.initialize_dictionary(Mode.COMPRESSION);
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

        if (this.compression_dictionary.containsKey(wc)) {
          w = wc;
        } else {
          buffered_writer.write(this.compression_dictionary.get(w));

          this.compression_dictionary.put(wc, this.compression_dictionary.size());
          // Reset w to the current character
          w = "" + current_char;
        }
      }

      if (!w.isEmpty()) {
        buffered_writer.write(this.compression_dictionary.get(w));
      }

      // make sure everything is written
      buffered_writer.flush();
    } catch (IOException e) {
      System.err.println("Got error: " + e.getMessage());
    }
  }

  @Override
  public void extract(File input, File output) {
    this.initialize_dictionary(Mode.EXTRACTION);

    try (
        Reader reader = new FileReader(input, StandardCharsets.UTF_8);
        BufferedReader buffered_reader = new BufferedReader(reader);
        OutputStream writer = new FileOutputStream(output);
        BufferedOutputStream buffered_writer = new BufferedOutputStream(writer)) {

      int current_code = buffered_reader.read();
      int previous_code = current_code;

      buffered_writer.write(this.extraction_dictionary.get(current_code).getBytes());
      while ((current_code = buffered_reader.read()) != -1) {
        String previous_current;

        if (this.extraction_dictionary.containsKey(current_code)) {
          previous_current = this.extraction_dictionary.get(current_code);
        } else if (current_code == this.extraction_dictionary.size()) {
          previous_current = this.extraction_dictionary.get(previous_code)
              + this.extraction_dictionary.get(previous_code).charAt(0);
        } else {
          throw new IllegalStateException("Invalid code: " + current_code);
        }
        buffered_writer.write(previous_current.getBytes());
        this.extraction_dictionary.put(this.extraction_dictionary.size(),
            this.extraction_dictionary.get(previous_code) + previous_current.charAt(0));
        previous_code = current_code;
      }
      // make sure everything is written
      buffered_writer.flush();
    } catch (IOException e) {
      System.err.println("Got error: " + e.getMessage());
    }
  };
}
