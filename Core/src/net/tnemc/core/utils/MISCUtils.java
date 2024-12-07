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
import java.util.Date;
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

  public static void setComment(final YamlDocument yaml, final String route, final String comment) {

    setComment(yaml, route, Collections.singletonList(comment));
  }

  public static void setComment(final YamlDocument yaml, final String route, final List<String> comments) {

    if(yaml.contains(route)) {
      yaml.getBlock(route).setComments(comments);
    }
  }

  /**
   * Checks if the time difference between a given date and the current date is greater than or equal to the specified number of minutes.
   *
   * @param date the date to compare
   * @param minutes the number of minutes to compare against
   * @return true if the time difference is greater than or equal to the specified minutes, false otherwise
   */
  public static boolean isTimeDifferenceGreaterOrEqual(final Date date, final int minutes) {

    final Date now = new Date();

    final long diffInMillis = now.getTime() - date.getTime();

    final long minutesInMillis = minutes * 60 * 1000;

    return diffInMillis >= minutesInMillis;
  }

  public static String randomString(final int length) {

    final Random rand = new Random();
    final StringBuilder identifierBuilder = new StringBuilder();
    for(int i = 0; i < length; i++) {
      final char c = (char)('A' + rand.nextInt(26));
      identifierBuilder.append(c);
    }
    return identifierBuilder.toString();
  }

  public static void deleteFolder(final File folder) {

    final File[] files = folder.listFiles();
    if(files != null) { //some JVMs return null for empty dirs
      for(final File f : files) {
        if(f.isDirectory()) {
          deleteFolder(f);
        } else {
          f.delete();
        }
      }
    }
    folder.delete();
  }

  public static void zipFolder(final File sourceFolder, final String destinationZipFile) throws IOException {

    try(final FileOutputStream fos = new FileOutputStream(destinationZipFile);
        final ZipOutputStream zos = new ZipOutputStream(fos)) {

      //zipFolder(sourceFolder, sourceFolder.getName(), zos);

      zipFile(sourceFolder, sourceFolder.getName(), zos);
      zos.closeEntry();
      zos.flush();
      zos.close();
      fos.flush();
    } catch(final Exception e) {
      e.printStackTrace();
    }
  }

  public static void zipFile(final File fileToZip, final String fileName, final ZipOutputStream zipOut) throws IOException {

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

      final File[] children = fileToZip.listFiles();
      if(children == null) {
        return;
      }

      for(final File childFile : children) {
        zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
      }
      return;
    }

    final FileInputStream fis = new FileInputStream(fileToZip);
    final ZipEntry zipEntry = new ZipEntry(fileName);
    zipOut.putNextEntry(zipEntry);

    final byte[] bytes = new byte[1024];
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

    for(final File file : files) {
      if(file.isDirectory()) {
        zipFolder(file, parentFolder + File.separator + file.getName(), zos);
        continue;
      }

      final ZipEntry zipEntry = new ZipEntry(parentFolder + File.separator + file.getName());
      zos.putNextEntry(zipEntry);

      try(final FileInputStream fis = new FileInputStream(file)) {
        final byte[] buffer = new byte[1024];
        int length;
        while((length = fis.read(buffer)) > 0) {
          zos.write(buffer, 0, length);
        }
      }
      zos.closeEntry();
    }
  }
}