package net.tnemc.core.manager;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.io.maps.EnhancedHashMap;
import net.tnemc.core.transaction.Transaction;
import net.tnemc.core.transaction.TransactionCheck;
import net.tnemc.core.transaction.TransactionCheckGroup;

import java.util.Optional;

/**
 * Manages everything related to the transaction system. This is usually just keeping track of the
 * various check types and other systems.
 *
 * @see Transaction
 * @see TransactionCheck
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TransactionManager {

  private final EnhancedHashMap<String, TransactionCheck> checks = new EnhancedHashMap<>();

  private final EnhancedHashMap<String, TransactionCheckGroup> checkGroups = new EnhancedHashMap<>();

  /**
   * Adds a {@link TransactionCheck check}.
   * @param check The check to add.
   */
  public void addCheck(final TransactionCheck check) {
    checks.put(check);
  }

  /**
   * Adds a {@link TransactionCheck check}.
   * @param check The check to add.
   * @param group The {@link TransactionCheckGroup group} to add this check to.
   */
  public void addCheck(final TransactionCheck check, final String group) {
    checks.put(check);
    addCheckToGroup(check.identifier(), group);
  }

  /**
   * Attempts to find a {@link TransactionCheck check}.
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
   * @param group The group to add.
   */
  public void addGroup(final TransactionCheckGroup group) {
    checkGroups.put(group);
  }

  /**
   * Attempts to find a {@link TransactionCheckGroup group}.
   * @param identifier The identifier of the group to use in the search.
   *
   * @return An Optional containing the group if it exists based on the identifier, otherwise an
   * empty Optional.
   */
  public Optional<TransactionCheckGroup> findGroup(final String identifier) {
    return Optional.ofNullable(checkGroups.get(identifier));
  }
}