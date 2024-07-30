INSERT INTO dw.provider (id, provider_ref_id, name, current)
VALUES (1, 1001, 'Provider 1', TRUE),
       (2, 1002, 'Provider 2', TRUE);

INSERT INTO dw.withdrawal (withdrawal_id, sequence_id, currency_code, provider_id, current)
VALUES ('w1', 's1', 'USD', 1001, TRUE),
       ('w2', 's2', 'EUR', 1002, TRUE);

INSERT INTO dw.payment (invoice_id, payment_id, sequence_id, change_id, currency_code)
VALUES ('inv1', 'pay1', 'seq1', 'chg1', 'USD'),
       ('inv2', 'pay2', 'seq2', 'chg2', 'EUR');

INSERT INTO dw.payment_route (id, invoice_id, route_provider_id, current)
VALUES (1, 'inv1', 1001, TRUE),
       (2, 'inv2', 1002, TRUE);