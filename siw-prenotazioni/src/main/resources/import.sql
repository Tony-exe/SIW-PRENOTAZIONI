-- UTENTE ADMIN
INSERT INTO users (nome, cognome, email) VALUES ('Antony', 'Bosianu', 'ant.bosianu@stud.uniroma3.com');
INSERT INTO credentials (username, password, ruolo, user_id) VALUES ('admin', '$2a$10$3Yqv3/gAjvxXDCSN530ud.3LN43XAQsfSjx0YqT7FhnDnxnR8.KD.', 'ADMIN', (SELECT id FROM users WHERE email = 'ant.bosianu@stud.uniroma3.com'));