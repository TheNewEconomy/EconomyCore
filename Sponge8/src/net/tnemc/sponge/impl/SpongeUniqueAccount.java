package net.tnemc.sponge.impl;

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

import net.tnemc.core.account.PlayerAccount;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import java.util.UUID;

/**
 * SpongeUniqueAccount
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SpongeUniqueAccount extends SpongeVirtualAccount implements UniqueAccount {

  public SpongeUniqueAccount(PlayerAccount account) {
    super(account);
  }

  @Override
  public UUID uniqueId() {
    if(account instanceof PlayerAccount) {
      return ((PlayerAccount)account).getUUID();
    }
    return UUID.fromString(identifier());
  }
}
