--INSERT INTO users (name, login, email, birthday)
--VALUES
--('Ivan', 'Ivan_login', 'ivan@mail.ru', '1998-12-03'),
--('Vasiliy', 'Vasiliy_login', 'vasiliy@mail.ru', '1994-11-23'),
--('Petr', 'Petr_login', 'petr@mail.ru', '1993-10-01'),
--('Victor', 'Victor_login', 'victor@mail.ru', '1991-02-05'),
--('Denis', 'Denis_login', 'denis@mail.ru', '1990-03-12');

--INSERT INTO friends (user_id, friend_id)
--VALUES
--(3, 2),
--(2, 1),
--(2, 3),
--(2, 4);

INSERT INTO genre (name)
VALUES
('Комедия'),
('Драма'),
('Мультфильм'),
('Триллер'),
('Документальный'),
('Боевик');


INSERT INTO mpa (name)
VALUES
('G'),
('PG'),
('PG-13'),
('R'),
('NC-17');


--INSERT INTO films (name, description, release_date, duration, mpa_id)
--VALUES
--('фильм 1', 'описание фильма 1', '1991-01-01', '65', 2),
--('фильм 2', 'описание фильма 2', '1991-02-02', '70', 1),
--('фильм 3', 'описание фильма 3', '1991-02-03', '75', 3),
--('фильм 4', 'описание фильма 4', '1991-02-04', '80', 5),
--('фильм 5', 'описание фильма 5', '1991-02-05', '90', 4);


--INSERT INTO film_genres (film_id, genre_id)
--VALUES
--(1, 4),
--(1, 5),
--(2, 1),
--(3, 5),
--(4, 2),
--(1, 3);

--INSERT INTO film_likes (film_id, user_id)
--VALUES
--(1, 3),
--(1, 4),
--(2, 3),
--(2, 1),
--(2, 2);



