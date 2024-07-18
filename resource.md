# The New Economy
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/246101510dca4eb9a729ef178dae682c)](https://app.codacy.com/gh/TheNewEconomy/EconomyCore/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Build Status](https://ci.codemc.io/job/creatorfromhell/job/TNE/badge/icon)](https://ci.codemc.io/job/creatorfromhell/job/TNE/)

The New Economy, TNE, is the ultimate economy plugin for your Minecraft server, no matter what platform you're on. While
TNE is pushed as being "feature-packed," but it's not feature-packed in the normal sense of jamming a bunch of useless features
into the core plugin, but rather using modules.

<p align="center">
    <img src="logo.png" width="500" />
</p>    
<p align="center">    
<i><b>The economy plugin for server owners that want more out of their economy.</b></i>
</p>

# About TNE
TNE was originally created for hey0's hMod long before Bukkit/Spigot/Paper was a thing. Since then, it has taken on many
forms and this is the future form of the plugin. This core is designed to be robust and carry TNE into the future with or
without Minecraft by not being platform-dependent.

# Requirements
TNE has some minimum requirements that should be followed.
- MySQL 8.0+ (if using MySQL for storage)
- MariaDB 10.7.0+
- Java 17+

# Features

## Support your favorite plugins
We provide support for all your favorite plugins from Towny to Factions. Anything that supports Vault, supports TNE!

## Cross-Server
We support cross-server balance syncing through our velocity and bungee plugins, as well as Redis!

## Updates
We continue to develop updates for TNE for free, and fix bugs as they're reported.

## All the currencies
TNE Allows you to create multiple currencies with ease through the currency creation GUI.

### Types
TNE Supports different types of currencies!

Virtual: Simple currency that exists in thin air

Item: Use minecraft items as currency.

Mixed: This type uses both virtual and item balances, and allows players to switch their balance between items and virtual whenever!

Experience: Use experience as currency.

## Money Notes
Convert your virtual currency into a physical money note using the /money note command.

## Multi-platform
Use Sponge? Sure. Paper? Why not? Both? Over Velocity? Definitely! Switch between, your TNE data works
for you no matter your platform. More platforms soon!

Supported:
- Paper/Spigot/Purpur
- Sponge8

Coming:
- Fabric
- Sponge7

## Discord Server
We have a discord server available to those users that have questions or need support!

## Configurability
TNE strives to allow you to configure things as much as possible. From the messages to starting money, to max holdings!

## MISC Features
- Adventure Chat support for translations to allow for awesome colorful messages!
- Support for Placeholders, include top 10
- Easy commands for easy administration
- Multiple data types
  - SQL, YAML, and Maria DB
- Tab completion for commands!
- baltop command

Placeholders

| Name                                                  |  Description                                                   |
|-------------------------------------------------------|----------------------------------------------------------------|
| tne_balance                                           | Returns player's balance.                                      |
| tne_balance_formatted                                 | Returns formatted player's balance.                            |
| tne_world_worldname                                   | returns player's balance for specific world.                   |
| tne_world_worldname_formatted                         | returns the formatted player's balance for specific world.     |
| tne_currency_currencyname                             | returns player's balance for specific currency.                |
| tne_currency_currencyname_formatted                   | returns player's balance for specific currency.                |
| tne_wcur_worldname_currencyname                       | returns player's balance for specific world and currency.      |
| tne_wcur_worldname_currencyname_formatted             | returns player's balance for specific world and currency.      |
| tne_toppos                                            | returns player's position on baltop                            |
| tne_toppos_world name or all                          | returns player's balance for specific currency.                |
| tne_toppos_world name or all_currency name or all     | returns player's balance for specific world and currency.      |
| tne_toppos_<currency name>\_position\_<pos #>           | returns the value for the position based on the value of Messages.Money.PlaceholderTopEntry in messages.yml      |

Permissions & Commands


Support

- Discord
- Github