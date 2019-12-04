/*
 * Insert lists 
*/

INSERT INTO list (id, name, owner_id) 
VALUES (1, 'Testilista 1', 'testUser');

INSERT INTO list (id, name, owner_id) 
VALUES (2, 'Testilista 2', 'testUser');

INSERT INTO list (id, name, owner_id) 
VALUES (3, 'Testilista 3', 'testUser');

/*
 * Insert list items 
*/

INSERT INTO listitem (id, name, owner_id, checked, list_id) 
VALUES (1, 'Testituote 1', 'testUser', 0, 1);

INSERT INTO listitem (id, name, owner_id, checked, list_id) 
VALUES (2, 'Testituote 2', 'testUser', 0, 1);

INSERT INTO listitem (id, name, owner_id, checked, list_id) 
VALUES (3, 'Testituote 3', 'testUser', 1, 1);

INSERT INTO listitem (id, name, owner_id, checked, list_id) 
VALUES (4, 'Testituote 4', 'testUser', 1, 1);

