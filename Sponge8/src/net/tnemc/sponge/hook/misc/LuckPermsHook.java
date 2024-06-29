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

import net.luckperms.api.LuckPerms;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.ServiceRegistration;

import java.util.Optional;

/**
 * LuckPermsHook
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class LuckPermsHook {

  public static void register() {
    final Optional<ServiceRegistration<LuckPerms>> provider = Sponge.serviceProvider().registration(LuckPerms.class);
    //provider.ifPresent(luckPermsServiceRegistration->luckPermsServiceRegistration.service().getContextManager().registerCalculator(new LuckBalanceContext()));
  }
}