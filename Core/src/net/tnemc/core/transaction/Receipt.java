package net.tnemc.core.transaction;

/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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