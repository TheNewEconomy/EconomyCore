# The New Economy
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/246101510dca4eb9a729ef178dae682c)](https://app.codacy.com/gh/TheNewEconomy/EconomyCore/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Build Status](https://ci.codemc.io/job/creatorfromhell/job/TNE/badge/icon)](https://ci.codemc.io/job/creatorfromhell/job/TNE/)

The New Economy, TNE, is the ultimate economy plugin for your Minecraft server, no matter what platform you're on. While
TNE is pushed as being "feature-packed," but it's not feature-packed in the normal sense of jamming a bunch of useless features
into the core plugin, but rather using modules.

<p align="center">
    <img src="https://raw.githubusercontent.com/TheNewEconomy/EconomyCore/main/logo.png" width="500" />
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

# Switched?
- Converter Coming Soon! Take the poll: [here](https://strawpoll.com/4xw9gxag)

# Support
To get an actual response use a support method below, not the reviews.

- [Developer API Docs](https://github.com/TheNewEconomy/EconomyCore/wiki/API)
- [Discord](https://discord.gg/WNdwzpy)
- [Github](https://github.com/TheNewEconomy/EconomyCore/issues)

# Features

## Menu System
Want to use menus for currency creation and player actions? We got you!

### MyBal for Players
![mybal gif](https://raw.githubusercontent.com/TheNewEconomy/EconomyCore/main/mybal.gif)

### MyEco for Admins
![myeco gif](https://raw.githubusercontent.com/TheNewEconomy/EconomyCore/main/myeco.gif)

## Support your favorite plugins
We provide support for all your favorite plugins from Towny to Factions. Anything that supports Vault, supports TNE!

## Cross-Server
We support cross-server balance syncing through our velocity and bungee plugins, as well as Redis!

### Bungee
Download: [here](https://cdn.modrinth.com/data/bZ4eSWf0/versions/OTwQFFn1/TNE-BungeeCore-0.1.2.8-Release-1.jar)

### Velocity
Download: [here](https://cdn.modrinth.com/data/bZ4eSWf0/versions/prNGjbjv/TNE-VelocityCore-0.1.2.8-Release-1.jar)

## Updates
We continue to develop updates for TNE for free, and fix bugs as they're reported.

## All the currencies
TNE Allows you to create multiple currencies with ease through the currency creation GUI.

### Types
TNE Supports different types of currencies!

#### Virtual: Simple currency that exists in thin air

#### Item: Use minecraft items as currency.

#### Mixed: This type uses both virtual and item balances, and allows players to switch their balance between items and virtual whenever!

#### Experience: Use experience as currency.

## Money Notes
Convert your virtual currency into a physical money note using the /money note command.

## Multi-platform
Use Sponge? Sure. Paper? Why not? Both? Over Velocity? Definitely! Switch between, your TNE data works
for you no matter your platform. More platforms soon!

### Supported:
- Paper/Spigot/Purpur
- Sponge8

### Coming:
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
- Transaction Logging
- Easy-to-use menu system for currency creation and balance actions!
- Multiple data types
  - SQL, YAML, and Maria DB
- Tab completion for commands!
- baltop command

## Open Source
Contribute on the [github](https://github.com/TheNewEconomy/EconomyCore)!

## Placeholders

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
| tne_toppos_<currency name>\_position\_<pos #>         | returns the value for the position based on the value of Messages.Money.PlaceholderTopEntry in messages.yml      |

## Permissions & Commands

## /tne
**About:** All commands that are designated as being for administrative purposes.  
**Shortcuts:** /tne menu(/ecomenu), /tne(/ecomin, /ecoadmin, /ecomanage, /tneneweconomy)
**Base node:** tne.admin (default: ops)  
**Wildcard node:** tne.admin.*

| Command                           | Permission        | Description                                                                                               | Default       |
|-----------------------------------|-------------------|-----------------------------------------------------------------------------------------------------------|---------------|
| tne backup                        | tne.admin.backup  | Creates a backup of all server data.                                                                      | ops |
| tne create \<player\> \[balance\] | tne.admin.create  | Creates a new economy account. Player ~ The account owner. Balance ~ The starting balance of the account. | ops |
| tne debug  \[level\]              | tne.admin.debug   | Toggles console debug mode to the specified level.                                                        | ops |
| tne delete \<player\>             | tne.admin.delete  | Deletes a player account. Player ~ The account owner.                                                     | ops |
| tne extract                       | tne.admin.extract | Extracts all player balances with their username attached for database related debugging.                 | ops |
| tne menu                          | tne.admin.menu    | Opens up the tne economy admin menu                                                                       | ops |
| tne purge                         | tne.admin.purge   | Deletes all player accounts that have the default balance                                                 | ops |
| tne reload \[configs\]            | tne.admin.reload  | Used to reload configurations.                                                                            | ops |
| tne reset                         | tne.admin.reset   | Deletes all economy-related data from the database.                                                       | ops |
| tne restore                       | tne.admin.restore | Restores all balances that are located in extracted.yml after performing the extract command.             | ops |
| tne save                          | tne.admin.save    | Force saves all TNE data.                                                                                 | ops |
| tne status \<player\> \[status\]  | tne.admin.status  | Displays, or sets, the current account status of an account. Player ~ The account owner.                  | ops |
| tne version                       | tne.admin.version | Displays the version of TNE currently running.                                                            | ops |

## /money
**About:** All commands that are used to interact with the server's money system.  
**Shortcuts:** /money balance(/bal, /balance), /money pay(/pay), /money top(/baltop), /money menu(/mybal)  
**Base node:** tne.money (default: everyone)  
**Wildcard node:** tne.money.*

| Command                                                    | Permission            | Description                                                                            | Default       |
|------------------------------------------------------------|-----------------------|----------------------------------------------------------------------------------------|---------------|
| money balmenu                                              | tne.money.mybal       | Opens up the mybal menu.                                                               | everyone |
| money balance \[world\] \[currency\]                       | tne.money.balance     | Displays your current holdings.                                                        | everyone |
| money convert \<amount\> \<to currency\> \[from currency\] | tne.money.convert     | Convert some of your holdings to another currency.                                     | everyone |
| money deposit \<amount\> \[currency\]                      | tne.money.deposit     | Deposits money from an item form into a virtual bank for mixed currencies.             | everyone |
| money give \<player\> \<amount\> \[world\] \[currency\]    | tne.money.give        | Adds money into your economy, and gives it to a player.                                | ops |
| money givenote \<player\> \<amount\> \[currency\]          | tne.money.givenote    | Gives the specified player a currency note for the specified amount.                   | ops |
| money note \<amount\> \[currency\]                         | tne.money.note        | Makes your virtual currency physical, for storage/trading purposes.                    | everyone |
| money other \<player\> \[world\] \[currency\]              | tne.money.other       | Retrieves the balance of a player.                                                     | everyone |
| money pay \<player\> \<amount\> \[currency\]               | tne.money.pay         | Use some of your holdings to pay another player.                                       | everyone |
| money request \<player\> \<amount\> \[currency\]           | tne.money.request     | Request money from a player.                                                           |
| money set \<player\> \<amount\> \[world\] \[currency\]     | tne.money.set         | Set the holdings of a player.                                                          | ops |
| money setall  \<amount\> \[world\] \[currency\]            | tne.money.setall      | Set the holdings of all players.                                                       | ops |
| money take \<player\> \<amount\> \[world\] \[currency\]    | tne.money.take        | Removes money from your economy, specifically from a player's balance.                 | ops |
| money top \[page\]                                         | tne.money.top         | A list of players with the highest balances.                                           | ops |
| money top \[page\] \[refresh\(true/false\)\]               | tne.money.top.refresh | This permission allows the user to include the refresh argument to refresh the baltop. | ops |
| money withdraw \<amount\> \[currency\]                     | tne.money.withdraw    | Withdraws money from your virtual balance into its item form.                          | ops |

## /transaction
**About:** All commands that are used to interact with the transaction system.  
**Shortcuts:** /trans  
**Base node:** tne.transaction (default: everyone)  
**Wildcard node:** tne.transaction.*


| Command                                                            | Permission                                              | Description                                                                           | Default       |
|--------------------------------------------------------------------|---------------------------------------------------------|---------------------------------------------------------------------------------------|---------------|
| transaction away \[page #\]                                        | tne.transaction.away                                    | Displays transactions that you missed since the last time you were on. | everyone |
| transaction history \[player:name\] \[page:#\] \[world:name/all\]  | tne.transaction.history / tne.transaction.history.other | See a detailed break down of your transaction history. Page ~ The page number you wish to view. World ~ The world name you wish to filter, or all for every world. Defaults to current world. | everyone |
| transaction info \<uuid\>                                          | tne.transaction.info                                    | Displays information about a transaction. UUID ~ The id of the transaction. | everyone |
| transaction void \<uuid\>                                          | tne.transaction.void                                    | Undoes a previously completed transaction. UUID ~ The id of the transaction. | ops |