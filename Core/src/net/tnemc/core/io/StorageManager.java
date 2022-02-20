package net.tnemc.core.io;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages everything related to Storage Engines and their providers.
 *
 * @see Storage
 * @see StorageProvider
 * @see Loader
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public class StorageManager {

  protected Map<String, StorageProvider> providers = new HashMap<>();

  protected String type;
  protected String host;
  protected Integer port;
  protected String database;
  protected String user;
  protected String password;
  protected String prefix;
  protected String file;
  protected boolean directSave;
  protected boolean cache;
  protected Integer update;
  protected boolean compress;
  protected boolean ssl = false;

  public StorageManager(String type, String host, Integer port, String database, String user,
                        String password, String prefix, String file, boolean directSave,
                        boolean cache, Integer update, boolean compress) {
    this.type = type;
    this.host = host;
    this.port = port;
    this.database = database;
    this.user = user;
    this.password = password;
    this.prefix = prefix;
    this.file = file;
    this.directSave = directSave;
    this.cache = cache;
    this.update = update;
    this.compress = compress;
  }

  public Map<String, StorageProvider> getProviders() {
    return providers;
  }

  public boolean registerProvider(Class<? extends StorageProvider> provider) {
    StorageProvider instance = null;
    try {
      instance = provider.getConstructor().newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if(instance == null) return false;
    this.providers.put(instance.identifier(), instance);
    return true;
  }

  public void registerProviders(HashMap<String, Class<? extends StorageProvider>> providers) {
    for(Class<? extends StorageProvider> provider : providers.values()) {
      if(registerProvider(provider)) {
        //TODO: Log registered storage provider
      }
    }
  }

  public String getHost() {
    return host;
  }

  public Integer getPort() {
    return port;
  }

  public String getDatabase() {
    return database;
  }

  public String getUser() {
    return user;
  }

  public String getPassword() {
    return password;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getType() {
    return type;
  }

  public boolean isDirectSave() {
    return directSave;
  }

  public boolean canCache() {
    return cache;
  }

  public Integer getUpdate() {
    return update;
  }

  public String getFile() {
    return file;
  }

  public boolean isCompress() {
    return compress;
  }

  public boolean isSsl() {
    return ssl;
  }

  public void setSsl(boolean ssl) {
    this.ssl = ssl;
  }

  public StorageProvider provider() {
    return providers.get(type);
  }
}