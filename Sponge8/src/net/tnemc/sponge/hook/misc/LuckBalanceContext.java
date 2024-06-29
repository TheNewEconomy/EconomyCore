package net.tnemc.sponge.hook.misc;

/*
 * The New Economy
 * Copyright (C) 2022 - 2024 Daniel "creatorfromhell" Vidmar
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

import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import net.luckperms.api.context.ContextSet;
import net.tnemc.core.hook.luckperms.BalanceContext;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.Subject;

/**
 * LuckBalanceContext
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class LuckBalanceContext implements ContextCalculator<Subject> {

  final BalanceContext context = new BalanceContext();

  @Override
  public void calculate(@NotNull Subject subject, @NotNull ContextConsumer contextConsumer) {
    if(subject instanceof Player player) {
      context.calculate(player.uniqueId(), contextConsumer);
    }
  }

  @Override
  public ContextSet estimatePotentialContexts() {
    return context.estimate();
  }
}