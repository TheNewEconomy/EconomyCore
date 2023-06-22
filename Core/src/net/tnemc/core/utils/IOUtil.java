package net.tnemc.core.utils;

import net.tnemc.core.TNECore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class IOUtil {

  /**
   * Used to trust self-signed SSL certificates during an HTTP stream reading session.
   *
   * @return The {@link TrustManager} array.
   */
  public static TrustManager[] selfCertificates() {
    return new TrustManager[] {
        new X509TrustManager() {
          public X509Certificate[] getAcceptedIssuers() {
            return null;
          }

          public void checkClientTrusted(X509Certificate[] certs, String authType) {
            //nothing to see here
          }

          public void checkServerTrusted(X509Certificate[] certs, String authType) {
            //nothing to see here
          }
        }
    };
  }

  /**
   * Used to read the current TNE release from tnemc.net.
   * @return The version read, or an empty Optional if the call timed out.
   */
  public static Optional<String> readVersion() {
    String build = null;
    try {
      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, selfCertificates(), new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

      URL url = new URL("https://tnemc.net/files/tnebuild.txt");
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      connection.setReadTimeout(3000);

      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      build = in.readLine();
      in.close();
    } catch (Exception e) {
      e.printStackTrace();
      TNECore.log().warning("Unable to contact update server!");
    }
    return Optional.ofNullable(build);
  };

  /**
   * Used to find the absolute path based on a case-insensitive file name, in a directory.
   * @param file The file name to use for the search.
   * @param directory The directory to search in.
   * @return An optional containing the path, or an empty optional if unable to locate the file in
   * the directory.
   */
  public static Optional<String> findFileInsensitive(final String file, final File directory) {
    File[] jars = directory.listFiles((dir, name) -> name.endsWith(".jar"));

    if(jars != null) {
      for (File jar : jars) {
        if(jar.getAbsolutePath().toLowerCase().contains(file.toLowerCase() + ".jar")) {
          return Optional.of(jar.getAbsolutePath());
        }
      }
    }
    return Optional.empty();
  }

  public static File[] getYAMLs(final File directory) {
    return directory.listFiles((dir, name) -> name.endsWith(".yml"));
  }

  /**
   * Used to load a specific file from a jar at a path.
   * @param path The path of the jar file.
   * @param file The name of the file to load from the jar.
   * @param callback The Consumer function that intakes the {@link BufferedReader} obtained during
   * loading.
   */
  public static void loadFileFromJar(final File path, final String file, Consumer<Optional<BufferedReader>> callback) {
    JarFile jar = null;
    InputStream in = null;
    BufferedReader reader = null;

    try {
      jar = new JarFile(path);

      JarEntry infoFile = jar.getJarEntry(file);

      if(infoFile == null) {
        TNECore.log().error("Error encountered while loading module: " + path + ". No module.info file found.");
        return;
      }

      in = jar.getInputStream(infoFile);
      reader = new BufferedReader(new InputStreamReader(in));

      callback.accept(Optional.ofNullable(reader));

    } catch(IOException ignore) {
      TNECore.log().error("Something went wrong during loading.");
      callback.accept(Optional.empty());
    } finally {
      if(jar != null) {
        try {
          jar.close();
        } catch(IOException ignore) {
          TNECore.log().error("Something went wrong while closing the jar IO.");
        }
      }

      if(in != null) {
        try {
          in.close();
        } catch(IOException ignore) {
          TNECore.log().error("Something went wrong while closing the InputStream IO.");
        }
      }

      if(reader != null) {
        try {
          reader.close();
        } catch(IOException ignore) {
          TNECore.log().error("Something went wrong while closing the BufferedReader IO.");
        }
      }
    }
  }
}