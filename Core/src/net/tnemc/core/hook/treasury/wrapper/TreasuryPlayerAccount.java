package net.tnemc.core.hook.treasury.wrapper;
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

import me.lokka30.treasury.api.economy.account.PlayerAccount;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * TreasuryPlayerAccount
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TreasuryPlayerAccount extends TreasuryAccount implements PlayerAccount {

  public TreasuryPlayerAccount(net.tnemc.core.account.PlayerAccount account) {
    super(account);
  }

  @Override
  public @NotNull UUID identifier() {
    return ((net.tnemc.core.account.PlayerAccount)account).getUUID();
  }
}