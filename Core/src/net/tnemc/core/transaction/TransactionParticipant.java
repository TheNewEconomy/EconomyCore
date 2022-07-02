package net.tnemc.core.transaction;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.account.holdings.HoldingsEntry;

import java.util.UUID;

/**
 * Represents a participant in a {@link Transaction transaction}.
 *
 * @see Transaction
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TransactionParticipant {

  private final String id;
  private final HoldingsEntry startingBalance;
  private HoldingsEntry endingBalance;

  public TransactionParticipant(final String id, final HoldingsEntry startingBalance) {
    this.id = id;
    this.startingBalance = startingBalance;
  }

  public String getId() {
    return id;
  }

  public HoldingsEntry getStartingBalance() {
    return startingBalance;
  }

  public HoldingsEntry getEndingBalance() {
    return endingBalance;
  }

  public void setEndingBalance(HoldingsEntry endingBalance) {
    this.endingBalance = endingBalance;
  }
}