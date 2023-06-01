package net.tnemc.core.module;

import net.tnemc.core.TNECore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Optional;

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
public class ModuleUpdateChecker {

  private String module;
  private String updateURL;
  private String oldVersion;
  private String current = "";
  private String jarURL = "";

  public ModuleUpdateChecker(String module, String updateURL, String oldVersion) {
    this.module = module;
    this.updateURL = updateURL;
    this.oldVersion = oldVersion;
  }
  public void check() {
    TNECore.log().inform("Checking module for update: " + module);
    if(readInformation()) {
      if(!upToDate()) {
        TNECore.log().inform("Updating module: " + module);
        if(download(module, jarURL)) {
          TNECore.log().inform("Downloaded module update for " + module);
        } else {
          TNECore.log().inform("Failed to download module update for " + module);
        }
        TNECore.loader().load(module);
      } else {
        TNECore.log().inform("Module " + module + " is up to date.");
      }
    }
  }

  public boolean upToDate() {
    if(current.trim().equalsIgnoreCase("")) {
      return true;
    }

    String[] oldSplit = oldVersion.split("\\.");
    String[] currentSplit = current.split("\\.");

    for(int i = 0; i < currentSplit.length; i++) {

      if(i >= oldSplit.length && !currentSplit[i].equalsIgnoreCase("0")) return false;
      if(i >= oldSplit.length && currentSplit[i].equalsIgnoreCase("0")) continue;

      if(Integer.valueOf(currentSplit[i]) > Integer.valueOf(oldSplit[i])) return false;
    }
    return true;
  }

  public static boolean download(String module, String jarURL) {
    if(jarURL.trim().equalsIgnoreCase("")) {
      return false;
    }

    if(TNECore.loader().hasModule(module)) {
      TNECore.loader().unload(module);
    }

    try {

      TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
      }};

      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, trustAllCerts, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

      URL url = new URL(jarURL);
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        String fileName = jarURL.substring(jarURL.lastIndexOf("/") + 1);

        InputStream in = connection.getInputStream();
        File file = new File(TNECore.directory() + File.separator + "modules", fileName);

        if(file.exists()) {
          if(!file.renameTo(new File(TNECore.directory() + File.separator + "modules", "outdated-" + fileName))) {
            return false;
          }
        }

        FileOutputStream out = new FileOutputStream(file);

        int bytesRead = -1;
        byte[] buffer = new byte[4096];
        while ((bytesRead = in.read(buffer)) != -1) {
          out.write(buffer, 0, bytesRead);
        }

        out.close();
        in.close();
        return true;
      }
    } catch (Exception e) {
      return false;
    }
    return false;
  }

  public boolean readInformation() {
    Optional<Document> document = readUpdateURL(updateURL);
    if(document.isPresent()) {
      Document doc = document.get();

      NodeList mainNodes = doc.getElementsByTagName("modules");
      if(mainNodes != null && mainNodes.getLength() > 0) {
        Node modulesNode = mainNodes.item(0);
        Element element = (Element)modulesNode;

        NodeList modules = element.getElementsByTagName("module");

        for(int i = 0; i < modules.getLength(); i++) {
          Node moduleNode = modules.item(i);

          if(moduleNode.hasAttributes()) {

            Node nameNode = moduleNode.getAttributes().getNamedItem("name");
            if (nameNode != null) {

              if (nameNode.getTextContent().equalsIgnoreCase(module)) {

                Node releasedNode = moduleNode.getAttributes().getNamedItem("released");
                if (releasedNode != null) {
                  if(releasedNode.getTextContent().equalsIgnoreCase("yes")) {

                    //We have the correct name, and this module is released.
                    Element moduleElement = (Element)moduleNode;

                    NodeList versions = moduleElement.getElementsByTagName("versions");

                    if(versions != null && versions.getLength() > 0) {

                      NodeList versionsNodes = moduleElement.getElementsByTagName("version");

                      for(int v = 0; v < versionsNodes.getLength(); v++) {

                        Node versionNode = versionsNodes.item(v);
                        if(versionNode != null && versionNode.hasAttributes()) {

                          Element versionElement = (Element)versionNode;

                          Node latest = versionNode.getAttributes().getNamedItem("latest");
                          Node versionReleased = versionNode.getAttributes().getNamedItem("released");

                          if(latest != null && latest.getTextContent().equalsIgnoreCase("yes") &&
                          versionReleased != null && versionReleased.getTextContent().equalsIgnoreCase("yes")) {

                            //We have the latest module version
                            NodeList name = versionElement.getElementsByTagName("name");
                            NodeList jar = versionElement.getElementsByTagName("jar");

                            if(name != null && name.getLength() > 0) {
                              this.current = name.item(0).getTextContent();
                            }

                            if(jar != null && jar.getLength() > 0) {
                              this.jarURL = jar.item(0).getTextContent();
                            }
                            return true;
                          }
                        }
                      }

                    }

                  }

                }
              }
            }
          }
        }
      }
    }
    return false;
  }

  public static Optional<Document> readUpdateURL(String updateURL) {
    try {

      TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
      }};

      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, trustAllCerts, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

      URL url = new URL(updateURL);
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.parse(connection.getInputStream());

      return Optional.of(document);

    } catch(Exception e) {
      return Optional.empty();
    }
  }

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public String getURL() {
    return updateURL;
  }

  public void setURL(String updateURL) {
    this.updateURL = updateURL;
  }

  public String getOldVersion() {
    return oldVersion;
  }

  public void setOldVersion(String oldVersion) {
    this.oldVersion = oldVersion;
  }

  public String getCurrent() {
    return current;
  }

  public void setCurrent(String current) {
    this.current = current;
  }

  public String getJarURL() {
    return jarURL;
  }

  public void setJarURL(String jarURL) {
    this.jarURL = jarURL;
  }
}