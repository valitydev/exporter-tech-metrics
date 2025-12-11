CREATE SCHEMA IF NOT EXISTS dw;

DROP TABLE IF EXISTS dw.withdrawal;
DROP TABLE IF EXISTS dw.provider;
DROP TABLE IF EXISTS dw.terminal;
DROP TABLE IF EXISTS dw.payment;
DROP TABLE IF EXISTS dw.payment_route;

CREATE TABLE IF NOT EXISTS dw.withdrawal
(
    withdrawal_id VARCHAR(255),
    sequence_id   VARCHAR(255),
    currency_code VARCHAR(10),
    provider_id   INTEGER,
    terminal_id   VARCHAR(255),
    current       BOOLEAN,
    PRIMARY KEY (withdrawal_id, sequence_id)
);

CREATE TABLE IF NOT EXISTS dw.provider
(
    id              INTEGER PRIMARY KEY,
    provider_ref_id INTEGER,
    name            VARCHAR(255),
    current         BOOLEAN
);

CREATE TABLE IF NOT EXISTS dw.terminal
(
    id              INTEGER PRIMARY KEY,
    terminal_ref_id INTEGER,
    name            VARCHAR(255),
    current         BOOLEAN
);

CREATE TABLE IF NOT EXISTS dw.payment
(
    invoice_id    VARCHAR(255),
    payment_id    VARCHAR(255),
    sequence_id   VARCHAR(255),
    change_id     VARCHAR(255),
    currency_code VARCHAR(10),
    PRIMARY KEY (invoice_id, payment_id, sequence_id, change_id)
);

CREATE TABLE IF NOT EXISTS dw.payment_route
(
    id                INTEGER PRIMARY KEY,
    invoice_id        VARCHAR(255),
    route_terminal_id INTEGER,
    route_provider_id INTEGER,
    current           BOOLEAN
);

SELECT * FROM dw.provider;
SELECT * FROM dw.terminal;
SELECT * FROM dw.payment;
SELECT * FROM dw.payment_route;
SELECT * FROM dw.withdrawal;