package net.tnemc.core.region;

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

import net.tnemc.core.config.MainConfig;
import net.tnemc.core.region.mode.BiomeMode;
import net.tnemc.core.region.mode.WorldMode;
import net.tnemc.plugincore.PluginCore;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A class that contains information related to regions such as configs, and region-sharing naming.
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class RegionProvider {

  private final Map<String, String> sharing = new HashMap<>();
  private final Map<String, RegionGroup> regions = new HashMap<>();

  private final Map<String, RegionMode> modes = new HashMap<>();
  private final List<String> disabledRegions = new ArrayList<>();

  private boolean realmSharing;

  private RegionMode mode;

  public RegionProvider(boolean realmSharing, final String mode) {
    this.realmSharing = realmSharing;

    //add modes
    modes.put("biome", new BiomeMode());
    modes.put("world", new WorldMode());

    disabledRegions.addAll(MainConfig.yaml().getStringList("Core.Region.DisabledRegions"));

    //set mode
    this.mode = modes.getOrDefault(mode, modes.get("world"));
  }

  public void initializeRegion(final String name, final RegionType type) {
    if(this.mode.accepted().contains(type)) {

      if(type.equals(RegionType.WORLD) && MainConfig.yaml().getBoolean("Core.Region.GroupRealms", true)) {

        //Group the realms with this world.
        if(!name.contains("_nether") && !name.contains("_the_end")) {
          sharing.put(name + "_nether", name);
          sharing.put(name + "_the_end", name);
        } else {

          //This is the nether or end, so we don't need to go through the sharing fiasco below.
          sharing.put(name, name.split("_")[0]);
          return;
        }
      }

      if(MainConfig.yaml().contains("Core.Region.Sharing." + name)) {
        sharing.put(name, MainConfig.yaml().getString("Core.Region.Sharing." + name));
      }
    }
  }

  /**
   * Used to add a region group to our existing map of groups.
   * @param name The name of the group
   * @param group The group instance.
   */
  public void addGroup(final String name, final RegionGroup group) {
    regions.put(name, group);
  }

  /**
   * Used to find a region group based on its name.
   * @param name The name of the group to find.
   * @return An optional containing the {@link RegionGroup} if it exists, otherwise an empty optional.
   */
  public Optional<RegionGroup> retrieve(final String name) {
    return Optional.ofNullable(regions.get(name));
  }

  public boolean multiRegion() {
    return MainConfig.yaml().getBoolean("Core.Region.MultiRegion");
  }

  /**
   * Used to resolve the region name to its connection or itself if no connection exists.
   *
   * @param region The region to get the true name, with connection if possible.
   *
   * @return The region name if no connection, otherwise the name of the connection.
   */
  @NotNull
  public String resolve(final String region) {

    if(disabledRegions.contains(region)) {
      return region;
    }

    if(!MainConfig.yaml().getBoolean("Core.Region.MultiRegion")) {
      return defaultRegion();
    }

    return sharing.getOrDefault(region, defaultRegion());
  }

  public String defaultRegion() {
    final String configDefault = MainConfig.yaml().getString("Core.Region.DefaultRegion");
    if(configDefault.equalsIgnoreCase("TNE_SYSTEM")) {
      return PluginCore.server().defaultWorld();
    }
    return configDefault;
  }

  public boolean isSharing() {
    return realmSharing;
  }

  public void setRealmSharing(boolean realmSharing) {
    this.realmSharing = realmSharing;
  }

  public RegionMode getMode() {
    return mode;
  }

  public void setMode(final String mode) {
    this.mode = modes.getOrDefault(mode, modes.get("world"));
  }

  public Map<String, RegionGroup> getRegions() {
    return regions;
  }

  public List<String> getDisabledRegions() {
    return disabledRegions;
  }
}