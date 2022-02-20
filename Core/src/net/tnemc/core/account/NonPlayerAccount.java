package net.tnemc.core.account;

import java.util.UUID;

/**
 * Represents an account that is not associated with a player.
 *
 * @see Account
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public class NonPlayerAccount extends SharedAccount {

  public NonPlayerAccount(UUID identifier, String name) {
    super(identifier, name, identifier);
  }

  public NonPlayerAccount(UUID identifier, String name, UUID owner) {
    super(identifier, name, owner);
  }
}