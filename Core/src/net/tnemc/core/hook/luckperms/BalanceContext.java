package net.tnemc.core.hook.luckperms;

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

import net.luckperms.api.context.ContextConsumer;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.currency.Currency;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;
import java.util.UUID;

/**
 * BalanceContext
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BalanceContext {

  final String KEY = "tne:balance-";

  public void calculate(@NonNull UUID uuid, @NonNull ContextConsumer contextConsumer) {
    final Optional<Account> account = TNECore.eco().account().findAccount(uuid);

    if(account.isPresent()) {

      final String region = TNECore.eco().region().getMode().region(uuid);
      for(Currency currency : TNECore.eco().currency().currencies()) {
        contextConsumer.accept(KEY + currency.getIdentifier(), account.get().getHoldingsTotal(region, currency.getUid()).toPlainString());
      }
    } else {

      for(Currency currency : TNECore.eco().currency().currencies()) {
        contextConsumer.accept(KEY + currency.getIdentifier(), currency.getStartingHoldings().toPlainString());
      }
    }
  }

  public ContextSet estimate() {
    final ImmutableContextSet.Builder builder = ImmutableContextSet.builder();

    for(Currency currency : TNECore.eco().currency().currencies()) {
      builder.add(KEY + currency.getIdentifier(), "0");
      builder.add(KEY + currency.getIdentifier(), "100");
      builder.add(KEY + currency.getIdentifier(), "500");
      builder.add(KEY + currency.getIdentifier(), "1000");
    }
    return builder.build();
  }
}