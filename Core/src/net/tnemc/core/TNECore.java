package net.tnemc.core;

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

import net.tnemc.core.account.Account;
import net.tnemc.core.account.AccountStatus;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.api.TNEAPI;
import net.tnemc.core.api.callback.TNECallbackProvider;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.core.command.parameters.PercentBigDecimal;
import net.tnemc.core.command.parameters.resolver.AccountResolver;
import net.tnemc.core.command.parameters.resolver.BigDecimalResolver;
import net.tnemc.core.command.parameters.resolver.CurrencyResolver;
import net.tnemc.core.command.parameters.resolver.DebugResolver;
import net.tnemc.core.command.parameters.resolver.PercentDecimalResolver;
import net.tnemc.core.command.parameters.resolver.StatusResolver;
import net.tnemc.core.command.parameters.suggestion.AccountSuggestion;
import net.tnemc.core.command.parameters.suggestion.CurrencySuggestion;
import net.tnemc.core.command.parameters.suggestion.DebugSuggestion;
import net.tnemc.core.command.parameters.suggestion.RegionSuggestion;
import net.tnemc.core.command.parameters.suggestion.StatusSuggestion;
import net.tnemc.core.config.DataConfig;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.config.MessageConfig;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.hook.treasury.TreasuryHook;
import net.tnemc.core.io.message.BaseTranslationProvider;
import net.tnemc.core.region.RegionGroup;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.api.CallbackManager;
import net.tnemc.plugincore.core.compatibility.LogProvider;
import net.tnemc.plugincore.core.compatibility.ServerConnector;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import net.tnemc.plugincore.core.compatibility.scheduler.Chore;
import net.tnemc.plugincore.core.compatibility.scheduler.ChoreExecution;
import net.tnemc.plugincore.core.compatibility.scheduler.ChoreTime;
import net.tnemc.plugincore.core.io.message.MessageData;
import net.tnemc.plugincore.core.io.message.MessageHandler;
import net.tnemc.plugincore.core.io.message.TranslationProvider;
import net.tnemc.plugincore.core.io.storage.StorageManager;
import net.tnemc.plugincore.core.module.cache.ModuleFileCache;
import net.tnemc.plugincore.core.utils.UpdateChecker;
import revxrsal.commands.orphan.Orphans;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * The core class of TNE which should be used within each implementation's class.
 *
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public abstract class TNECore extends PluginCore {

  /*
   * Core final variables utilized within TNE.
   */
  public static final String coreURL = "https://tnemc.net/files/module-version.xml";

  public static final String version = "0.1.2.6"; //not in TNPC
  public static final String build = "Release-1"; //not in TNPC

  /* Key Managers and Object instances utilized with TNE */

  //General Key Object Instances
  protected EconomyManager economyManager; //not in TNPC

  private MainConfig config; //not in TNPC
  private DataConfig data; //not in TNPC
  private MessageConfig messageConfig; //not in TNPC
  private Chore<?> autoSaver = null; //not in TNPC

  private final TNEAPI api = new TNEAPI(); //not in TNPC
  protected UUID serverAccount; //not in TNPC

  public TNECore(ServerConnector server, LogProvider logger) {
    super(server, logger, new BaseTranslationProvider(), new TNECallbackProvider());
    this.server = server;
    this.logger = logger;
  }

  public TNECore(ServerConnector server, LogProvider logger, TranslationProvider provider) {
    super(server, logger, provider, new TNECallbackProvider());
    this.server = server;
    this.logger = logger;
  }

  /**
   * Used to enable the core. This should contain things that can't be initialized until after the
   * server software is operational.
   */
  protected void onEnable() {

    this.config = new MainConfig();
    this.data = new DataConfig();
    this.messageConfig = new MessageConfig();

    TNECore.log().inform("Loading configurations!");
    if(!this.config.load()) {
      TNECore.log().error("Failed to load main configuration!", DebugLevel.OFF);
    }

    if(!this.data.load()) {
      TNECore.log().error("Failed to load data configuration!", DebugLevel.OFF);
    }

    if(!this.messageConfig.load()) {
      TNECore.log().error("Failed to load message configuration!", DebugLevel.OFF);
    }

    registerConfigs();

    //Call initConfigurations for all modules loaded.
    loader.getModules().values().forEach((moduleWrapper -> moduleWrapper.getModule().initConfigurations(directory)));

    registerCallbacks();

    //Register the callback listeners and callbacks for the modules
    loader.getModules().values().forEach((moduleWrapper ->{
      moduleWrapper.getModule().registerCallbacks().forEach((key, entry)->{
        callbackManager.addCallback(key, entry);
      });

      moduleWrapper.getModule().registerListeners().forEach((key, function)->{
        callbackManager.addConsumer(key, function);
      });
    }));

    this.economyManager = new EconomyManager();

    this.economyManager.init();

    if(!this.economyManager.currency().load(directory, false)) {
      return;
    }

    this.economyManager.currency().saveCurrenciesUUID(directory);

    if(server.pluginAvailable("Treasury")) {
      new TreasuryHook().register();
    }

    //set our debug options.
    this.level = DebugLevel.fromID(MainConfig.yaml().getString("Core.Debugging.Mode"));

    //Set our server UUID. This is used for proxy messaging.
    final boolean randomUUID = MainConfig.yaml().getBoolean("Core.Server.RandomUUID", false);

    //Added in build 0.1.2.5-Pre1, removed in 0.1.2.7
    if(!MainConfig.yaml().contains("Core.Server.Geyser")) {
      MainConfig.yaml().set("Core.Server.Geyser", ".");
      MainConfig.yaml().setComment("Core.Server.Geyser", "#The geyser prefix for the server.");

      try {
        MainConfig.yaml().save();
      } catch(IOException e) {
        logger.error("Issue while updating config.yml to config.yml", e, DebugLevel.OFF);
      }
    }

    if(!randomUUID) {
      if(!MainConfig.yaml().contains("Core.ServerID")) {

        serverID = UUID.randomUUID();
        MainConfig.yaml().set("Core.ServerID", serverID.toString());
        MainConfig.yaml().setComment("Core.ServerID", "#Don't modify unless you know what you're doing.");

        try {
          MainConfig.yaml().save();
        } catch(IOException e) {
          logger.error("Issue while saving Server UUID to config.yml", e, DebugLevel.OFF);
        }
      } else {
        serverID = UUID.fromString(MainConfig.yaml().getString("Core.ServerID"));
      }
    } else {
      serverID = UUID.randomUUID();
    }

    if(!this.storage.meetsRequirement()) {
      TNECore.log().error("This server does not meet SQL requirements needed for TNE!", DebugLevel.OFF);
      return;
    }

    this.storage.loadAll(Account.class, "");

    //Call the enableSave method for all modules loaded.
    loader.getModules().values().forEach((moduleWrapper -> moduleWrapper.getModule().enableSave(this.storage)));

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
                                                                         EconomyManager.NORMAL
            )));
          }
        } else {
          logger.error("Unable to create Server Account. Reason: " + response.getResponse().response());
        }
      }
    }

    //register our commands
    command.setHelpWriter((command, actor) -> {
      final MessageData data = new MessageData("Messages.Commands.Help.Entry");
      data.addReplacement("$command", command.getPath().toRealString());
      data.addReplacement("$arguments", MessageHandler.getInstance().getTranslator().translateNode(new MessageData("Messages.Commands." + command.getUsage()), "default"));
      data.addReplacement("$description", MessageHandler.getInstance().getTranslator().translateNode(new MessageData("Messages.Commands." + command.getDescription()), "default"));

      return MessageHandler.getInstance().getTranslator().translateNode(data, "default");
    });

    //Custom Parameters:
    //TODO: Register custom validators

    //Value Resolvers
    command.registerValueResolver(Account.class, new AccountResolver());
    command.registerValueResolver(AccountStatus.class, new StatusResolver());
    command.registerValueResolver(DebugLevel.class, new DebugResolver());
    command.registerValueResolver(Currency.class, new CurrencyResolver());
    command.registerValueResolver(BigDecimal.class, new BigDecimalResolver());
    command.registerValueResolver(PercentBigDecimal.class, new PercentDecimalResolver());

    //Annotation
    command.getAutoCompleter().registerParameterSuggestions(AccountStatus.class, new StatusSuggestion());
    command.getAutoCompleter().registerParameterSuggestions(DebugLevel.class, new DebugSuggestion());
    command.getAutoCompleter().registerParameterSuggestions(RegionGroup.class, new RegionSuggestion());
    command.getAutoCompleter().registerParameterSuggestions(Account.class, new AccountSuggestion());
    command.getAutoCompleter().registerParameterSuggestions(Currency.class, new CurrencySuggestion());

    registerCommands();

    //Call our command methods for the modules.
    loader.getModules().values().forEach((moduleWrapper ->{
      moduleWrapper.getModule().registerCommands(command);

      moduleWrapper.getModule().registerMoneySub().forEach((orphan)->command.register(Orphans.path("money"), orphan));
      moduleWrapper.getModule().registerTransactionSub().forEach((orphan)->command.register(Orphans.path("transaction"), orphan));
      moduleWrapper.getModule().registerAdminSub().forEach((orphan)->command.register(Orphans.path("tne"), orphan));
    }));
    registerMenuHandler();

    //Set up the auto saver if enabled.
    if(DataConfig.yaml().getBoolean("Data.AutoSaver.Enabled")) {

      this.autoSaver = server.scheduler().createRepeatingTask(()->{
        storage.storeAll();
      }, new ChoreTime(0),
         new ChoreTime(DataConfig.yaml().getInt("Data.AutoSaver.Interval"), TimeUnit.SECONDS),
         ChoreExecution.SECONDARY);
    }

    this.moduleCache = new ModuleFileCache();

    if(MainConfig.yaml().getBoolean("Core.Update.Check")) {
      this.updateChecker = new UpdateChecker();

      TNECore.log().inform("Build Stability: " + this.updateChecker.stable());

      if(this.updateChecker.needsUpdate()) {
        TNECore.log().inform("Update Available! Latest: " + this.updateChecker.getBuild());
      }
    }

    economyManager.printInvalid();

    server.scheduler().createDelayedTask(()-> economyManager.getTopManager().load(), new ChoreTime(2), ChoreExecution.SECONDARY);
    server.scheduler().createRepeatingTask(()-> economyManager.getTopManager().load(), new ChoreTime(2), new ChoreTime(MainConfig.yaml().getInt("Core.Commands.Top.Refresh"), TimeUnit.SECONDS), ChoreExecution.SECONDARY);

    //ConfigHandler.send();
  }

  public void onDisable() {

    if(autoSaver != null) {
      autoSaver.cancel();
    }

    //store our data syncly because it needs to finish
    /*final Optional<Datable<?>> data = Optional.ofNullable(storage.getEngine().datables().get(Account.class));
    if(data.isPresent()) {
      data.get().storeAll(storage.getConnector(), null);
    }*/

    super.onDisable();
  }


  public abstract void registerCommands();

  public abstract void registerConfigs();

  public abstract void registerCallbacks();

  /**
   * The implementation's {@link EconomyManager}, which is used to manage everything economy related
   * in TNE.
   * @return The {@link EconomyManager Economy Manager}.
   */
  public static EconomyManager eco() {
    return core().economyManager;
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

  public static TNECore core() {
    return (TNECore)instance();
  }

  public static TNEAPI api() {
    return core().api;
  }

  public DebugLevel getLevel() {
    return level;
  }

  public void setLevel(DebugLevel level) {
    this.level = level;
  }

  public UUID getServerAccount() {
    return serverAccount;
  }
}
