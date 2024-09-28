package net.tnemc.core.config;

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
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import net.tnemc.plugincore.core.config.Config;

import java.util.Collections;

/**
 * DataConfig
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class DataConfig extends Config {

  private static DataConfig instance;

  public DataConfig() {

    super("data.yml", "data.yml", Collections.singletonList("Data"),
          LoaderSettings.builder().setAutoUpdate(true).build(),
          UpdaterSettings.builder().setAutoSave(true).setVersioning(new BasicVersioning("Data.config-version")).build());

    instance = this;
  }

  public static YamlDocument yaml() {

    return instance.getYaml();
  }
}