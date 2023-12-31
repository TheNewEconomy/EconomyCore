package net.tnemc.core.config;

/*
 * The New Economy
 * Copyright (C) 2022 - 2023 Daniel "creatorfromhell" Vidmar
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

import net.tnemc.core.TNECore;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Represents a configuration file.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public abstract class Config {

  protected YamlFile yaml;

  protected final File file;
  protected final String defaults;
  private boolean create = false;

  protected final List<String> nodes;

  public Config(final String fileName, String defaults, String... nodes) {
    this.defaults = defaults;
    this.nodes = List.of(nodes);
    file = new File(TNECore.directory(), fileName);


    if(!file.exists()) {
      TNECore.log().error("Configuration doesn't exist! File Name:" + fileName);
      create = true;
    }

    this.yaml = new YamlFile(file.getPath());
  }

  public YamlFile getYaml() {
    return yaml;
  }

  public void setYaml(YamlFile yaml) {
    this.yaml = yaml;
  }

  public boolean load() {

    if(create) {
      saveDefaults();
    }

    try {
      this.yaml.loadWithComments();
      return true;
    } catch(Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public void saveDefaults() {
    TNECore.server().saveResource(defaults, false);
  }

  public boolean save() {
    try {
      yaml.save(file);
      return true;
    } catch(IOException e) {
      TNECore.log().error("Error while saving config \"" + nodes.get(0) + "\".");
      e.printStackTrace();
      return false;
    }
  }
}