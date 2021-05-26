INSERT INTO users (id, email, password) VALUES (1, 'bruno@yahoo.com', '$2a$10$B.3dWSKFW9TQmf0SVQskxOZRHiLmFQTtbsI3GlfxUK.KFPnXMFIoW'), (2, 'franco@yopmail.com', '$2a$10$CgNRZ1fcr009vw9XduhgLObgMAu2EK47/nskgn3txC2tjPcipmY0K');
INSERT INTO playlists (id, user_id, name) VALUES (1, 1, 'Lista de Bruno'), (2, 2, 'Lista de Franco');
INSERT INTO songs (id, name, author, genre) VALUES (1, 'El 38', 'Divididos', 0), (2, 'Pibe Cantina', 'Yerba Brava', 7), (3, 'I Shot the Sheriff', 'Bob Marley', 6), (4, 'El Otoño', 'Antonio Vivaldi', 5), (5, 'Sandwiches de Miga', 'Pappo''s Blues', 0), (6, 'Subterranean Homesick Blues', 'Bob Dylan', 4);
INSERT INTO playlists_songs (playlist_id, song_id) VALUES (1, 1), (1, 2), (1, 3), (2, 4), (2, 5), (2, 6);
