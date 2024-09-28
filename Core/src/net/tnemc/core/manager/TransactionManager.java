package net.tnemc.core.manager;

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

import net.tnemc.core.TNECore;
import net.tnemc.core.config.MainConfig;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionCheck;
import net.tnemc.core.transaction.TransactionCheckGroup;
import net.tnemc.core.transaction.TransactionProcessor;
import net.tnemc.core.transaction.TransactionType;
import net.tnemc.core.transaction.check.MaximumBalanceCheck;
import net.tnemc.core.transaction.check.MinimumBalanceCheck;
import net.tnemc.core.transaction.check.NegativeBalanceCheck;
import net.tnemc.core.transaction.check.PreCallbackCheck;
import net.tnemc.core.transaction.check.RegionCheck;
import net.tnemc.core.transaction.check.RestrictedCurrencyCheck;
import net.tnemc.core.transaction.check.StatusCheck;
import net.tnemc.core.transaction.check.TrackingCheck;
import net.tnemc.core.transaction.processor.BaseTransactionProcessor;
import net.tnemc.core.transaction.tax.TaxType;
import net.tnemc.core.transaction.tax.type.FlatType;
import net.tnemc.core.transaction.tax.type.PercentileType;
import net.tnemc.core.transaction.type.ConversionType;
import net.tnemc.core.transaction.type.DepositType;
import net.tnemc.core.transaction.type.PayType;
import net.tnemc.core.transaction.type.WithdrawType;
import net.tnemc.plugincore.core.io.maps.EnhancedHashMap;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.TimeZone;

/**
 * Manages everything related to the transaction system. This is usually just keeping track of the
 * various check types and other systems.
 *
 * @author creatorfromhell
 * @see Transaction
 * @see TransactionCheck
 * @since 0.1.2.0
 */
public class TransactionManager {

  private final SimpleDateFormat format;
  private final ReceiptManager receiptManager;

  private final EnhancedHashMap<String, TransactionCheck> checks = new EnhancedHashMap<>();

  private final EnhancedHashMap<String, TransactionCheckGroup> checkGroups = new EnhancedHashMap<>();

  private final EnhancedHashMap<String, TransactionType> types = new EnhancedHashMap<>();

  private final EnhancedHashMap<String, TaxType> tax = new EnhancedHashMap<>();

  private boolean track;
  private BigDecimal amount;

  private TransactionProcessor processor;


  public TransactionManager() {

    this.format = new SimpleDateFormat(MainConfig.yaml().getString("Core.Transactions.Format"));
    this.format.setTimeZone(TimeZone.getTimeZone(MainConfig.yaml().getString("Core.Transactions.Timezone")));

    this.track = MainConfig.yaml().getBoolean("Core.Transactions.Tracking.Enabled");

    try {
      this.amount = new BigDecimal(MainConfig.yaml().getString("Core.Transactions.Tracking.Amount", "400"));
    } catch(NumberFormatException ignore) {

      //Invalid configuration so we set our default
      this.amount = new BigDecimal("400");
    }

    this.receiptManager = new ReceiptManager();

    //Add our default TransactionTypes.
    addType(new ConversionType());
    addType(new DepositType());
    addType(new PayType());
    addType(new WithdrawType());

    //Add our default transaction checks.
    addCheck(new PreCallbackCheck(), "core");
    addCheck(new RegionCheck(), "core");
    addCheck(new TrackingCheck(), "core");
    addCheck(new RestrictedCurrencyCheck(), "core");
    addCheck(new StatusCheck(), "core");
    addCheck(new NegativeBalanceCheck(), "core");
    addCheck(new MinimumBalanceCheck(), "core");
    addCheck(new MaximumBalanceCheck(), "core");

    //Add our default tax types.
    addTax(new FlatType());
    addTax(new PercentileType());

    this.processor = new BaseTransactionProcessor(findGroup("core").get().getChecks());
  }

  /**
   * Attempts to find a {@link TransactionType type}.
   *
   * @param identifier The identifier of the type to use in the search.
   *
   * @return An Optional containing the type if it exists based on the identifier, otherwise an
   * empty Optional.
   */
  public Optional<TransactionType> findType(final String identifier) {

    return Optional.ofNullable(types.get(identifier));
  }

  /**
   * Adds a {@link TransactionType type}.
   *
   * @param type The type to add.
   */
  public void addType(final TransactionType type) {

    types.put(type);
  }

  /**
   * Attempts to find a {@link TaxType type}.
   *
   * @param identifier The identifier of the type to use in the search.
   *
   * @return An Optional containing the type if it exists based on the identifier, otherwise an
   * empty Optional.
   */
  public Optional<TaxType> findTax(final String identifier) {

    return Optional.ofNullable(tax.get(identifier));
  }

  /**
   * Adds a {@link TaxType type}.
   *
   * @param type The type to add.
   */
  public void addTax(final TaxType type) {

    tax.put(type);
  }

  /**
   * Adds a {@link TransactionCheck check}.
   *
   * @param check The check to add.
   */
  public void addCheck(final TransactionCheck check) {

    checks.put(check);
  }

  /**
   * Adds a {@link TransactionCheck check}.
   *
   * @param check The check to add.
   * @param group The {@link TransactionCheckGroup group} to add this check to.
   */
  public void addCheck(final TransactionCheck check, final String group) {

    checks.put(check);
    addCheckToGroup(check.identifier(), group);
  }

  /**
   * Attempts to find a {@link TransactionCheck check}.
   *
   * @param identifier The identifier of the check to use in the search.
   *
   * @return An Optional containing the check if it exists based on the identifier, otherwise an
   * empty Optional.
   */
  public Optional<TransactionCheck> findCheck(final String identifier) {

    return Optional.ofNullable(checks.get(identifier));
  }

  /**
   * Adds a {@link TransactionCheck check} to a {@link TransactionCheckGroup group} if it exists,
   * otherwise it'll create a new group and add it to that.
   *
   * @param check The check to add.
   * @param group The group to add the check to.
   */
  public void addCheckToGroup(final String check, final String group) {

    Optional<TransactionCheckGroup> groupOptional = findGroup(group);
    groupOptional.ifPresentOrElse(transactionCheckGroup->{
      transactionCheckGroup.addCheck(check);
      addGroup(groupOptional.get());
    }, ()->{
      TransactionCheckGroup groupObj = new TransactionCheckGroup(group);
      groupObj.addCheck(check);
      addGroup(groupObj);
    });

  }

  /**
   * Adds a {@link TransactionCheckGroup group}.
   *
   * @param group The group to add.
   */
  public void addGroup(final TransactionCheckGroup group) {

    checkGroups.put(group);
  }

  /**
   * Attempts to find a {@link TransactionCheckGroup group}.
   *
   * @param identifier The identifier of the group to use in the search.
   *
   * @return An Optional containing the group if it exists based on the identifier, otherwise an
   * empty Optional.
   */
  public Optional<TransactionCheckGroup> findGroup(final String identifier) {

    return Optional.ofNullable(checkGroups.get(identifier));
  }

  public boolean isTrack() {

    return track;
  }

  public void setTrack(boolean track) {

    this.track = track;
  }

  public BigDecimal getAmount() {

    return amount;
  }

  public void setAmount(BigDecimal amount) {

    this.amount = amount;
  }

  public TransactionProcessor getProcessor() {

    return processor;
  }

  public void setProcessor(TransactionProcessor processor) {

    this.processor = processor;
  }

  public static ReceiptManager receipts() {

    return TNECore.eco().transaction().receiptManager;
  }

  public SimpleDateFormat getFormat() {

    return format;
  }
}