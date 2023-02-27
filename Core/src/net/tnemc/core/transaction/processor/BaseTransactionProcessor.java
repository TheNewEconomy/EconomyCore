package net.tnemc.core.transaction.processor;

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
import net.tnemc.core.transaction.TransactionCheck;
import net.tnemc.core.transaction.TransactionCheckGroup;
import net.tnemc.core.transaction.TransactionProcessor;

import java.util.LinkedList;

/**
 * BaseTransactionProcessor
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BaseTransactionProcessor implements TransactionProcessor {

  private final LinkedList<String> checks = new LinkedList<>();

  public BaseTransactionProcessor() {
    checks.addAll(TNECore.eco().transaction().findGroup("core").get().getChecks());
  }

  /**
   * Used to get the checks for this processor.
   *
   * @return The checks for this processor.
   */
  @Override
  public LinkedList<String> getChecks() {
    return checks;
  }

  /**
   * Used to add {@link TransactionCheck check} to this processor.
   *
   * @param check The check to add.
   */
  @Override
  public void addCheck(TransactionCheck check) {
    checks.add(check.identifier());
  }

  /**
   * Used to add {@link TransactionCheck checks} from a group to this processor.
   *
   * @param group The group to add.
   */
  @Override
  public void addGroup(TransactionCheckGroup group) {
    checks.addAll(group.getChecks());
  }
}