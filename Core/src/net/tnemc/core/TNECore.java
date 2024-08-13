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

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.AccountStatus;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.api.TNEAPI;
import net.tnemc.core.api.callback.TNECallbacks;
import net.tnemc.core.api.callback.account.AccountCreateCallback;
import net.tnemc.core.api.callback.account.AccountDeleteCallback;
import net.tnemc.core.api.callback.account.AccountLoadCallback;
import net.tnemc.core.api.callback.account.AccountSaveCallback;
import net.tnemc.core.api.callback.account.AccountTypesCallback;
import net.tnemc.core.api.callback.currency.CurrencyDropCallback;
import net.tnemc.core.api.callback.currency.CurrencyLoadCallback;
import net.tnemc.core.api.callback.currency.DenominationLoadCallback;
import net.tnemc.core.api.callback.transaction.PostTransactionCallback;
import net.tnemc.core.api.callback.transaction.PreTransactionCallback;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.core.channel.BalanceHandler;
import net.tnemc.core.channel.SyncHandler;
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
import net.tnemc.core.currency.calculations.ItemCalculations;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.core.io.yaml.YamlStorageManager;
import net.tnemc.core.manager.Updater;
import net.tnemc.core.menu.MyBalMenu;
import net.tnemc.core.menu.MyEcoMenu;
import net.tnemc.core.region.RegionGroup;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.utils.MISCUtils;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.menu.core.manager.MenuManager;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.PluginEngine;
import net.tnemc.plugincore.core.api.CallbackEntry;
import net.tnemc.plugincore.core.api.CallbackManager;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import net.tnemc.plugincore.core.compatibility.scheduler.Chore;
import net.tnemc.plugincore.core.compatibility.scheduler.ChoreExecution;
import net.tnemc.plugincore.core.compatibility.scheduler.ChoreTime;
import net.tnemc.plugincore.core.io.message.MessageData;
import net.tnemc.plugincore.core.io.message.MessageHandler;
import net.tnemc.plugincore.core.io.storage.Datable;
import net.tnemc.plugincore.core.io.storage.StorageManager;
import net.tnemc.plugincore.core.io.storage.engine.StorageSettings;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * The core class of TNE which should be used within each implementation's class.
 *
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public abstract class TNECore extends PluginEngine {

  protected static TNECore instance;

  /*
   * Core final variables utilized within TNE.
   */
  public static final String coreURL = "https://tnemc.net/files/module-version.xml";

  public static final String version = "0.1.3.2";
  public static final String build = "Pre-5";

  /* Key Managers and Object instances utilized with TNE */

  //General Key Object Instances
  protected EconomyManager economyManager;

  protected Updater updater;

  protected final YamlStorageManager yamlManager = new YamlStorageManager();

  private MainConfig config;
  private DataConfig data;
  private MessageConfig messageConfig;
  private Chore<?> autoSaver = null;

  private final TNEAPI api = new TNEAPI();
  protected UUID serverAccount;

  public TNECore() {
    instance = this;
  }

  @Override
  public String versionCheckSite() {
    return "https://tnemc.net/files/tnebuild.txt";
  }

  @Override
  public String version() {
    return version;
  }

  @Override
  public String build() {
    return build;
  }

  @Override
  public void registerConfigs() {

    this.config = new MainConfig();
    this.data = new DataConfig();
    this.messageConfig = new MessageConfig();

    PluginCore.log().inform("Loading configurations!");
    if(!this.config.load()) {
      PluginCore.log().error("Failed to load main configuration!", DebugLevel.OFF);
    }

    if(!this.data.load()) {
      PluginCore.log().error("Failed to load data configuration!", DebugLevel.OFF);
    }

    if(!this.messageConfig.load()) {
      PluginCore.log().error("Failed to load message configuration!", DebugLevel.OFF);
    }
  }

  @Override
  public void registerPluginChannels() {
    PluginCore.instance().getChannelMessageManager().register(new BalanceHandler());
    PluginCore.instance().getChannelMessageManager().register(new SyncHandler());
  }

  @Override
  public void registerStorage() {
    final StorageSettings settings = new StorageSettings(
            DataConfig.yaml().getString("Data.Database.File"),
            DataConfig.yaml().getString("Data.Database.SQL.Host"),
            DataConfig.yaml().getInt("Data.Database.SQL.Port"),
            DataConfig.yaml().getString("Data.Database.SQL.DB"),
            DataConfig.yaml().getString("Data.Database.SQL.User"),
            DataConfig.yaml().getString("Data.Database.SQL.Password"),
            DataConfig.yaml().getString("Data.Database.Prefix"),
            DataConfig.yaml().getBoolean("Data.Database.SQL.PublicKey"),
            DataConfig.yaml().getBoolean("Data.Database.SQL.SSL"),
            "TNE",
            DataConfig.yaml().getInt("Data.Pool.MaxSize"),
            DataConfig.yaml().getLong("Data.Pool.MaxLife"),
            DataConfig.yaml().getLong("Data.Pool.Timeout"),
            DataConfig.yaml().getString("Data.Sync.Type")
            );

    final JedisPoolConfig config = new JedisPoolConfig();
    config.setMaxTotal(DataConfig.yaml().getInt("Data.Sync.Redis.Pool.MaxSize", 10));
    config.setMaxIdle(DataConfig.yaml().getInt("Data.Sync.Redis.Pool.MaxIdle", 10));
    config.setMinIdle(DataConfig.yaml().getInt("Data.Sync.Redis.Pool.MinIdle", 1));

    this.storage = new StorageManager(DataConfig.yaml().getString("Data.Database.Type"),
            new TNEStorageProvider(), settings, new JedisPool(config, DataConfig.yaml().getString("Data.Sync.Redis.Host"),
            DataConfig.yaml().getInt("Data.Sync.Redis.Port"),
            DataConfig.yaml().getInt("Data.Sync.Redis.Timeout"),
            DataConfig.yaml().getString("Data.Sync.Redis.User"),
            DataConfig.yaml().getString("Data.Sync.Redis.Password"),
            DataConfig.yaml().getInt("Data.Sync.Redis.Index"),
            DataConfig.yaml().getBoolean("Data.Sync.Redis.SSL")));
  }

  @Override
  public void registerCommands() {

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
  }

  @Override
  public void registerCallbacks(CallbackManager callbackManager) {
    callbackManager.addCallback(TNECallbacks.ACCOUNT_TYPES.toString(), new CallbackEntry(AccountTypesCallback.class));
    callbackManager.addCallback(TNECallbacks.ACCOUNT_LOAD.toString(), new CallbackEntry(AccountLoadCallback.class));
    callbackManager.addCallback(TNECallbacks.ACCOUNT_SAVE.toString(), new CallbackEntry(AccountSaveCallback.class));
    callbackManager.addCallback(TNECallbacks.ACCOUNT_CREATE.toString(), new CallbackEntry(AccountCreateCallback.class));
    callbackManager.addCallback(TNECallbacks.ACCOUNT_DELETE.toString(), new CallbackEntry(AccountDeleteCallback.class));
    callbackManager.addCallback(TNECallbacks.TRANSACTION_PRE.toString(), new CallbackEntry(PreTransactionCallback.class));
    callbackManager.addCallback(TNECallbacks.TRANSACTION_POST.toString(), new CallbackEntry(PostTransactionCallback.class));
    callbackManager.addCallback(TNECallbacks.CURRENCY_DROP.toString(), new CallbackEntry(CurrencyDropCallback.class));
    callbackManager.addCallback(TNECallbacks.CURRENCY_LOAD.toString(), new CallbackEntry(CurrencyLoadCallback.class));
    callbackManager.addCallback(TNECallbacks.DENOMINATION_LOAD.toString(), new CallbackEntry(DenominationLoadCallback.class));
  }

  @Override
  public String commandHelpWriter(ExecutableCommand command, CommandActor actor) {
    final MessageData data = new MessageData("Messages.Commands.Help.Entry");
    data.addReplacement("$command", command.getPath().toRealString());
    data.addReplacement("$arguments", MessageHandler.getInstance().getTranslator().translateNode(new MessageData("Messages.Commands." + command.getUsage()), "default"));
    data.addReplacement("$description", MessageHandler.getInstance().getTranslator().translateNode(new MessageData("Messages.Commands." + command.getDescription()), "default"));

    return MessageHandler.getInstance().getTranslator().translateNode(data, "default");
  }

  @Override
  public void registerUpdateChecker() {
    if(MainConfig.yaml().getBoolean("Core.Update.Check")) {
      this.updateChecker = new Updater();

      PluginCore.log().inform("Running version: " + version() + " Build: " + build());

      PluginCore.log().inform("Build Stability: " + this.updateChecker.stable());

      if(this.updateChecker.needsUpdate()) {
        PluginCore.log().inform("Update Available! Latest: " + this.updateChecker.getBuild());
      }

      if(this.updateChecker.isEarlyBuild()) {
        PluginCore.log().inform("Running pre-release version! Thanks for being a tester!");
      }
    }
  }

  @Override
  public void postConfigs() {

    //set our debug options.
    PluginCore.instance().setLevel(DebugLevel.fromID(MainConfig.yaml().getString("Core.Debugging.Mode")));

    this.economyManager = new EconomyManager();

    this.economyManager.init();

    if(!this.economyManager.currency().load(PluginCore.directory(), false)) {
      return;
    }

    this.economyManager.currency().saveCurrenciesUUID(PluginCore.directory());

    UUID serverID;
    //Set our server UUID. This is used for proxy messaging.
    final boolean randomUUID = MainConfig.yaml().getBoolean("Core.Server.RandomUUID", false);

    if(!randomUUID) {
      if(!MainConfig.yaml().contains("Core.ServerID")) {

        serverID = UUID.randomUUID();
        MainConfig.yaml().set("Core.ServerID", serverID.toString());
        MISCUtils.setComment(MainConfig.yaml(), "Core.ServerID", "Don't modify unless you know what you're doing.");

        try {
          MainConfig.yaml().save();
        } catch(IOException e) {
          PluginCore.log().error("Issue while saving Server UUID to config.yml", e, DebugLevel.OFF);
        }
      } else {
        serverID = UUID.fromString(MainConfig.yaml().getString("Core.ServerID"));
      }
    } else {
      serverID = UUID.randomUUID();
    }
    PluginCore.instance().setServerID(serverID);
  }

  @Override
  public void postStorage() {
  }

  @Override
  public void postEnable() {
    MenuManager.instance().addMenu(new MyEcoMenu());
    MenuManager.instance().addMenu(new MyBalMenu());

    this.storage.loadAll(Account.class, "");

    this.storage.loadAll(Receipt.class, "");

    final String name = MainConfig.yaml().getString("Core.Server.Account.Name");
    serverAccount = UUID.nameUUIDFromBytes(("NonPlayer:" + name).getBytes(StandardCharsets.UTF_8));

    if(MainConfig.yaml().getBoolean("Core.Server.Account.Enabled")) {

      PluginCore.log().inform("Checking Server Account.");

      if(economyManager.account().findAccount(serverAccount.toString()).isEmpty()) {

        PluginCore.log().inform("Creating Server Account.");

        final AccountAPIResponse response = economyManager.account().createAccount(serverAccount.toString(), name, true);
        if(response.getResponse().success()) {

          PluginCore.log().inform("Server Account has been created.");

          final BigDecimal defaultBalance = new BigDecimal(MainConfig.yaml().getString("Core.Server.Account.Balance"));
          if(defaultBalance.compareTo(BigDecimal.ZERO) > 0) {
            response.getAccount().ifPresent(value->value.setHoldings(new HoldingsEntry(economyManager.region().defaultRegion(),
                    economyManager.currency().getDefaultCurrency().getUid(),
                    defaultBalance,
                    EconomyManager.NORMAL
            )));
          }
        } else {
          PluginCore.log().error("Unable to create Server Account. Reason: " + response.getResponse().response());
        }
      }
    }

    //Set up the auto saver if enabled.
    if(DataConfig.yaml().getBoolean("Data.AutoSaver.Enabled")) {

      this.autoSaver = PluginCore.server().scheduler().createRepeatingTask(()->{
                storage.storeAll();
              }, new ChoreTime(0),
              new ChoreTime(DataConfig.yaml().getInt("Data.AutoSaver.Interval"), TimeUnit.SECONDS),
              ChoreExecution.SECONDARY);
    }


    economyManager.printInvalid();

    PluginCore.server().scheduler().createDelayedTask(()-> economyManager.getTopManager().load(), new ChoreTime(2), ChoreExecution.SECONDARY);
    PluginCore.server().scheduler().createRepeatingTask(()-> economyManager.getTopManager().load(), new ChoreTime(2), new ChoreTime(MainConfig.yaml().getInt("Core.Commands.Top.Refresh"), TimeUnit.SECONDS), ChoreExecution.SECONDARY);
  }

  @Override
  public void postDisable() {
    if(autoSaver != null) {
      autoSaver.cancel();
    }

    final Optional<Datable<?>> data = Optional.ofNullable(storage.getEngine().datables().get(Account.class));
    if(data.isPresent()) {
      data.get().storeAll(storage.getConnector(), null);
    }
  }

  /**
   * The implementation's {@link EconomyManager}, which is used to manage everything economy related
   * in TNE.
   * @return The {@link EconomyManager Economy Manager}.
   */
  public static EconomyManager eco() {
    return instance.economyManager;
  }

  public static YamlStorageManager yaml() {
    return instance.yamlManager;
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

  public static TNECore instance() {
    return instance;
  }

  public static TNEAPI api() {
    return instance.api;
  }

  public UUID getServerAccount() {
    return serverAccount;
  }

  public abstract <INV> ItemCalculations<INV> itemCalculations();

  public AbstractItemStack<?> denominationToStack(final ItemDenomination denomination) {
    return denominationToStack(denomination, 1);
  }

  public AbstractItemStack<?> denominationToStack(final ItemDenomination denomination, int amount) {
    return PluginCore.server().stackBuilder().of(denomination.getMaterial(), amount)
            .enchant(denomination.getEnchantments())
            .lore(denomination.getLore())
            .flags(denomination.getFlags())
            .damage(denomination.getDamage())
            .display(MiniMessage.miniMessage().deserialize(denomination.getName()))
            .modelData(denomination.getCustomModel()).debug(false);
  }
}
