-- UTENTE ADMIN
INSERT INTO users (nome, cognome, email) VALUES ('Antony', 'Bosianu', 'ant.bosianu@stud.uniroma3.com');
INSERT INTO credentials (username, password, ruolo, user_id) VALUES ('admin', '$2a$10$3Yqv3/gAjvxXDCSN530ud.3LN43XAQsfSjx0YqT7FhnDnxnR8.KD.', 'ADMIN', (SELECT id FROM users WHERE email = 'ant.bosianu@stud.uniroma3.com'));

-- UTENTI DI PROVA
INSERT INTO users (nome, cognome, email) VALUES ('user1', 'user1', 'user1@example.com');
INSERT INTO credentials (username, password, ruolo, user_id) VALUES ('user1', '$2a$10$PWpg90PuwuLwoVgVbAAYyObZBJq33QE/Hn63In8dpGbM/vHaEv4ja', 'USER', (SELECT id FROM users WHERE email = 'user1@example.com'));

INSERT INTO users (nome, cognome, email) VALUES ('user2', 'user2', 'user2@example.com');
INSERT INTO credentials (username, password, ruolo, user_id) VALUES ('user2', '$2a$10$ZwggSJtl70zOJY.f6eouQOjZr7/Brqoav6r9fax8iFva2ivXRF8FK', 'USER', (SELECT id FROM users WHERE email = 'user2@example.com'));