package ch.heigvd.dai.wrappers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

public class TarWrapper {
  public static void add(TarArchiveOutputStream tar_stream, File file, String parent) throws IOException {
    String entryName = parent + file.getName();
    TarArchiveEntry tarEntry = new TarArchiveEntry(file, entryName);

    if (file.isDirectory()) {
      tar_stream.putArchiveEntry(tarEntry);
      tar_stream.closeArchiveEntry();

      File[] children = file.listFiles();
      if (children != null) {
        for (File child : children) {
          add(tar_stream, child, entryName + "/");
        }
      }
    } else {
      tar_stream.putArchiveEntry(tarEntry);

      try (FileInputStream fis = new FileInputStream(file)) {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
          tar_stream.write(buffer, 0, length);
        }
      }

      tar_stream.closeArchiveEntry();
    }
  }

  public static void extract(File archive, File output_directory) throws IOException {
    if (!output_directory.exists()) {
      output_directory.mkdirs();
    }

    try (FileInputStream fis = new FileInputStream(archive);
        TarArchiveInputStream tar_stream = new TarArchiveInputStream(fis)) {

      TarArchiveEntry entry;

      while ((entry = tar_stream.getNextEntry()) != null) {
        File extracted_file = new File(output_directory, entry.getName());

        if (entry.isDirectory()) {
          extracted_file.mkdirs();
        } else {
          File parentDir = extracted_file.getParentFile();
          if (!parentDir.exists()) {
            parentDir.mkdirs();
          }

          try (FileOutputStream fos = new FileOutputStream(extracted_file)) {
            byte[] buffer = new byte[1024];
            int len;

            while ((len = tar_stream.read(buffer)) > 0) {
              fos.write(buffer, 0, len);
            }
          }
        }
      }
    }
  }
}
