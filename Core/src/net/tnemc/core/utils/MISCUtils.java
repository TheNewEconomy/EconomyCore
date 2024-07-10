package net.tnemc.core.utils;

/*
 * The New Economy
 * Copyright (C) 2022 - 2024 Daniel "creatorfromhell" Vidmar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import dev.dejvokep.boostedyaml.YamlDocument;
import net.tnemc.core.config.MessageConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * MISCUtils
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class MISCUtils {

  public static String worldFormatted(final String world) {
    return MessageConfig.yaml().getString("Messages.Worlds." + world, world);
  }

  public static void setComment(YamlDocument yaml, String route, String comment) {
    setComment(yaml, route, Collections.singletonList(comment));
  }

  public static void setComment(YamlDocument yaml, String route, List<String> comments) {

    if(yaml.contains(route)) {
      yaml.getBlock(route).setComments(comments);
    }
  }

  public static String randomString(final int length) {
    final Random rand = new Random();
    final StringBuilder identifierBuilder = new StringBuilder();
    for(int i = 0; i < length; i++) {
      char c = (char)('A' + rand.nextInt(26));
      identifierBuilder.append(c);
    }
    return identifierBuilder.toString();
  }

  public static void deleteFolder(File folder) {
    File[] files = folder.listFiles();
    if(files!=null) { //some JVMs return null for empty dirs
      for(File f: files) {
        if(f.isDirectory()) {
          deleteFolder(f);
        } else {
          f.delete();
        }
      }
    }
    folder.delete();
  }

  public static void zipFolder(File sourceFolder, String destinationZipFile) throws IOException {
    try(FileOutputStream fos = new FileOutputStream(destinationZipFile);
        ZipOutputStream zos = new ZipOutputStream(fos)) {

      //zipFolder(sourceFolder, sourceFolder.getName(), zos);

      zipFile(sourceFolder, sourceFolder.getName(), zos);
      zos.closeEntry();
      zos.flush();
      zos.close();
      fos.flush();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
    if(fileToZip.isHidden()) {
      return;
    }

    if(fileToZip.isDirectory()) {

      if(fileName.endsWith("/")) {

        zipOut.putNextEntry(new ZipEntry(fileName));
        zipOut.closeEntry();
      } else {

        zipOut.putNextEntry(new ZipEntry(fileName + "/"));
        zipOut.closeEntry();
      }

      File[] children = fileToZip.listFiles();
      if(children == null) {
        return;
      }

      for(File childFile : children) {
        zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
      }
      return;
    }

    FileInputStream fis = new FileInputStream(fileToZip);
    ZipEntry zipEntry = new ZipEntry(fileName);
    zipOut.putNextEntry(zipEntry);

    byte[] bytes = new byte[1024];
    int length;

    while((length = fis.read(bytes)) >= 0) {
      zipOut.write(bytes, 0, length);
    }
    fis.close();
  }

  public static void zipFolder(final File folder, final String parentFolder, final ZipOutputStream zos) throws IOException {

    if(folder == null || folder.exists()) {
      return;
    }

    final File[] files = folder.listFiles();
    if(files == null) {
      return;
    }

    for (File file : files) {
      if (file.isDirectory()) {
        zipFolder(file, parentFolder + File.separator + file.getName(), zos);
        continue;
      }

      final ZipEntry zipEntry = new ZipEntry(parentFolder + File.separator + file.getName());
      zos.putNextEntry(zipEntry);

      try (FileInputStream fis = new FileInputStream(file)) {
        final byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
          zos.write(buffer, 0, length);
        }
      }
      zos.closeEntry();
    }
  }
}