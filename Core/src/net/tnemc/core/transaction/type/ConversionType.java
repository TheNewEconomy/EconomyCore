package net.tnemc.core.transaction.type;
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

import net.tnemc.core.config.MainConfig;
import net.tnemc.plugincore.core.io.maps.MapKey;
import net.tnemc.core.transaction.TransactionType;
import net.tnemc.core.transaction.tax.TaxEntry;

import java.util.Optional;

/**
 * ConversionType
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class ConversionType implements TransactionType {
  /**
   * The identifier of this transaction type.
   *
   * @return The unique identifier for this transaction type. Should be human-friendly.
   */
  @Override
  @MapKey
  public String identifier() {
    return "conversion";
  }

  /**
   * The taxation amount to be assessed on the recipient of the transaction. This will take the amount
   * from the amount being sent to the account, and send it to the server account.
   *
   * @return The {@link TaxEntry} related to the taxation amount for the recipient, if applicable,
   * otherwise an empty optional.
   */
  @Override
  public Optional<TaxEntry> toTax() {
    return Optional.empty();
  }

  /**
   * The taxation amount to be assessed on the sender of the transaction. This will add the amount
   * to the amount being sent to the account, and send it to the server account.
   *
   * @return The {@link TaxEntry} related to the taxation amount for the sender, if applicable,
   * otherwise an empty optional.
   */
  @Override
  public Optional<TaxEntry> fromTax() {
    if(MainConfig.yaml().getBoolean("Core.Transactions.Conversion.Tax.Enabled")) {
      final String tax = MainConfig.yaml().getString("Core.Transactions.Conversion.Tax.Rate");
      TaxEntry entry;
      if(tax.contains("%")) {
        entry = new TaxEntry("percent", Double.parseDouble(tax.replace("%", "")) / 100);
      } else {
        entry = new TaxEntry("flat", Double.parseDouble(tax));
      }
      return Optional.of(entry);
    }
    return Optional.empty();
  }
}