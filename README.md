[![Typing SVG](https://readme-typing-svg.herokuapp.com?color=%2336BCF7&lines=java-filmorate)](https://git.io/typing-svg)  
[![Typing SVG](https://readme-typing-svg.demolab.com?font=Lora&weight=100&size=18&pause=1000&color=A5FFB6CF&repeat=false&random=false&width=435&lines=Template+repository+for+Filmorate+project.+)](https://git.io/typing-svg)  
## Это репозиторий проекта "Filmorate"  

------
#### Было сложно, но мы справились
Наше приложение **умеет**:  
1. Создавать и обновлять фильмы по эндпоинтам.
2. Аналогичные запросы с пользователями.
3. Возможность ставить фильмам лайки, а также  
возможность менять список жанров и возрастные рейтинги.
4. Добавлять пользователей в друзья,  
сама дружба - односторонняя.
5. Работать с различными БД, на данный момент,  
в коде представлены реализации PostgresSql и H2.

------
*Примеры некоторых эндпоинтов:*  

**GET:**  

*localhost:8080/users/id - получить пользователя по id*  
*localhost:8080/films/id - получить фильм по id*  
*localhost:8080/users - получить всех пользователей*  
*localhost:8080/films - получить всех пользователей*  
*localhost:8080/users/id/friends/friendId - получить всех пользователей*

**В коде можно увидеть список аналогичных PUT и DELETE запросов, и некоторых других..** 


Приложение написано на Java. Пример кода:
```java
@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping("{id}")
    public Mpa getMpaById(@PathVariable("id") Long id) {
        log.info("Получение рейтинга по id {}", id);
        return mpaService.getMpaById(id);
    }

    @GetMapping()
    public List<Mpa> getAllMpa() {
        log.info("Получение списка рейтингов");
        return mpaService.getAllMpa();
    }
}
```
**Создание таблиц в Postgres**
```SQL
CREATE  user filmorate_user superuser
ALTER  user filmorate_user password '123'
CREATE database filmorate with owner filmorate_user

CREATE TABLE IF NOT EXISTS "users" (
  "user_id" integer GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  "email" varchar(40) NOT NULL,
  "login" varchar(40) NOT NULL,
  "name" varchar(40) NOT NULL,
  "birthday" date
);

CREATE TABLE IF NOT EXISTS "films" (
  "film_id" integer GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  "name" varchar NOT NULL,
  "description" varchar(200),
  "release_date" date,
  "duration" integer,
  "mpa_id" integer
);

CREATE TABLE IF NOT EXISTS "film_likes" (
  "user_id" integer,
  "film_id" integer
);

CREATE TABLE IF NOT EXISTS "friends" (
  "user_id" integer,
  "friend_id" integer
);

CREATE TABLE IF NOT EXISTS "mpa" (
  "mpa_id" integer GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  "name" varchar(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS "genre" (
  "genre_id" integer GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
  "name" varchar(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS "film_genres" (
  "film_id" integer,
  "genre_id" integer
);

ALTER TABLE "film_likes" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id");

ALTER TABLE "film_likes" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("film_id");

ALTER TABLE "friends" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id");

ALTER TABLE "friends" ADD FOREIGN KEY ("friend_id") REFERENCES "users" ("user_id");

ALTER TABLE "film_genres" ADD FOREIGN KEY ("film_id") REFERENCES "films" ("film_id");

ALTER TABLE "films" ADD FOREIGN KEY ("mpa_id") REFERENCES "mpa" ("mpa_id");

ALTER TABLE "film_genres" ADD FOREIGN KEY ("genre_id") REFERENCES "genre" ("genre_id"); 
```
------
**Наполнение данными в postgres требует обязательного заполнения таблиц genre и mpa**  
Например так:
```sql
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
```

------

**Создание таблиц и наполнение их данными (в h2), представлено в файлах schema.sql и data.sql**

------
Структура БД представлена в .png файле проекта

**Для работы с Postgres необходимо:**  

*1. В строке spring.datasource.url=jdbc:postgresql://localhost:port/DBname*   
*заменить port и DBname на соответсвующие значения в настройках Вашей БД*  
*2. spring.datasource.username=*  
*3. spring.datasource.password=*  
*Аналогично п.1 указать логин и пароль*  
*4. Закомментировать текущие настройки h2*


