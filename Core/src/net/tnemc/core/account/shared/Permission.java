package net.tnemc.core.account.shared;

import net.tnemc.core.account.Account;

/**
 * Represents a permission for an {@link Member member} of an
 * {@link Account account}.
 *
 *
 * @see Member
 * @see Account
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public interface Permission {

  /**
   * The identifier of the permission.
   * @return The identifier of the permission.
   */
  String identifier();

  /**
   * The default value of the permission.
   * @return The default value of the permission.
   */
  boolean defaultValue();
}