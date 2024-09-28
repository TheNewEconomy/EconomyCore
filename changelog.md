# 0.1.2.0 Build 29

- Added message for console that outlines when accounts are loaded with balances of currencies that
  don't exist.
- Made default last online date be the same as creation date for player accounts
    - This is now taken into account for ImportItems config in config.yml
- Add TNK Account Types for alpha testing The New Kings
    - Adds Village, Kingdom, Pact, Camp types
- Added Redis Support. This will allow data syncing without the need for bungee
- Added a currency template module(requested by stoffeh)
    - This contains some example pre-configured commonly used currency setups for TNE.
- Added $currency, $player variables for /money give/take messages in messages.yml
- Updated Turkish Translation(thanks to Colonel Kai)
- Ability to do /money give/take as a %, example /money take 20% player
- Fix issue where /money withdraw/deposit didn't throw message when not using a mixed currency type.