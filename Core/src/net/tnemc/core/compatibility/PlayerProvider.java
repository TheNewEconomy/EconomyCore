package net.tnemc.core.compatibility;


import java.math.BigDecimal;
import java.util.UUID;

/**
 * A class that acts as a bridge between various player objects on different server software providers.
 *
 * @since 0.1.2.0
 * @author creatorfromhell
 */
public interface PlayerProvider {

  /**
   * Used to get the {@link UUID} of this player.
   * @return The {@link UUID} of this player.
   */
  UUID getUUID();

  /**
   * Used to get the name of this player.
   * @return The name of this player.
   */
  String getName();

  /**
   * Used to get the amount of experience this player has.
   *
   * @return The amount of levels this player has.
   */
  int getExp();

  /**
   * Used to set the amount of experience this player has.
   *
   * @param exp The amount of experience to set for this player.
   */
  void setExp(int exp);

  /**
   * Used to get the amount of experience levels this player has.
   *
   * @return The amount of experience levels this player has.
   */
  int getExpLevel();

  /**
   * Used to set the amount of experience levels this player has.
   *
   * @param level The amount of experience levels to set for this player.
   */
  void setExpLevel(int level);

  /**
   * Used to get an inventory object.
   *
   * @param ender True if the ender chest object should be returned, otherwise false.
   * @return The inventory object.
   */
  Object getInventory(boolean ender);

  /**
   * Used to determine if this player has the specified permission node.
   *
   * @param permission The node to check for.
   * @return True if the player has the permission, otherwise false.
   */
  boolean hasPermission(String permission);

  /*BigDecimal getItems(Currency currency, Object inventory);

  default void setItems(Account account, Currency currency, BigDecimal amount, Object inventory,
                        boolean remove) {

    boolean consolidate = EconomyManager.instance().canConsolidate();
    if(currency instanceof ItemCurrrency) {
      consolidate = ((ItemCurrrency)currency).isConsolidate();
    }
    setItems(account, currency, amount, inventory, remove, consolidate);
  }

  void setItems(Account account, Currency currency, BigDecimal amount, Object inventory,
                boolean remove, boolean consolidate);*/
}