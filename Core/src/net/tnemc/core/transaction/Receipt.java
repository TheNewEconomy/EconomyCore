package net.tnemc.core.transaction;


/*
 * The New Economy
 * Copyright (C) 2022 Daniel "creatorfromhell" Vidmar
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

import net.tnemc.core.account.holdings.modify.HoldingsModifier;
import net.tnemc.core.actions.ActionSource;

import java.util.UUID;

/**
 * Represents a record of a successful {@link Transaction transaction}. Similar to how in the real
 * world we get receipts for completed transactions, these are the same.
 *
 * @see Transaction
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class Receipt {

  //Our archival information
  private UUID id;
  private long time;
  private String type;

  private ActionSource source;
  private TransactionParticipant from;
  private TransactionParticipant to;
  private HoldingsModifier modifierTo;
  private HoldingsModifier modifierFrom;

  //Our active information that may be modified.
  private boolean archive = false;
  private boolean voided = false;

  public Receipt(UUID id, long time, Transaction transaction) {
    this.id = id;
    this.time = time;
    this.type = transaction.getType();
    this.source = transaction.getSource();
  }

  public UUID getId() {
    return id;
  }

  public long getTime() {
    return time;
  }

  public String getType() {
    return type;
  }

  public ActionSource getSource() {
    return source;
  }

  public TransactionParticipant getFrom() {
    return from;
  }

  public void setFrom(TransactionParticipant from) {
    this.from = from;
  }

  public TransactionParticipant getTo() {
    return to;
  }

  public void setTo(TransactionParticipant to) {
    this.to = to;
  }

  public HoldingsModifier getModifierTo() {
    return modifierTo;
  }

  public void setModifierTo(HoldingsModifier modifierTo) {
    this.modifierTo = modifierTo;
  }

  public HoldingsModifier getModifierFrom() {
    return modifierFrom;
  }

  public void setModifierFrom(HoldingsModifier modifierFrom) {
    this.modifierFrom = modifierFrom;
  }

  public boolean isArchive() {
    return archive;
  }

  public void setArchive(boolean archive) {
    this.archive = archive;
  }

  public boolean isVoided() {
    return voided;
  }

  public void setVoided(boolean voided) {
    this.voided = voided;
  }
}