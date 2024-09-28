package net.tnemc.core.currency;

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

/**
 * CurrencyRegion represents
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class CurrencyRegion {

  private final String region;
  private final boolean enabled;
  private final boolean regionDefault;

  public CurrencyRegion(String region, boolean enabled, boolean regionDefault) {

    this.region = region;
    this.enabled = enabled;
    this.regionDefault = regionDefault;
  }

  public String region() {

    return region;
  }

  public boolean isEnabled() {

    return enabled;
  }

  public boolean isDefault() {

    return regionDefault;
  }
}