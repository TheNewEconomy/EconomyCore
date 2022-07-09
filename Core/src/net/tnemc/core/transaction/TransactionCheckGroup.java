package net.tnemc.core.transaction;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.io.maps.MapKey;

import java.util.LinkedList;

/**
 * Represents a group of transaction checks. This is used for easily adding multiple checks to a
 * transaction without needing to list them all individually.
 *
 * @see TransactionCheck
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TransactionCheckGroup {

  private final LinkedList<String> checks = new LinkedList<>();

  private final String identifier;

  public TransactionCheckGroup(String identifier) {
    this.identifier = identifier;
  }

  public void addCheck(final String identifier) {
    checks.add(identifier);
  }

  public void removeCheck(final String identifier) {
    checks.remove(identifier);
  }

  @MapKey
  public String getIdentifier() {
    return identifier;
  }

  public LinkedList<String> getChecks() {
    return checks;
  }

  public void setChecks(final LinkedList<String> replacements) {
    checks.clear();
    checks.addAll(replacements);
  }
}