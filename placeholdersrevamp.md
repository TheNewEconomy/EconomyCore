# TheNewEconomy Placeholders

## 🧮 Placeholder Value Types

These dynamic parts are used within various placeholders:

| Placeholder Token | Description                                                       |
|-------------------|-------------------------------------------------------------------|
| `$currency`       | Identifier for the currency (e.g., `gold`, `dollar`)              |
| `$storage`        | A storage type: `all`, `normal`, `ender`, `inventory`, `virtual`  |
| `$amount`         | Numeric value such as `1`, `100`, `2.5k`, `1e6`                   |
| `$account`        | A UUID or username representing the account                       |
| `$checkaccount`   | Another UUID or username used for comparison or permission checks |
| `$region`         | Region identifier, typically a world name                         |
| `$number`         | A valid non-negative number (e.g., `1`, `5`, `10`)                |

---

## 💰 Balance Placeholders

| Placeholder                                                   | Description                   |
|---------------------------------------------------------------|-------------------------------|
| `tne_balance`                                                 | Default balance               |
| `tne_balance_formatted`                                       | Formatted default balance     |
| `tne_balance_currency_$currency`                              | Balance in specific currency  |
| `tne_balance_currency_$currency_formatted`                    | Formatted currency balance    |
| `tne_balance_region_$region`                                  | Balance in specific region    |
| `tne_balance_region_$region_formatted`                        | Formatted region balance      |
| `tne_balance_storage_$storage`                                | Balance in specific storage   |
| `tne_balance_storage_$storage_formatted`                      | Formatted storage balance     |
| `tne_balance_curreg_$currency_$region`                        | Currency + region balance     |
| `tne_balance_curreg_$currency_$region_formatted`              | Formatted currency + region   |
| `tne_balance_curregstor_$currency_$region_$storage`           | Full triple-balance breakdown |
| `tne_balance_curregstor_$currency_$region_$storage_formatted` | Formatted full balance        |
| `tne_balance_curstor_$currency_$storage`                      | Currency + storage            |
| `tne_balance_curstor_$currency_$storage_formatted`            | Formatted currency + storage  |
| `tne_balance_regstor_$region_$storage`                        | Region + storage              |
| `tne_balance_regstor_$region_$storage_formatted`              | Formatted region + storage    |

---

## 👤 Account Balance Placeholders

| Placeholder                                                               | Description                 |
|---------------------------------------------------------------------------|-----------------------------|
| `tne_accbalance_$account`                                                 | Balance of another account  |
| `tne_accbalance_$account_formatted`                                       | Formatted version           |
| `tne_accbalance_$account_currency_$currency`                              | Currency-specific           |
| `tne_accbalance_$account_currency_$currency_formatted`                    | Formatted currency-specific |
| `tne_accbalance_$account_region_$region`                                  | Region-specific             |
| `tne_accbalance_$account_region_$region_formatted`                        | Formatted region            |
| `tne_accbalance_$account_storage_$storage`                                | Storage-specific            |
| `tne_accbalance_$account_storage_$storage_formatted`                      | Formatted storage           |
| `tne_accbalance_$account_curreg_$currency_$region`                        | Currency + region           |
| `tne_accbalance_$account_curreg_$currency_$region_formatted`              | Formatted currency + region |
| `tne_accbalance_$account_curregstor_$currency_$region_$storage`           | Full                        |
| `tne_accbalance_$account_curregstor_$currency_$region_$storage_formatted` | Formatted full              |
| `tne_accbalance_$account_curstor_$currency_$storage`                      | Currency + storage          |
| `tne_accbalance_$account_curstor_$currency_$storage_formatted`            | Formatted                   |
| `tne_accbalance_$account_regstor_$region_$storage`                        | Region + storage            |
| `tne_accbalance_$account_regstor_$region_$storage_formatted`              | Formatted                   |

---

## 🏆 Leaderboard Placeholders

These placeholders provide access to leaderboard-related information, including positions, holders,
formatted entries, and balances.

| Placeholder                                    | Description                                                                  |
|------------------------------------------------|------------------------------------------------------------------------------|
| `tne_toppos_$currency`                         | Your position on the leaderboard for the given currency.                     |
| `tne_toppos_$currency_$account`                | Position of a specific account on the currency leaderboard.                  |
| `tne_toppos_$currency_account_$number`         | Account name at the specified leaderboard position. Returns `null` if empty. |
| `tne_toppos_$currency_holder_$number`          | Holder (account name) at the specified position.                             |
| `tne_toppos_$currency_holder_$number_$account` | Holder at specified position scoped for the given account.                   |
| `tne_toppos_$currency_balance_$number`         | Balance at the specified leaderboard position.                               |

### 🔍 Notes

- **Position Placeholders** (`tne_toppos_$currency_$account`) return a number (e.g., `1`, `25`).
- **Balance Placeholders** return the numeric value of the balance at that position.
- Placeholders like `tne_toppos_$currency_holder` supports localized formatting
  via `Messages.Money.PlaceholderTopEntry` in `messages.yml`.

---

## 🆔 Account Information Placeholders

| Placeholder                    | Description                          |
|--------------------------------|--------------------------------------|
| `tne_account_created_$account` | Date of account creation             |
| `tne_account_id_$account`      | UUID or name of account              |
| `tne_account_status_$account`  | Status (e.g., `active`, `suspended`) |
| `tne_account_type_$account`    | Type: `player`, `bank`, `npc`, etc   |

---

## 🔐 Account Permission Placeholders

| Placeholder                               | Description                                      |
|-------------------------------------------|--------------------------------------------------|
| `tne_can_balance_$account_$checkaccount`  | Can $checkaccount view balance of $account?      |
| `tne_can_deposit_$account_$checkaccount`  | Can $checkaccount deposit to $account?           |
| `tne_can_invite_$account_$checkaccount`   | Can $checkaccount invite others to $account?     |
| `tne_can_transfer_$account_$checkaccount` | Can $checkaccount transfer ownershipof $account? |
| `tne_can_withdraw_$account_$checkaccount` | Can $checkaccount withdraw from $account?        |

---

## 🔁 Transaction Placeholders

| Placeholder                                             | Description                 |
|---------------------------------------------------------|-----------------------------|
| `tne_tx_give_$account_$amount`                          | Give funds                  |
| `tne_tx_set_$account_$amount`                           | Set exact amount            |
| `tne_tx_take_$account_$amount`                          | Take funds                  |
| `tne_tx_give_$account_$amount_currency_$currency`       | Give in currency            |
| `tne_tx_set_$account_$amount_currency_$currency`        | Set with currency           |
| `tne_tx_take_$account_$amount_currency_$currency`       | Take with currency          |
| `tne_tx_give_$account_$amount_region_$region`           | Region-specific give        |
| `tne_tx_set_$account_$amount_region_$region`            | Region-specific set         |
| `tne_tx_take_$account_$amount_region_$region`           | Region-specific take        |
| `tne_tx_give_$account_$amount_curreg_$currency_$region` | Give with currency + region |
| `tne_tx_set_$account_$amount_curreg_$currency_$region`  | Set with currency + region  |
| `tne_tx_take_$account_$amount_curreg_$currency_$region` | Take with currency + region |

---

## 💱 Currency Information Placeholders

| Placeholder                                  | Description                      |
|----------------------------------------------|----------------------------------|
| `tne_currency_name_$currency`                | Currency name                    |
| `tne_currency_symbol_$currency`              | Currency symbol (e.g., `$`, `Ƀ`) |
| `tne_currency_precision_$currency`           | Decimal places                   |
| `tne_currency_type_$currency`                | Currency classification          |
| `tne_currency_test_$currency_<test balance>` | Test formatting a value          |

---

## 🛠️ Misc / Utility Placeholders

| Placeholder   | Description                  |
|---------------|------------------------------|
| `tne_debug`   | Whether debug mode is active |
| `tne_version` | Plugin version               |
