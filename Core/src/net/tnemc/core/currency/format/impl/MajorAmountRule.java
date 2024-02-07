package net.tnemc.core.currency.format.impl;

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

import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.currency.format.FormatRule;
import net.tnemc.core.utils.Monetary;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class MajorAmountRule implements FormatRule {
  @Override
  public String name() {
    return "major_amount";
  }

  @Override
  public String format(@Nullable Account account, HoldingsEntry entry, String format) {

    final Monetary monetary = entry.asMonetary();

    String replacement = monetary.major().toString();

    if(entry.currency().isPresent() && entry.currency().get().isSeparateMajor()) {
      replacement = String.format(Locale.US,
                                  "%,d",
                                  monetary.major()).replace(",",
                                                            entry.currency()
                                                                 .get().getMajorSeparator());
    }
    return format.replace("<major.amount>", replacement);
  }
}