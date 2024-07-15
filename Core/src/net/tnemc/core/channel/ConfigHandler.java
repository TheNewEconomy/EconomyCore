package net.tnemc.core.channel;

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

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.tnemc.core.TNECore;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.io.serialization.impl.SerialCurrency;
import net.tnemc.plugincore.PluginCore;
import net.tnemc.plugincore.core.channel.ChannelBytesWrapper;
import net.tnemc.plugincore.core.channel.ChannelMessageHandler;
import net.tnemc.plugincore.core.compatibility.log.DebugLevel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * ConfigHandler
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ConfigHandler extends ChannelMessageHandler {
  public ConfigHandler() {
    super("config");
  }

  public static void send() {

    if(!TNECore.instance().data().getYaml().getString("Data.Sync.Config.Hub", "none").equalsIgnoreCase("none")) {
      return;
    }

    //hub string
    //currency size short
    //currencies string
    //configs

    final ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF(TNECore.instance().data().getYaml().getString("Data.Sync.Config.SharingPin", "000000"));

    //write our currencies to the stream
    out.writeShort(TNECore.eco().currency().currencies().size());
    for(Currency currency : TNECore.eco().currency().currencies()) {
      out.writeUTF(new SerialCurrency().toJSON(currency).toJSONString());
    }
    out.writeUTF("config.yml:" + TNECore.instance().config().getYaml().dump());
    out.writeUTF("messages.yml:" + TNECore.instance().message().getYaml().dump());

    TNECore.instance().storage().sendProxyMessage("tne:config", out.toByteArray());
  }

  @Override
  public void handle(ChannelBytesWrapper wrapper) {

    if(TNECore.instance().data().getYaml().getString("Data.Sync.Config.Hub", "none").equalsIgnoreCase("none") ||
       !TNECore.instance().data().getYaml().getBoolean("Data.Sync.Config.Sync", true)) {
      return;
    }

    try {

      //make sure this is the hub server we want to sync with.
      final String hub = wrapper.readUTF();
      if(!hub.equalsIgnoreCase(TNECore.instance().data().getYaml().getString("Data.Sync.Config.Hub", "none"))) {
        return;
      }

      final List<String> sync = new ArrayList<>();
      sync.addAll(TNECore.instance().data().getYaml().getStringList("Data.Sync.Config.Configs"));

      //sync our currencies
      final short currencies = wrapper.readShort();
      TNECore.eco().currency().currencies().clear();
      final List<UUID> loaded = new ArrayList<>();
      for(int i = 0; i < currencies; i++) {

        final String json = wrapper.readUTF();

        if(sync.contains("currency")) {
          final Currency currency = new SerialCurrency().fromJSON(json);
          TNECore.eco().currency().addCurrency(currency);
          loaded.add(currency.getUid());
        }
      }

      //Delete currencies that weren't a part of the sync.
      final Iterator<Currency> it = TNECore.eco().currency().currencies().iterator();
      while(it.hasNext()) {
        final Currency cur = it.next();

        if(!loaded.contains(cur.getUid())) {
          it.remove();
          TNECore.eco().currency().delete(cur.getUid());
        }
      }

      //Load configurations from stream
      for(int i = 0; i < 2; i++) {
        final String config = wrapper.readUTF();
        final String[] parts = config.split(":");

        if(parts.length < 2) {
          continue;
        }

        if(parts[0].equals("messages.yml")) {
          if(sync.contains("messages.yml")) {
            //TODO: This needs a new work around or removed since plugin channels are limited.
            //TNECore.instance().message().setYaml(YamlFile.loadConfigurationFromString(parts[1], true));
          }
        } else {
          if(sync.contains("config.yml")) {
            //TODO: This needs a new work around or removed since plugin channels are limited.
            //TNECore.instance().config().setYaml(YamlFile.loadConfigurationFromString(parts[1], true));
          }
        }

        //End loop if we reached messages.yml because it is the last config we have.
        if(parts[0].equalsIgnoreCase("messages.yml")) {
          break;
        }
      }

    } catch(Exception e) {
      PluginCore.log().error("Issue while retrieving config sync data!", e, DebugLevel.OFF);
    }
  }
}