package net.tnemc.core;

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

import net.tnemc.core.account.Account;
import net.tnemc.core.api.BaseAPI;
import net.tnemc.core.api.TNEAPI;
import net.tnemc.core.compatibility.LogProvider;
import net.tnemc.core.compatibility.ServerConnector;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.config.DataConfig;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.config.MessageConfig;
import net.tnemc.core.io.message.MessageHandler;
import net.tnemc.core.io.message.TranslationProvider;
import net.tnemc.core.io.message.translation.BaseTranslationProvider;
import net.tnemc.core.io.storage.StorageManager;
import net.tnemc.core.menu.impl.mybal.MyBalMenu;
import net.tnemc.core.menu.impl.mycurrency.MyCurrencyMenu;
import net.tnemc.core.menu.impl.myeco.MyEcoMenu;
import net.tnemc.menu.core.MenuManager;

import java.io.File;
import java.util.regex.Pattern;

/**
 * The core class of TNE which should be used within each implementation's class.
 *
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public abstract class TNECore {

  /*
   * Core final variables utilized within TNE.
   */
  public static final String coreURL = "https://tnemc.net/files/module-version.xml";
  public static final Pattern UUID_MATCHER_PATTERN = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");
  public static final String version = "1.2.0.0";
  public static final String build = "SNAPSHOT-1";

  /* Core non-final variables utilized within TNE as settings */
  protected File directory;

  //The DebugLevel that the server is currently running in.
  private DebugLevel level = DebugLevel.STANDARD;

  /* Key Managers and Object instances utilized with TNE */

  //General Key Object Instances
  protected LogProvider logger;

  //Manager Instances
  protected ServerConnector server;
  protected StorageManager storage;
  protected final EconomyManager economyManager = new EconomyManager();

  private MainConfig config;
  private DataConfig data;
  private MessageConfig messageConfig;
  private final MessageHandler messenger;

  /* Plugin Instance */
  private static TNECore instance;

  private TNEAPI api;

  private boolean enabled = false;

  public TNECore(ServerConnector server, LogProvider logger) {
    this.server = server;
    this.logger = logger;
    this.messenger = new MessageHandler(new BaseTranslationProvider());
  }

  public TNECore(ServerConnector server, LogProvider logger, TranslationProvider provider) {
    this.server = server;
    this.logger = logger;
    this.messenger = new MessageHandler(provider);
  }

  public static void setInstance(TNECore core) {
    if(instance == null) {
      instance = core;
    } else {
      throw new IllegalStateException("TNE has already been initiated. Please refrain from attempting" +
                                          "to modify the instance variable.");
    }
  }

  public void enable() {
    if(!enabled) {

      this.enabled = true;
      this.api = new BaseAPI();
      onEnable();

    } else {
      throw new IllegalStateException("TNE has already been enabled!");
    }
  }

  /**
   * Used to enable the core. This should contain things that can't be initialized until after the
   * server software is operational.
   */
  protected void onEnable() {

    if(!directory.exists()) {
      directory.mkdir();
    }

    this.config = new MainConfig();
    this.data = new DataConfig();
    this.messageConfig = new MessageConfig();

    this.config.load();
    this.data.load();
    this.messageConfig.load();

    this.storage = new StorageManager(data);
    this.storage.loadAll(Account.class, "");

    if(economyManager.account().findAccount("98a37f1a-e431-4eb2-9415-5db53b566436").isEmpty()) {
      economyManager.account().createAccount("98a37f1a-e431-4eb2-9415-5db53b566436", "Server_Account");
    }

    new MenuManager();
    MenuManager.instance().addMenu(new MyEcoMenu());
    MenuManager.instance().addMenu(new MyCurrencyMenu());
    MenuManager.instance().addMenu(new MyBalMenu());
  }

  /**
   * The implementation's {@link LogProvider}.
   *
   * @return The log provider.
   */
  public static LogProvider log() {
    return instance.logger;
  }

  /**
   * The implementation's {@link EconomyManager}, which is used to manage everything economy related
   * in TNE.
   * @return The {@link EconomyManager Economy Manager}.
   */
  public static EconomyManager eco() {
    return instance.economyManager;
  }

  /**
   * The {@link StorageManager} we are utilizing.
   *
   * @return The {@link StorageManager}.
   */
  public static StorageManager storage() {
    return instance.storage;
  }

  /**
   * The {@link ServerConnector} for the implementation.
   * @return The {@link ServerConnector} for the implementation.
   */
  public static ServerConnector server() {
    return instance.server;
  }

  public MainConfig config() {
    return config;
  }

  public DataConfig data() {
    return data;
  }

  public MessageConfig message() {
    return messageConfig;
  }

  public static MessageHandler messenger() {
    return instance.messenger;
  }

  public static File directory() {
    return instance.directory;
  }

  public static TNECore instance() {
    return instance;
  }

  public static TNEAPI api() {
    return instance.api;
  }

  public DebugLevel getLevel() {
    return level;
  }

  public void setLevel(DebugLevel level) {
    this.level = level;
  }
}
