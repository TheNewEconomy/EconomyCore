package net.tnemc.core.account;

/**
 * Represents the status of an {@link Account accout}. The  account
 * status affects things such as using the balance and receiving payments.
 *
 * @see Account
 * @since 0.1.1.17
 * @author creatorfromhell
 */
public enum AccountStatus {

  NORMAL("Normal", true, true),
  LOCKED_RECEIVE("Locked Receive", true, false),
  LOCKED_USAGE("Locked Usage", false, true),
  LOCKED("Locked", false, false);

  private final String name;

  /**
   * Whether the account may use balances in their account.
   */
  private final boolean use;

  /**
   * Whether the account may receive money into their account.
   */
  private final boolean receive;

  AccountStatus(String name, boolean use, boolean receive) {
    this.name = name;
    this.use = use;
    this.receive = receive;
  }

  public static AccountStatus fromName(String name) {
    for(AccountStatus status : values()) {
      if(status.getName().equalsIgnoreCase(name)) {
        return status;
      }
    }
    return NORMAL;
  }

  public String getName() {
    return name;
  }

  public boolean canUse() {
    return use;
  }

  public boolean canReceive() {
    return receive;
  }
}