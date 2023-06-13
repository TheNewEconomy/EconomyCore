package net.tnemc.core.command;
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

import net.tnemc.core.TNECore;
import net.tnemc.core.command.args.ArgumentsParser;
import net.tnemc.core.compatibility.scheduler.ChoreExecution;
import net.tnemc.core.compatibility.scheduler.ChoreTime;
import net.tnemc.core.io.message.MessageData;
import net.tnemc.core.module.ModuleUpdateChecker;
import net.tnemc.core.module.ModuleWrapper;
import net.tnemc.core.module.cache.ModuleFile;
import revxrsal.commands.orphan.Orphans;

import java.util.List;
import java.util.Optional;

/**
 * ModuleCommand
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ModuleCommand extends BaseCommand {

  public static void onAvailable(ArgumentsParser parser) {
    final String url = (parser.args().length > 0)? parser.args()[0] : TNECore.coreURL;

    TNECore.server().scheduler().createDelayedTask(()->{

      final List<ModuleFile> available = TNECore.instance().moduleCache().getModules(url);

      final MessageData msg = new MessageData("Messages.Module.AvailableHeader");
      msg.addReplacement("$url", url);
      parser.sender().message(msg);

      for(ModuleFile file : available) {

        final MessageData entry = new MessageData("Messages.Module.AvailableEntry");
        entry.addReplacement("$module", file.getName());
        entry.addReplacement("$version", file.getVersion());
        parser.sender().message(entry);
      }

    }, new ChoreTime(0), ChoreExecution.SECONDARY);
  }

  public static void onDownload(ArgumentsParser parser) {
    final String url = (parser.args().length > 1)? parser.args()[1] : TNECore.coreURL;
    final String moduleName = parser.args()[0].toLowerCase().trim();

    TNECore.server().scheduler().createDelayedTask(()->{
      TNECore.instance().moduleCache().getModules(url);

      Optional<ModuleFile> module = TNECore.instance().moduleCache().getModule(url, moduleName);
      if(module.isEmpty()) {
        final MessageData entry = new MessageData("Messages.Module.Invalid");
        entry.addReplacement("$module", moduleName);
        parser.sender().message(entry);
        return;
      }

      if(!ModuleUpdateChecker.download(module.get().getName(), module.get().getUrl())) {
        final MessageData entry = new MessageData("Messages.Module.FailedDownload");
        entry.addReplacement("$module", moduleName);
        parser.sender().message(entry);
        return;
      }

      final MessageData entry = new MessageData("Messages.Module.Downloaded");
      entry.addReplacement("$module", moduleName);
      parser.sender().message(entry);

    }, new ChoreTime(0), ChoreExecution.SECONDARY);
  }

  public static void onInfo(ArgumentsParser parser) {
    if(parser.args().length < 1) {
      help(parser.sender(), "module info", "Module.Info");
      return;
    }

    final String moduleName = parser.args()[0];

    final ModuleWrapper module = TNECore.loader().getModule(moduleName);
    if(module == null) {

      final MessageData entry = new MessageData("Messages.Module.Invalid");
      entry.addReplacement("$module", moduleName);
      parser.sender().message(entry);
      return;
    }

    final MessageData entry = new MessageData("Messages.Module.Info");
    entry.addReplacement("$module", moduleName);
    entry.addReplacement("$author", module.author());
    entry.addReplacement("$version", module.version());
    parser.sender().message(entry);
  }

  public static void onList(ArgumentsParser parser) {
    final StringBuilder modules = new StringBuilder();
    TNECore.loader().getModules().forEach((key, value)->{
      if(modules.length() > 0) modules.append(", ");
      modules.append(value.getInfo().name());
    });

    final MessageData entry = new MessageData("Messages.Module.List");
    entry.addReplacement("$modules", modules.toString());
    parser.sender().message(entry);
  }

  public static void onLoad(ArgumentsParser parser) {
    if(parser.args().length < 1) {
      help(parser.sender(), "module load", "Module.Load");
      return;
    }

    final String moduleName = parser.args()[0];
    final boolean loaded = TNECore.loader().load(moduleName);

    if(!loaded) {

      final MessageData entry = new MessageData("Messages.Module.Invalid");
      entry.addReplacement("$module", moduleName);
      parser.sender().message(entry);
      return;
    }

    final ModuleWrapper module = TNECore.loader().getModule(moduleName);

    final String author = module.getInfo().author();
    final String version = module.getInfo().version();

    module.getModule().enable(TNECore.instance());
    module.getModule().initConfigurations(TNECore.directory());

    module.getModule().enableSave(TNECore.storage());

    module.getModule().registerCallbacks().forEach((key, entry)->{
      TNECore.callbacks().addCallback(key, entry);
    });

    module.getModule().registerListeners().forEach((key, function)->{
      TNECore.callbacks().addConsumer(key, function);
    });

    module.getModule().registerCommands(TNECore.instance().command());
    module.getModule().registerMoneySub().forEach((orphan)->TNECore.instance().command().register(Orphans.path("money"), orphan));
    module.getModule().registerTransactionSub().forEach((orphan)->TNECore.instance().command().register(Orphans.path("transaction"), orphan));
    module.getModule().registerAdminSub().forEach((orphan)->TNECore.instance().command().register(Orphans.path("tne"), orphan));

    TNECore.loader().getModules().put(module.name(), module);

    final MessageData entry = new MessageData("Messages.Module.Loaded");
    entry.addReplacement("$module", moduleName);
    entry.addReplacement("$author", author);
    entry.addReplacement("$version", version);
    parser.sender().message(entry);
  }
}