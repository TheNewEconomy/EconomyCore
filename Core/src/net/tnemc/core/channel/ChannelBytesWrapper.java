package net.tnemc.core.channel;
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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * ChannelBytesWrapper represents a utility wrapper for a ByteArray stream.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ChannelBytesWrapper implements AutoCloseable {

  private final byte[] data;
  private DataInputStream in;

  public ChannelBytesWrapper(byte[] data) {
    this.data = data;
    open();
  }

  public void open() {
    this.in = new DataInputStream(new ByteArrayInputStream(data));
  }

  @Override
  public void close() {
    try {
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String readUTF() throws IOException {
    return in.readUTF();
  }

  public Optional<UUID> readUUID() throws IOException {
    final String str = readUTF();

    try {
      return Optional.of(UUID.fromString(str));
    } catch (Exception ignore) {
      return Optional.empty();
    }
  }

  public Optional<BigDecimal> readBigDecimal() throws IOException {
    final String str = readUTF();

    try {
      return Optional.of(new BigDecimal(str));
    } catch (Exception ignore) {
      return Optional.empty();
    }
  }
}