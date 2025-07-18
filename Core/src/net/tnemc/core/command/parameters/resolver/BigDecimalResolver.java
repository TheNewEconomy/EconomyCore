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

import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.math.BigDecimal;

/**
 * BigDecimalResolver
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class BigDecimalResolver implements ParameterType<CommandActor, BigDecimal> {

  @Override
  public BigDecimal parse(@NotNull final MutableStringStream input, @NotNull final ExecutionContext<CommandActor> context) {

    return new BigDecimal(PercentDecimalResolver.replaceDecimals(input.readString()));
  }
}