package net.tnemc.core.io.redis;
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

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.tnemc.core.TNECore;
import net.tnemc.core.compatibility.log.DebugLevel;
import net.tnemc.core.config.DataConfig;
import net.tnemc.core.utils.Identifier;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * TNEJedisManager
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TNEJedisManager {

  protected final JedisPool pool;
  protected final TNESubscriber subscriber = new TNESubscriber();
  protected final byte[] channel = "tne:balance".getBytes(StandardCharsets.UTF_8);
  final Thread redisThread;

  private static TNEJedisManager instance;

  public TNEJedisManager() {
    final JedisPoolConfig config = new JedisPoolConfig();
    config.setMaxTotal(DataConfig.yaml().getInt("Data.Sync.Redis.Pool.MaxSize", 10));
    config.setMaxIdle(DataConfig.yaml().getInt("Data.Sync.Redis.Pool.MaxIdle", 10));
    config.setMinIdle(DataConfig.yaml().getInt("Data.Sync.Redis.Pool.MinIdle", 1));
    this.pool = new JedisPool(config, DataConfig.yaml().getString("Data.Sync.Redis.Host"), DataConfig.yaml().getInt("Data.Sync.Redis.Port"),
            DataConfig.yaml().getInt("Data.Sync.Redis.Timeout"), DataConfig.yaml().getString("Data.Sync.Redis.User"),
            DataConfig.yaml().getString("Data.Sync.Redis.Password"), DataConfig.yaml().getInt("Data.Sync.Redis.Index"),
            DataConfig.yaml().getBoolean("Data.Sync.Redis.SSL"));

    if(connectionTest()) {

      redisThread = new Thread(() -> {
        try(Jedis jedis = pool.getResource()) {
          jedis.subscribe(subscriber, channel);
        }
      }, "TNE Redis Thread");

      redisThread.start();

      TNECore.log().error("Redis Subscriber Thread Started", DebugLevel.OFF);
    } else {
      TNECore.log().error("Redis Connection Test Failed!", DebugLevel.OFF);
      redisThread = null;
    }

    instance = this;
  }

  public boolean connectionTest() {
    try(Jedis jedis = pool.getResource()) {
      jedis.ping();
      return true;
    } catch(Exception ignore) {
      return false;
    }
  }

  public void publish(final byte[] data) {
    try(Jedis jedis = pool.getResource()) {
      jedis.publish(channel, data);
    }
  }

  public void publish(final String channel, final byte[] data) {
    try(Jedis jedis = pool.getResource()) {
      jedis.publish(channel.getBytes(StandardCharsets.UTF_8), data);
    }
  }

  public static TNEJedisManager instance() {
    return instance;
  }

  public static void send(final String account, final String region, final UUID currency, final Identifier handler, final BigDecimal amount) {

    final ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF(TNECore.instance().getServerID().toString());
    out.writeUTF(account);
    out.writeUTF(region);
    out.writeUTF(currency.toString());
    out.writeUTF(handler.asID());
    out.writeUTF(amount.toPlainString());

    instance.publish(out.toByteArray());
  }
}