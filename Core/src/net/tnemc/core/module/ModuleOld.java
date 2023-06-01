package net.tnemc.core.module;

import net.tnemc.core.TNECore;
import net.tnemc.core.manager.DataManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public interface ModuleOld {

  default void load(TNECore plugin) {}

  default void postLoad(TNECore plugin) {}

  default void unload(TNECore plugin) {}

  default void backup(DataManager manager) {}

  default void enableSave(DataManager manager) {}

  default void disableSave(DataManager manager) {}

  default void initializeConfigurations() {}

  default void loadConfigurations() {}

  default void saveConfigurations() {}

  /**
   * @return A TNDBU(https://github.com/TheNewEconomy/TheNewDBUpdater) compliant YAML file containing the SQL tables required
   * for the module.
   */
  default String tablesFile() {
    return "";
  }

  /**
   * @return A map of messages that should be added to message.yml in the format of YamlNode, Value
   */
  default Map<String, String> messages() {
    return new HashMap<>();
  }


  default List<String> events() {
    return new ArrayList<>();
  }


  /**
   * Load a file from the classpath and return the InputStream.
   * @param filename The name of the file.
   * @return The input stream for the file.
   */
  default InputStream getResource(String filename) {
    try {
      URL url = getClass().getClassLoader().getResource(filename);
      if (url == null) {
        return null;
      } else {
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        return connection.getInputStream();
      }
    } catch (IOException var4) {
      return null;
    }
  }
}