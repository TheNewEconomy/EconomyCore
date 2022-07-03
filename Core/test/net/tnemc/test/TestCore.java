package net.tnemc.test;
/*
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

import net.tnemc.core.TNECore;
import net.tnemc.core.handlers.PlayerJoinHandler;
import net.tnemc.test.compatibility.TestPlayerProvider;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * TNECoreTest
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TestCore extends TNECore {

  @Test
  public void initialize() {

    //Startup
    setInstance(this);

    //This should throw an exception because it's already initialized.
    assertThrows(IllegalStateException.class, ()->setInstance(this), "Instance was set twice");

    assertNotNull(economyManager, "Economy Manager is null");
    assertNotNull(economyManager.account(), "Economy Manager is null");
    assertNotNull(economyManager.translation(), "Translation Manager is null");
    assertNotNull(economyManager.data(), "Data Manager is null");
  }

  @Test
  public void accountTests() {
    //PlayerJoinHandler Tests
    PlayerJoinHandler handler = new PlayerJoinHandler();

    final UUID cfhID = UUID.nameUUIDFromBytes("creatorfromhell".getBytes(StandardCharsets.UTF_8));

    final boolean cancelled = handler.handle(new TestPlayerProvider("creatorfromhell")).isCancelled();

    assertFalse(cancelled, "PlayerJoinHandler threw false.");

    assertNotNull(eco().account().uuidProvider().retrieve("creatorfromhell").get(), "Get uuid pair returned null");

    assertEquals(eco().account().uuidProvider().retrieve("creatorfromhell").get().getIdentifier(), cfhID, "Invalid UUID returned.");

    //Non-Player Account Tests
    eco().account().createAccount("town-Test", "town-Test");
    assertNotNull(eco().account().findAccount("town-Test").get().getIdentifier());
  }
}