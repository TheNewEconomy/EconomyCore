-- accounts table
CREATE TABLE IF NOT EXISTS tne_accounts (
    uid BINARY(16) NOT NULL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    account_type VARCHAR(30) NOT NULL,
    created DATETIME NOT NULL,
    pin VARCHAR(16),
    status VARCHAR(36)
    );

CREATE TABLE IF NOT EXISTS tne_non_players_accounts (
    uid BINARY(16) NOT NULL UNIQUE,
    owner BINARY(16) NOT NULL,

    FOREIGN KEY(uid) REFERENCES tne_accounts(uid) ON DELETE CASCADE,
    FOREIGN KEY(owner) REFERENCES tne_accounts(uid) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS tne_players_accounts (
    uid BINARY(16) NOT NULL UNIQUE,
    last_online DATETIME NOT NULL,
    FOREIGN KEY(uid) REFERENCES tne_accounts(uid) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS tne_account_members (
    uid BINARY(16) NOT NULL,
    account BINARY(16) NOT NULL,
    perm VARCHAR(36) NOT NULL,
    perm_value TINYINT(1) NOT NULL,

    FOREIGN KEY(uid) REFERENCES tne_accounts(uid) ON DELETE CASCADE,
    FOREIGN KEY(account) REFERENCES tne_accounts(uid) ON DELETE CASCADE
    );

-- holdings table
CREATE TABLE IF NOT EXISTS tne_holdings (
    uid BINARY(16) NOT NULL,
    server VARCHAR(40) NOT NULL,
    region VARCHAR(40) NOT NULL,
    currency BINARY(16) NOT NULL,
    holdings_type VARCHAR(30) NOT NULL,
    holdings DECIMAL(49, 4) NOT NULL,

    UNIQUE(`uid`, `server`, `region`, `currency`, `holdings_type`),
    FOREIGN KEY(uid) REFERENCES tne_accounts(uid) ON DELETE CASCADE
    );

-- receipt table
CREATE TABLE IF NOT EXISTS tne_receipts (
    uid BINARY(16) NOT NULL,
    performed DATETIME NOT NULL,
    receipt_type VARCHAR(30) NOT NULL,
    receipt_source VARCHAR(60) NOT NULL,
    receipt_source_type VARCHAR(30) NOT NULL,
    archive TINYINT(1) NOT NULL,
    voided TINYINT(1) NOT NULL
    );

-- receipt table
CREATE TABLE IF NOT EXISTS tne_receipts_holdings (
    uid BINARY(16) NOT NULL UNIQUE,
    participant BINARY(16) NOT NULL,
    ending TINYINT(1) NOT NULL,
    server VARCHAR(40) NOT NULL,
    region VARCHAR(40) NOT NULL,
    currency BINARY(16) NOT NULL,
    holdings_type VARCHAR(30) NOT NULL,
    holdings DECIMAL(49, 4) NOT NULL,

    FOREIGN KEY(uid) REFERENCES tne_receipts(uid) ON DELETE CASCADE
    );

-- Participants of transactions/receipts.
CREATE TABLE IF NOT EXISTS tne_receipts_participants (
    uid BINARY(16) NOT NULL PRIMARY KEY,
    participant BINARY(16) NOT NULL,
    participant_type VARCHAR(10) NOT NULL,
    tax DECIMAL(49, 4) NOT NULL,

    FOREIGN KEY(uid) REFERENCES tne_receipts(uid) ON DELETE CASCADE
    );

-- Holdings modifiers for receipts
CREATE TABLE IF NOT EXISTS tne_receipts_modifiers (
    uid VARCHAR(36) NOT NULL PRIMARY KEY,
    participant BINARY(16) NOT NULL,
    participant_type VARCHAR(10) NOT NULL,
    operation VARCHAR(10) NOT NULL,
    region VARCHAR(40) NOT NULL,
    currency BINARY(16) NOT NULL,
    modifier DECIMAL(49, 4) NOT NULL,

    FOREIGN KEY(uid) REFERENCES tne_receipts(uid) ON DELETE CASCADE
    );