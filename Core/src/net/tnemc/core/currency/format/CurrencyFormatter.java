package net.tnemc.core.currency.format;
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

import net.tnemc.core.EconomyManager;
import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.format.impl.DecimalRule;
import net.tnemc.core.currency.format.impl.MajorAmountRule;
import net.tnemc.core.currency.format.impl.MajorNameRule;
import net.tnemc.core.currency.format.impl.MajorRule;
import net.tnemc.core.currency.format.impl.MaterialRule;
import net.tnemc.core.currency.format.impl.MinorAmountRule;
import net.tnemc.core.currency.format.impl.MinorRule;
import net.tnemc.core.currency.format.impl.ShortenRule;
import net.tnemc.core.currency.format.impl.SymbolRule;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * CurrencyFormatter represents a formatter, which is responsible for converting a
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CurrencyFormatter {

  final static LinkedHashMap<String, FormatRule> rulesMap = new LinkedHashMap<>();

  static {
    addRule(new ShortenRule());

    addRule(new DecimalRule());
    addRule(new MajorAmountRule());
    addRule(new MajorNameRule());
    addRule(new MajorRule());
    addRule(new MinorAmountRule());
    //addRule(new MinorNameRule());
    addRule(new MinorRule());
    addRule(new SymbolRule());
    addRule(new MaterialRule());
  }

  public static void addRule(FormatRule rule) {
    rulesMap.put(rule.name(), rule);
  }

  public static String format(@Nullable Account account, final BigDecimal amount) {
    return format(account, new HoldingsEntry(TNECore.server().defaultRegion(), TNECore.eco().currency().getDefaultCurrency().getUid(), amount, EconomyManager.NORMAL));
  }

  public static String format(@Nullable Account account, HoldingsEntry entry) {
    String format = "";

    final Optional<Currency> currency = TNECore.eco().currency().findCurrency(entry.getCurrency());
    if(currency.isPresent()) {

      format = currency.get().getFormat();

      for(FormatRule rule : rulesMap.values()) {
        format = rule.format(account, entry, format);
      }
    }

    return format;
  }
}