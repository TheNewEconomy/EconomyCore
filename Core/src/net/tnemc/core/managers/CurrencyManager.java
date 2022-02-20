package net.tnemc.core.managers;

import net.tnemc.core.TNECore;
import net.tnemc.core.currency.Currency;
import net.tnemc.core.currency.CurrencyType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CurrencyManager {

  Map<String, Currency> currencies = new HashMap<>();

  Map<String, CurrencyType> types = new HashMap<>();

  public Optional<CurrencyType> findType(@NotNull String type) {
    return Optional.ofNullable(types.get(type));
  }

  public Currency getDefault(@NotNull String world) {
    final String worldGroup = TNECore.connector().worldConnectionProvider().resolveWorld(world);

    return currencies.get(worldGroup);
  }
}