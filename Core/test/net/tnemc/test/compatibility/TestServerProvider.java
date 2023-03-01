package net.tnemc.test.compatibility;
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

import net.tnemc.core.compatibility.PlayerProvider;
import net.tnemc.core.compatibility.ServerConnector;
import net.tnemc.core.currency.calculations.ItemCalculations;
import net.tnemc.core.currency.item.ItemDenomination;
import net.tnemc.core.io.message.TranslationProvider;
import net.tnemc.item.AbstractItemStack;
import net.tnemc.item.providers.CalculationsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * TestServerProvider
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class TestServerProvider implements ServerConnector {
  @Override
  public Optional<PlayerProvider> findPlayer(@NotNull UUID identifier) {
    return Optional.empty();
  }

  @Override
  public boolean playedBefore(UUID uuid) {
    return false;
  }

  @Override
  public boolean playedBefore(String name) {
    return false;
  }

  @Override
  public boolean online(String name) {
    return false;
  }

  @Override
  public String defaultWorld() {
    return null;
  }

  @Override
  public String replaceColours(String string) {
    return null;
  }

  @Override
  public TranslationProvider translation() {
    return null;
  }

  @Override
  public void saveResource(String path, boolean replace) {

  }

  @Override
  public <S, T extends AbstractItemStack<S>, INV> CalculationsProvider<T, S, INV> calculations() {
    return null;
  }

  @Override
  public <S> AbstractItemStack<S> denominationToStack(ItemDenomination denomination) {
    return null;
  }

  @Override
  public <INV> ItemCalculations<INV> itemCalculations() {
    return null;
  }
}
