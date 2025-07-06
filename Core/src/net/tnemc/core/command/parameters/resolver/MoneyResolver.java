package net.tnemc.core.command.parameters.resolver;
/*
 * The New Economy
 * Copyright (C) 2022 - 2025 Daniel "creatorfromhell" Vidmar
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


import net.tnemc.core.TNECore;
import net.tnemc.core.currency.parser.ParseMoney;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

/**
 * PercentDecimalResolver
 *
 * @author creatorfromhell
 * @since 0.1.4.0
 */
public class MoneyResolver implements ParameterType<CommandActor, ParseMoney> {

  @Override
  public ParseMoney parse(@NotNull final MutableStringStream input, @NotNull final ExecutionContext<CommandActor> context) {

    final String value = input.readString();

    return TNECore.eco().currency().parser().parse(TNECore.eco().region().defaultRegion(), value);
  }
}