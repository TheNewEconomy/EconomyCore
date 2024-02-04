package net.tnemc.core.api.callback;
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
 * TNECallbacks represents the built-in callbacks to allow addon authors an easy way to find the
 * correct name identifier.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public enum TNECallbacks {

  ACCOUNT_TYPES("account_types"),
  ACCOUNT_LOAD("account_load"),
  ACCOUNT_SAVE("account_save"),
  ACCOUNT_CREATE("account_create"),
  ACCOUNT_DELETE("account_delete"),
  TRANSACTION_PRE("transaction_pre"),
  TRANSACTION_POST("transaction_post"),
  CURRENCY_LOAD("currency_load"),
  DENOMINATION_LOAD("denomination_load");

  final String identifier;

  TNECallbacks(String identifier) {
    this.identifier = identifier;
  }

  public String id() {
    return identifier;
  }
}