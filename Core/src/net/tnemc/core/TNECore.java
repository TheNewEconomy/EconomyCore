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
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.HoldingsType;
import net.tnemc.core.api.BaseAPI;
import net.tnemc.core.api.CallbackManager;
import net.tnemc.core.api.TNEAPI;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.core.compatibility.LogProvider;
import net.tnemc.core.compatibility.ServerConnector;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.compatibility.scheduler.ChoreExecution;
import net.tnemc.core.compatibility.scheduler.ChoreTime;
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
import net.tnemc.core.module.ModuleLoader;
import net.tnemc.menu.core.MenuManager;
import revxrsal.commands.CommandHandler;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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
  protected EconomyManager economyManager;
  protected CommandHandler command;

  private MainConfig config;
  private DataConfig data;
  private MessageConfig messageConfig;
  private final MessageHandler messenger;

  /* Plugin Instance */
  private static TNECore instance;

  private TNEAPI api;
  protected CallbackManager callbackManager;

  protected ModuleLoader loader;

  private boolean enabled = false;

  protected UUID serverAccount;

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

    TNECore.log().inform("Loading configurations!");
    if(!this.config.load()) {
      TNECore.log().error("Failed to load main configuration!");
    }

    if(!this.data.load()) {
      TNECore.log().error("Failed to load data configuration!");
    }

    if(!this.messageConfig.load()) {
      TNECore.log().error("Failed to load message configuration!");
    }

    this.callbackManager = new CallbackManager();

    this.economyManager = new EconomyManager();

    this.economyManager.currency().load(directory, false);
    this.economyManager.currency().saveCurrenciesUUID(directory);

    //set our debug options.
    this.level = DebugLevel.fromID(MainConfig.yaml().getString("Core.Debugging.Mode"));

    this.storage = new StorageManager();
    this.storage.loadAll(Account.class, "");

    if(MainConfig.yaml().getBoolean("Core.Server.Account.Enabled")) {

      logger.inform("Checking Server Account.");

      final String name = MainConfig.yaml().getString("Core.Server.Account.Name");
      serverAccount = UUID.nameUUIDFromBytes(("NonPlayer:" + name).getBytes(StandardCharsets.UTF_8));
      if(economyManager.account().findAccount(serverAccount.toString()).isEmpty()) {

        logger.inform("Creating Server Account.");

        final AccountAPIResponse response = economyManager.account().createAccount(serverAccount.toString(), name, true);
        if(response.getResponse().success()) {

          logger.inform("Server Account has been created.");

          final BigDecimal defaultBalance = new BigDecimal(MainConfig.yaml().getString("Core.Server.Account.Balance"));
          if(defaultBalance.compareTo(BigDecimal.ZERO) > 0) {
            response.getAccount().ifPresent(value->value.setHoldings(new HoldingsEntry(economyManager.region().defaultRegion(),
                                                                         economyManager.currency().getDefaultCurrency().getUid(),
                                                                         defaultBalance,
                                                                         HoldingsType.NORMAL_HOLDINGS
            )));
          }
        } else {
          logger.error("Unable to create Server Account. Reason: " + response.getResponse().response());
        }
      }
    }

    new MenuManager();
    MenuManager.instance().addMenu(new MyEcoMenu());
    MenuManager.instance().addMenu(new MyCurrencyMenu());
    MenuManager.instance().addMenu(new MyBalMenu());

    //Setup the autosaver if enabled.
    if(DataConfig.yaml().getBoolean("Data.AutoSaver.Enabled")) {

      server.scheduler().createRepeatingTask(()->{
        storage.storeAll();
      }, new ChoreTime(0),
         new ChoreTime(DataConfig.yaml().getInt("Data.AutoSaver.Interval"), TimeUnit.SECONDS),
         ChoreExecution.SECONDARY);
    }
  }

  public void onDisable() {
    //TODO: disable modules.
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

  public static CallbackManager callbacks() {
    return instance.callbackManager;
  }

  public static TNEAPI api() {
    return instance.api;
  }

  public static ModuleLoader loader() {
    return instance.loader;
  }

  public DebugLevel getLevel() {
    return level;
  }

  public void setLevel(DebugLevel level) {
    this.level = level;
  }

  public CommandHandler command() {
    return command;
  }

  public UUID getServerAccount() {
    return serverAccount;
  }
}
