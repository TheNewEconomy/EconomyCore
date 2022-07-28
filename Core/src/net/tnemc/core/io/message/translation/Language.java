package net.tnemc.core.io.message.translation;
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

/**
 * Language
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public  class Language {

  private final YamlFile config;

  private final String name;

  public Language(String name, YamlFile config) {
    this.name = name;
    this.config = config;
  }

  public YamlFile getConfig() {
    return config;
  }

  public String getName() {
    return name;
  }

  public boolean hasTranslation(String node) {
    return config.contains(node);
  }

  public String getTranslation(String node) {
    return config.getString(node);
  }
}