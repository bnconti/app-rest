INSERT INTO users (email, password) VALUES ('bruno@yahoo.com', '$2a$10$B.3dWSKFW9TQmf0SVQskxOZRHiLmFQTtbsI3GlfxUK.KFPnXMFIoW'), ('franco@yopmail.com', '$2a$10$CgNRZ1fcr009vw9XduhgLObgMAu2EK47/nskgn3txC2tjPcipmY0K');
INSERT INTO playlists (user_id, name) VALUES (1, 'Lista de Bruno'), (2, 'Lista de Franco');
INSERT INTO songs (name, author, genre) VALUES ('El 38', 'Divididos', 0), ('Pibe Cantina', 'Yerba Brava', 7), ('I Shot the Sheriff', 'Bob Marley', 6), ('El Otoño', 'Antonio Vivaldi', 5), ('Sandwiches de Miga', 'Pappo''s Blues', 0), ('Subterranean Homesick Blues', 'Bob Dylan', 4), ('La ñapi de mamá', 'Divididos', 0);
INSERT INTO playlists_songs (playlist_id, song_id) VALUES (1, 1), (1, 2), (1, 3), (2, 4), (2, 5), (2, 6), (2, 7);
