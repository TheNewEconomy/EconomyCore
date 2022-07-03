--username table
CREATE TABLE IF NOT EXISTS tne_player_names (
    uuid CHAR(36) NOT NULL PRIMARY KEY,
    username VARCHAR(16) NOT NULL UNIQUE
);

-- accounts table
CREATE TABLE IF NOT EXISTS tne_accounts (
    id VARCHAR(60) NOT NULL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    created BIGINT(16) NOT NULL,
    last_login BIGINT(16) NOT NULL,
    pin VARCHAR(16) NOT NULL,
    status VARCHAR(36),
    account_type VARCHAR(20) NOT NULL,
    owner CHAR(36) NOT NULL,
    account_language VARCHAR() NOT NULL,
);

-- holdings table
CREATE TABLE IF NOT EXISTS tne_holdings (
    id VARCHAR(60) NOT NULL PRIMARY KEY,
    server VARCHAR(100) NOT NULL PRIMARY KEY,
    region VARCHAR(70) NOT NULL PRIMARY KEY,
    currency VARCHAR(50) NOT NULL PRIMARY KEY,
    holdings_type VARCHAR(30) NOT NULL PRIMARY KEY,
    holdings DECIMAL(49, 4) NOT NULL,
);

-- receipt table
CREATE TABLE IF NOT EXISTS tne_receipts (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    performed BIGINT(16) NOT NULL,
    receipt_type VARCHAR(30) NOT NULL,
    receipt_source VARCHAR(60) NOT NULL,
    receipt_source_type VARCHAR(30) NOT NULL,
);

-- Participants of transactions/receipts.
CREATE TABLE IF NOT EXISTS tne_receipts_participants (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    participant_type VARCHAR(10) NOT NULL,
    starting_region VARCHAR(70) NOT NULL,
    starting_currency VARCHAR(50) NOT NULL,
    starting_holdings DECIMAL(49, 4) NOT NULL,
    ending_region VARCHAR(70) NOT NULL,
    ending_currency VARCHAR(50) NOT NULL,
    ending_holdings DECIMAL(49, 4) NOT NULL,
)

-- Holdings modifiers for receipts
CREATE TABLE IF NOT EXISTS tne_receipts_modifiers (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    participant_type VARCHAR(10) NOT NULL,
    operation VARCHAR(5) NOT NULL,
    region VARCHAR(70) NOT NULL,
    currency VARCHAR(50) NOT NULL,
    modifier DECIMAL(49, 4) NOT NULL,
)