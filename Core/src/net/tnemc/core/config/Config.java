package net.tnemc.core.config;
/*
 * The New Economy
 * Copyright (C) 2022 Daniel "creatorfromhell" Vidmar
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

import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;

/**
 * Represents a configuration file.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public abstract class Config {

  protected final YamlFile yaml;

  protected final File file;

  protected final String defaults;

  protected final List<String> nodes;

  public Config(File file, String defaults, String... nodes) {
    this.file = file;
    this.defaults = defaults;
    this.nodes = List.of(nodes);

    this.yaml = new YamlFile(file);
  }

  public boolean load() {

    if(!file.exists()) {
      saveDefaults();
    }

    try {
      yaml.createOrLoadWithComments();
    } catch(IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public void saveDefaults() {

    final URL url = getClass().getResource(defaults);

    if(url != null) {

      try {
        final URLConnection connection = url.openConnection();

        try(InputStream stream = connection.getInputStream()) {

          Files.copy(stream, file.toPath());
        }


      } catch(IOException e) {
        e.printStackTrace();
      }
    }
  }

  public boolean save() {
    try {
      yaml.save();
      return true;
    } catch(IOException e) {
      e.printStackTrace();
      return false;
    }
  }
}