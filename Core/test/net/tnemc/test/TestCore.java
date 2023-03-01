package net.tnemc.test;

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

import net.tnemc.core.TNECore;
import net.tnemc.core.account.Account;
import net.tnemc.core.account.PlayerAccount;
import net.tnemc.core.account.holdings.HoldingsEntry;
import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.handlers.PlayerJoinHandler;
import net.tnemc.core.io.message.translation.BaseTranslationProvider;
import net.tnemc.core.transaction.Receipt;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionResult;
import net.tnemc.core.transaction.processor.BaseTransactionProcessor;
import net.tnemc.core.utils.exceptions.InvalidTransactionException;
import net.tnemc.test.compatibility.TestPlayerProvider;
import net.tnemc.test.compatibility.TestServerProvider;
import org.junit.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TNECoreTest
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TestCore extends TNECore {

  public TestCore() {
    super(null, null, null, new BaseTranslationProvider());
  }

  @Test
  public void initialize() {

    //Startup
    setInstance(this);

    this.server = new TestServerProvider();

    Currency currency = new Currency();
    currency.setIdentifier("USD");

    eco().currency().addCurrency(currency);

    //This should throw an exception because it's already initialized.
    assertThrows(IllegalStateException.class, ()->setInstance(this), "Instance was set twice");

    assertNotNull(economyManager, "Economy Manager is null");
    assertNotNull(economyManager.account(), "Economy Manager is null");
    assertNotNull(economyManager.translation(), "Translation Manager is null");
    assertNotNull(economyManager.data(), "Data Manager is null");
  }

  @Test
  public void accountTests() {
    //PlayerJoinHandler Tests
    PlayerJoinHandler handler = new PlayerJoinHandler();

    final UUID cfhID = UUID.nameUUIDFromBytes("creatorfromhell".getBytes(StandardCharsets.UTF_8));
    final UUID townUUID = UUID.nameUUIDFromBytes("creatorfromhell".getBytes(StandardCharsets.UTF_8));

    final boolean cancelled = handler.handle(new TestPlayerProvider("creatorfromhell")).isCancelled();

    assertFalse(cancelled, "PlayerJoinHandler threw false.");

    assertNotNull(eco().account().uuidProvider().retrieve("creatorfromhell").get(), "Get uuid pair returned null");

    assertEquals(eco().account().uuidProvider().retrieve("creatorfromhell").get().getIdentifier(), cfhID, "Invalid UUID returned.");

    //Non-Player Account Tests
    System.out.println(eco().account().createAccount("town-Test", "town-Test").response());
    eco().account().createAccount(cfhID.toString(), "creatorfromhell");
    System.out.println(eco().account().createAccount(cfhID.toString(), "creatorfromhell").response());


    final Optional<Account> account = eco().account().findAccount(cfhID);

    final Optional<Account> towny = eco().account().findAccount("town-Test");

    if(towny.isPresent()) {
      System.out.println("Set Value: " + towny.get().setHoldings(new HoldingsEntry("world", "USD", new BigDecimal("1000"))));

      System.out.println("Null Check: " + (towny.get().getHoldings("world", "USD").isPresent()));

      System.out.println("Towny Holdings: " + towny.get().getHoldings("world", "USD").get().getAmount().toPlainString());

    }

    if(account.isPresent()) {
      System.out.println("Account exists");

      assertTrue(account.get() instanceof PlayerAccount);

      System.out.println("Holdings: " + account.get().getHoldings("world", "USD").get().getAmount().toPlainString());

      final HoldingsModifier modifier = new HoldingsModifier(
          "world",
          "USD",
          new BigDecimal("600"));

      Transaction transaction = new Transaction("pay").to(cfhID.toString(), modifier).from("town-Test", modifier.counter()).processor(new BaseTransactionProcessor());

      Optional<Receipt> receipt = Optional.empty();
      try {
        final TransactionResult result = transaction.process();
        System.out.println(result.getMessage());
        receipt = result.getReceipt();
      } catch(InvalidTransactionException e) {
        e.printStackTrace();
      }

      System.out.println("Final Holdings: " + account.get().getHoldings("world", "USD").get().getAmount().toPlainString());
      System.out.println("Towny Final Holdings: " + towny.get().getHoldings("world", "USD").get().getAmount().toPlainString());

      if(receipt.isPresent()) {

        receipt.get().voidTransaction();

        System.out.println("Final Holdings: " + account.get().getHoldings("world", "USD").get().getAmount().toPlainString());
        System.out.println("Towny Final Holdings: " + towny.get().getHoldings("world", "USD").get().getAmount().toPlainString());
      }
    }
  }

  @Override
  protected void onEnable() {

  }
}