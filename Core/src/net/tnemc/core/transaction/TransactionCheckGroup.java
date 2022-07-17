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