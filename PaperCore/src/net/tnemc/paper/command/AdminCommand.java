package net.tnemc.paper.command;

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
import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.AccountStatus;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.api.response.AccountAPIResponse;
import net.tnemc.core.command.BaseCommand;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.utils.Identifier;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;
import net.tnemc.plugincore.core.compatibility.scheduler.ChoreExecution;
import net.tnemc.plugincore.core.compatibility.scheduler.ChoreTime;
import net.tnemc.plugincore.core.io.message.MessageData;
import net.tnemc.plugincore.paper.impl.PaperCMDSource;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.help.CommandHelp;

import java.io.File;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * AdminCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
@Command({ "tne", "myeco", "ecomenu", "ecomin", "ecoadmin", "ecomanage", "theneweconomy" })
@Description("Admin.Main.Description")
public class AdminCommand {

  @DefaultFor({ "tne", "myeco", "ecomenu", "ecomin", "ecoadmin", "ecomanage", "theneweconomy" })
  @Subcommand({ "ecomenu", "menu", "myeco" })
  @Usage("Admin.MyEco.Arguments")
  @Description("Admin.MyEco.Description")
  @CommandPermission("tne.admin.menu")
  public void onMyEco(final BukkitCommandActor sender) {

    net.tnemc.core.command.AdminCommand.onMyEco(new PaperCMDSource(sender));
  }

  @Subcommand({ "help", "?" })
  @Usage("Help.Arguments")
  @Description("Help.Description")
  public void help(final BukkitCommandActor actor, final CommandHelp<String> helpEntries, @Default("1") final int page) {

    BaseCommand.help(new PaperCMDSource(actor), helpEntries, page);
  }

  @Subcommand({ "backup", "archive" })
  @Usage("Admin.Backup.Arguments")
  @Description("Admin.Backup.Description")
  @CommandPermission("tne.admin.backup")
  public void backup(final BukkitCommandActor sender) {

    net.tnemc.core.command.AdminCommand.onBackup(new PaperCMDSource(sender));
  }

  @Subcommand({ "create", "add", "new", "make", "+" })
  @Usage("Admin.Create.Arguments")
  @Description("Admin.Create.Description")
  @CommandPermission("tne.admin.create")
  public void create(final BukkitCommandActor sender, final String name) {

    net.tnemc.core.command.AdminCommand.onCreate(new PaperCMDSource(sender), name);
  }

  @Subcommand({ "debug" })
  @Usage("Admin.Debug.Arguments")
  @Description("Admin.Debug.Description")
  @CommandPermission("tne.admin.debug")
  public void debug(final BukkitCommandActor sender, final DebugLevel level) {

    net.tnemc.core.command.AdminCommand.onDebug(new PaperCMDSource(sender), level);
  }

  @Subcommand({ "delete", "destroy", "del", "remove", "-" })
  @Usage("Admin.Delete.Arguments")
  @Description("Admin.Delete.Description")
  @CommandPermission("tne.admin.delete")
  public void delete(final BukkitCommandActor sender, final String name) {

    net.tnemc.core.command.AdminCommand.onDelete(new PaperCMDSource(sender), name);
  }

  @Subcommand({ "extract" })
  @Usage("Admin.Extract.Arguments")
  @Description("Admin.Extract.Description")
  @CommandPermission("tne.admin.extract")
  public void extract(final BukkitCommandActor sender) {

    net.tnemc.core.command.AdminCommand.onExtract(new PaperCMDSource(sender));
  }

  @Subcommand({ "purge" })
  @Usage("Admin.Purge.Arguments")
  @Description("Admin.Purge.Description")
  @CommandPermission("tne.admin.purge")
  public void purge(final BukkitCommandActor sender) {

    net.tnemc.core.command.AdminCommand.onPurge(new PaperCMDSource(sender));
  }

  @Subcommand({ "reload" })
  @Usage("Admin.Reload.Arguments")
  @Description("Admin.Reload.Description")
  @CommandPermission("tne.admin.reload")
  public void reload(final BukkitCommandActor sender, @Default("all") final String type) {

    net.tnemc.core.command.AdminCommand.onReload(new PaperCMDSource(sender), type);
  }

  @Subcommand({ "reset", "nuke" })
  @Usage("Admin.Reset.Arguments")
  @Description("Admin.Reset.Description")
  @CommandPermission("tne.admin.reset")
  public void reset(final BukkitCommandActor sender) {

    net.tnemc.core.command.AdminCommand.onReset(new PaperCMDSource(sender));
  }

  @Subcommand({ "restore" })
  @Usage("Admin.Restore.Arguments")
  @Description("Admin.Restore.Description")
  @CommandPermission("tne.admin.restore")
  public void restore(final BukkitCommandActor sender, @Default("0") final int extraction) {

    net.tnemc.core.command.AdminCommand.onRestore(new PaperCMDSource(sender), extraction);
  }

  @Subcommand({ "old" })
  @Usage("Admin.Restore.Arguments")
  @Description("Admin.Restore.Description")
  @CommandPermission("tne.admin.old")
  public void old(final BukkitCommandActor sender, @Default("0") final int extraction) {

    PluginCore.server().scheduler().createDelayedTask(()->restoreOld(extraction), new ChoreTime(0), ChoreExecution.SECONDARY);
    new PaperCMDSource(sender).message(new MessageData("Messages.Admin.Restoration"));
  }

  @Subcommand({ "save" })
  @Usage("Admin.Save.Arguments")
  @Description("Admin.Save.Description")
  @CommandPermission("tne.admin.save")
  public void save(final BukkitCommandActor sender) {

    net.tnemc.core.command.AdminCommand.onSave(new PaperCMDSource(sender));
  }

  @Subcommand({ "status" })
  @Usage("Admin.Status.Arguments")
  @Description("Admin.Status.Description")
  @CommandPermission("tne.admin.status")
  public void status(final BukkitCommandActor sender, final Account account, @Default("normal") final AccountStatus status) {

    net.tnemc.core.command.AdminCommand.onStatus(new PaperCMDSource(sender), account, status);
  }

  @Subcommand({ "version", "ver", "build" })
  @Usage("Admin.Version.Arguments")
  @Description("Admin.Version.Description")
  @CommandPermission("tne.admin.version")
  public void version(final BukkitCommandActor sender) {

    net.tnemc.core.command.AdminCommand.onVersion(new PaperCMDSource(sender));
  }


  public static boolean restoreOld(@Nullable final Integer extraction) {

    final File file = new File(PluginCore.directory(), "extracted.yml");

    if(!file.exists()) {

      PluginCore.log().inform("The extraction file doesn't exist.", DebugLevel.OFF);
      return false;
    }
    YamlDocument extracted = null;
    try {
      extracted = YamlDocument.create(file);
    } catch(Exception e) {
      PluginCore.log().error("Failed load extraction file for writing.", e, DebugLevel.OFF);
    }

    if(extracted == null) {
      PluginCore.log().inform("The extraction file doesn't exist.", DebugLevel.OFF);
      return false;
    }

    if(extracted.contains("Accounts")) {
      final Set<Object> accounts = extracted.getSection("Accounts").getKeys();

      final int frequency = (int)(accounts.size() * 0.10);
      int number = 1;

      final boolean recode = extracted.contains("Version");

      for(final Object nameObj : accounts) {

        final String name = (String)nameObj;
        final String username = name.replaceAll("\\!", "\\.").replaceAll("\\@", "-").replaceAll("\\%", "_");
        boolean nonPlayer = false;

        UUID id = get(username);
        if(id == null) {
          nonPlayer = true;
          id = UUID.randomUUID();
        }

        final AccountAPIResponse response = TNECore.eco().account().createAccount(id.toString(), username, nonPlayer);
        if(response.getAccount().isEmpty()) {
          PluginCore.log().inform("Couldn't create account for " + username + ". Reason: " + response.getResponse().response(), DebugLevel.OFF);
          continue;
        }

        final Set<Object> regions = extracted.getSection("Accounts." + name + ".Balances").getKeys();
        for(final Object regionObj : regions) {

          final String region = (String)regionObj;
          final Set<Object> currencies = extracted.getSection("Accounts." + name + ".Balances." + region).getKeys();
          for(final Object currencyNameObj : currencies) {

            final String currency = (String)currencyNameObj;
            if(!recode) {
              final String finalCurrency = (currency.equalsIgnoreCase("default"))? TNECore.eco().currency().getDefaultCurrency().getIdentifier() : currency;
              final Optional<Currency> cur = TNECore.eco().currency().findCurrency(finalCurrency);

              final Currency currencyObj = cur.orElseGet(()->TNECore.eco().currency().getDefaultCurrency(TNECore.eco().region().resolve(region)));

              final BigDecimal amount = new BigDecimal(extracted.getString("Accounts." + name + ".Balances." + region + "." + currency));

              response.getAccount().get().setHoldings(new HoldingsEntry(TNECore.eco().region().resolve(region), currencyObj.getUid(),
                                                                        amount, EconomyManager.NORMAL), TNECore.eco().getFor(currencyObj.type()).get(0).identifier());
            } else {

              final Set<Object> types = extracted.getSection("Accounts." + name + ".Balances." + region + "." + currency).getKeys();
              for(final Object typeObj : types) {

                final String type = (String)typeObj;
                final BigDecimal amount = new BigDecimal(extracted.getString("Accounts." + name
                                                                             + ".Balances." + region
                                                                             + "." + currency + "."
                                                                             + type));

                response.getAccount().get().setHoldings(new HoldingsEntry(region, UUID.fromString(currency),
                                                                          amount, Identifier.fromID(type)));
              }
            }
            number++;
            try {
              final boolean message = (number % frequency == 0);

              if(message) {
                final int progress = (number * 100) / accounts.size();
                PluginCore.log().inform("Restoration Progress: " + progress, DebugLevel.OFF);
              }
            } catch(Exception ignore) { }
          }
        }
      }
      PluginCore.log().inform("Restoration has completed!", DebugLevel.OFF);
    }

    return true;
  }

  protected static UUID get(final String name) {

    for(final OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
      if(player.getName() == null) continue;
      if(player.getName().equalsIgnoreCase(name)) {
        return player.getUniqueId();
      }
    }
    return null;
  }
}