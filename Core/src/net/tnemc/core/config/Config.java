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

import net.tnemc.core.TNECore;
import org.simpleyaml.configuration.comments.format.YamlCommentFormat;
import org.simpleyaml.configuration.file.YamlConfiguration;
import org.simpleyaml.configuration.file.YamlFile;
import org.simpleyaml.configuration.implementation.api.QuoteStyle;
import org.simpleyaml.exceptions.InvalidConfigurationException;
import org.simpleyaml.utils.SupplierIO;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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

  protected final CommentedConfigurationNode root;

  protected final File file;

  protected final String defaults;
  private boolean create = false;

  protected final List<String> nodes;

  public Config(File file, String defaults, String... nodes) {
    this.file = file;
    this.defaults = defaults;
    this.nodes = List.of(nodes);


    if(!file.exists()) create = true;

    this.yaml = new YamlFile(file);

    try {
      yaml.createOrLoadWithComments();
    } catch(IOException e) {
      e.printStackTrace();
    }

    yaml.options().useComments(true);
    this.yaml.setCommentFormat(YamlCommentFormat.PRETTY);

    System.out.println("Loading config with nodes: " + nodes);

    try {
      String path = "jar:file:\\" + TNECore.directory().getAbsolutePath() + "..\\TNE.jar!/" + defaults;

      URL url = new URL(path.replaceAll(" ", "%20").replaceAll("//", "\\"));

      System.out.println(url.getPath());
      JarURLConnection connection = (JarURLConnection) url.openConnection();
      YamlFile defaultYaml = new YamlFile(new File(connection.getJarFileURL().toURI()));


      defaultYaml.options().useComments(true);
      defaultYaml.setCommentFormat(YamlCommentFormat.PRETTY);
      yaml.setDefaults(defaultYaml);

    } catch(URISyntaxException e) {
      throw new RuntimeException(e);
    } catch(MalformedURLException e) {
      throw new RuntimeException(e);
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    yaml.options().copyDefaults(true);
    try {
      yaml.save();
    } catch(IOException e) {
      e.printStackTrace();
    }
    //load();
  }

  public void addDefaults() {
    if(create) {

    }
  }

  InputStream getResourceUTF8(String filename) {
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

  public boolean load() {

    if(!file.exists()) {
      saveDefaults();
    }

    Commen

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