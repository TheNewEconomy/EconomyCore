package net.tnemc.core.io.serialization.impl;

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

import net.tnemc.core.account.shared.Member;
import net.tnemc.plugincore.core.io.serialization.JSONAble;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.UUID;

/**
 * SerialMember
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class SerialMember implements JSONAble<Member> {

  /**
   * Used to serialize this object to a JSON-valid string.
   *
   * @param member The object to serialize.
   *
   * @return The {@link JSONObject} associated with the JSON-valid String.
   */
  @Override
  public JSONObject toJSON(Member member) {
    final JSONObject json = new JSONObject();
    json.put("id", member.getId().toString());

    JSONObject permissionsJson = new JSONObject();
    member.getPermissions().forEach((permission, value) -> permissionsJson.put(permission, value));
    json.put("permissions", permissionsJson);

    return json;
  }

  /**
   * Used to generate information for this object from
   *
   * @param serialized The JSON-valid String that we are going to deserialize.
   *
   * @return The object that was deserialized from the JSON string.
   */
  @Override
  public Member fromJSON(String serialized) {
    try {
      final JSONParser parser = new JSONParser();
      final JSONObject json = (JSONObject)parser.parse(serialized);

      final Member member = new Member(UUID.fromString((String)json.get("id")));

      final JSONObject permissionsJson = (JSONObject) json.get("permissions");
      permissionsJson.forEach((permission, value) -> {
        if(value instanceof Boolean) {
          member.addPermission((String) permission, (Boolean) value);
        }
      });

      return member;
    } catch (ParseException | NumberFormatException | ClassCastException e) {
      e.printStackTrace();
      return null;
    }
  }
}