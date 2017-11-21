INSERT INTO client(city, country, number, postal_code, province, street, unit, category, creation_date, default_term, email, full_name)
        VALUES('Québec', 'Canada', '12', 'L9W 2C8', 'Québec', 'Avenue Godin', '', 'VIP', NOW(), 'IMMEDIATE', 'derek.batista@notanemail.com', 'Derek Batista');

INSERT INTO client(city, country, number, postal_code, province, street, unit, category, creation_date, default_term, email, full_name)
        VALUES('Montréal', 'Canada', '3463', 'M9N1G4', 'Québec', 'Avenue de Darlington', 'A', 'REGULAR', NOW(), 'DAYS30', 'samuel.donovan@notanemail.com', 'Samuel Donovan');

INSERT INTO client(city, country, number, postal_code, province, street, unit, category, creation_date, default_term, email, full_name)
        VALUES('Drummondville', 'Canada', '151', 'T9C 1S1', 'Québec', 'Rue Lindsay', '', 'REGULAR', NOW(), 'DAYS90', 'john.sanchez@notanemail.com', 'John Sanchez');

INSERT INTO client(city, country, number, postal_code, province, street, unit, category, creation_date, default_term, email, full_name)
        VALUES('Saint-Louis-du-Ha! Ha!', 'Canada', '619', 'V3C 4S7', 'Québec', 'Rue Lavoie', '', 'REGULAR', NOW(), 'IMMEDIATE', 'jon.brunetti@notanemail.com', 'Jon Brunetti');

INSERT INTO client(city, country, number, postal_code, province, street, unit, category, creation_date, default_term, email, full_name)
        VALUES('Paspébiac', 'Canada', '26', 'T0G 0Z0', 'Québec', 'Avenue Duret', '', 'VIP', NOW(), 'DAYS30', 'margaret.steen@notanemail.com', 'Margaret Steen');

INSERT INTO client(city, country, number, postal_code, province, street, unit, category, creation_date, default_term, email, full_name)
        VALUES('Le Précieux-Sang', 'Canada', '1048', 'H0H0H0', 'Québec', 'Route du missouri', '', 'REGULAR', NOW(), 'DAYS30', 'elidate.gregory@notanemail.com', 'Elida Gregory');

INSERT INTO product(id, name, unit_price)
        VALUES(1, 'Crayon', 1.99);

INSERT INTO product(id, name, unit_price)
        VALUES(2, 'Bureau de travail', 145.75);

INSERT INTO product(id, name, unit_price)
        VALUES(3, 'Patate', 0.99);

INSERT INTO product(id, name, unit_price)
        VALUES(4, 'Cahier Canada', 2.55);

INSERT INTO product(id, name, unit_price)
        VALUES(5, 'MacBook Pro 15"', 1869);

INSERT INTO product(id, name, unit_price)
        VALUES(6, 'License Windows™ 10 Pro', 149.99);

INSERT INTO product(id, name, unit_price)
        VALUES(7, 'Café', 15.12);

INSERT INTO product(id, name, unit_price)
        VALUES(8, 'Stylo', 3.99);

INSERT INTO product(id, name, unit_price)
        VALUES(9, 'OSTRICHPILLOW®', 99);

INSERT INTO product(id, name, unit_price)
        VALUES(10, 'Hamac', 134.95);

INSERT INTO role(id, name)
        VALUES(1, 'Administrateur');

INSERT INTO role(id, name)
        VALUES(2, 'Comptabilité');

INSERT INTO role(id, name)
        VALUES(3, 'Facturation');

INSERT INTO user(id)
        VALUES(1);
INSERT INTO user(id)
        VALUES(2);
INSERT INTO user(id)
        VALUES(3);
INSERT INTO user(id)
        VALUES(4);
INSERT INTO user(id)
        VALUES(5);

INSERT INTO user_roles(user_id, roles_id)
        VALUES(1, 1);

INSERT INTO user_roles(user_id, roles_id)
        VALUES(1, 2);

INSERT INTO user_roles(user_id, roles_id)
        VALUES(2, 2);

INSERT INTO user_roles(user_id, roles_id)
        VALUES(3, 1);

INSERT INTO user_roles(user_id, roles_id)
        VALUES(3, 2);

INSERT INTO user_roles(user_id, roles_id)
        VALUES(3, 3);

INSERT INTO user_roles(user_id, roles_id)
        VALUES(4, 3);

INSERT INTO user_roles(user_id, roles_id)
        VALUES(5, 2);
