package net.tnemc.bungee.message.backlog;

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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ConfigEntry
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ConfigEntry {

  private final List<String> synced = new ArrayList<>();

  private final List<String> configs = new ArrayList<>();
  private final List<String> currencies = new ArrayList<>();

  private byte[] bytes;


  private final String pin;

  public ConfigEntry(String pin) {
    this.pin = pin;
  }

  public List<String> getConfigs() {
    return configs;
  }

  public List<String> getCurrencies() {
    return currencies;
  }

  public List<String> getSynced() {
    return synced;
  }

  public String getPin() {
    return pin;
  }

  public byte[] getBytes() {
    return bytes;
  }

  private void genBytes() {

    final ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF(pin);

    out.writeShort(currencies.size());
    for(final String str : currencies) {
      out.writeUTF(str);
    }

    for(final String str : configs) {
      out.writeUTF(str);
    }
    bytes = out.toByteArray();
  }

  public static ConfigEntry fromBytes(DataInputStream stream) {

    //hub string
    //currency size short
    //currencies string
    //configs
    try {
      final String hub = stream.readUTF();

      final ConfigEntry entry = new ConfigEntry(hub);

      final short currencies = stream.readShort();
      for(int i = 0; i < currencies; i++) {
        entry.getCurrencies().add(stream.readUTF());
      }

      entry.getConfigs().add(stream.readUTF());
      entry.getConfigs().add(stream.readUTF());

      entry.genBytes();

      return entry;

    } catch(IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}