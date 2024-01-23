package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UpdateUserException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Component
@AllArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage, MpaStorage, GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        Map<String, String> params = Map.of(
                "name", film.getName(),
                "description", film.getDescription(),
                "release_date", film.getReleaseDate().toString(),
                "duration", String.valueOf(film.getDuration()),
                "mpa_id", String.valueOf(film.getMpa().getId())
        );
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        film.setId(id.longValue());
        try {
            insertGenresFroFilm(film);
        } catch (Exception e) {
            return film;
        }
        return film;
    }

    private void insertGenresFroFilm(Film film) {
        Comparator<Genre> comparator = (o1, o2) -> o1.getId() - o2.getId();
        Set<Genre> genresSorted = new TreeSet<>(comparator);
        genresSorted.addAll(film.getGenres());
        film.setGenres(genresSorted);
        Long id = film.getId();
        jdbcTemplate.update("delete from film_genres where film_id = ?", id);
        List<Genre> genres = new ArrayList<>(genresSorted);
        jdbcTemplate.batchUpdate("insert into film_genres (film_id, genre_id) values (?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Genre genre = genres.get(i);
                        ps.setLong(1, id);
                        ps.setLong(2, genre.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genresSorted.size();
                    }
                });
    }

    @Override
    public Film update(Film film) {
        try {
            Film exist = jdbcTemplate.queryForObject(
                    "select * from films where film_id = ?",
                    filmRowMapper(), film.getId());
        } catch (Exception e) {
            throw new FilmNotFoundException("Фильм не найден");
        }
        jdbcTemplate.update("update films set name = ?, " +
                        "description = ?, " +
                        "release_date = ?, " +
                        "duration = ?, " +
                        "mpa_id = ? " +
                        "where film_id = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        insertGenresFroFilm(film);
        return film;
    }


    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(
                "select f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.name as mpa_name " +
                        "from films f join mpa m on f.mpa_id = m.mpa_id order by f.film_id",
                filmRowMapper());
    }

    @Override
    public Film getFilmById(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                    "select f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.name as mpa_name " +
                            "from films f join mpa m on f.mpa_id = m.mpa_id where film_id = ? order by f.film_id",
                    filmRowMapper(), id);
        } catch (Exception e) {
            throw new FilmNotFoundException("Фильм не найден");
        }
    }

    @Override
    public void userLikeFilm(Long id, Long userId) {
        checkUserAndFilmExists(id, userId);
        jdbcTemplate.update("insert into film_likes (user_id, film_id) values (?,?)", userId, id);
    }

    @Override
    public void userDeleteLike(Long id, Long userId) {
        checkUserAndFilmExists(id, userId);
        jdbcTemplate.update("delete from film_likes where user_id = ? and film_id = ?", userId, id);
    }

    @Override
    public List<Film> getMostLiked(int count) {
        List<Film> popularFilms = jdbcTemplate.query(
                "select * from films as f " +
                        "join (select film_id, count(film_id) likes_amount " +
                        "from film_likes group by film_id" +
                        ") as a ON f.film_id = a.film_id order by a.likes_amount desc limit ?", filmRowMapper(), count
        );
        if (popularFilms.isEmpty()) {
            return jdbcTemplate.query("select * from films", filmRowMapper());
        }
        return popularFilms;
    }

    @Override
    public Mpa getMpaById(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                    "select * from mpa where mpa_id = ?",
                    mpaRowMapper(), id);
        } catch (Exception e) {
            throw new NotFoundException("Mpa c id %s не найден", id);
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query(
                "select * from mpa",
                mpaRowMapper()
        );
    }

    @Override
    public Genre getGenreById(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                    "select * from genre where genre_id = ?",
                    genreRowMapper(), id);
        } catch (Exception e) {
            throw new NotFoundException("Жанр c id %s не найден", id);
        }
    }

    @Override
    public Set<Genre> getAllGenres() {
        Comparator<Genre> comparator = (o1, o2) -> o1.getId() - o2.getId();
        Set<Genre> genres = new TreeSet<>(comparator);
        List<Genre> list = jdbcTemplate.query(
                "select * from genre",
                genreRowMapper()
        );
        genres.addAll(jdbcTemplate.query(
                "select * from genre",
                genreRowMapper()
        ));
        return genres;
    }

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getLong("film_id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));

            Mpa mpa = mpaForFilm(film);
            film.setMpa(mpa);

            List<Genre> genres = getGenresForFilm(film.getId());
            Set<Genre> genreSet = new LinkedHashSet<>(genres);
            film.setGenres(genreSet);
            return film;
        };
    }

    private Mpa mpaForFilm(Film film) {
        return jdbcTemplate.queryForObject("select  f.mpa_id, m.name as mpa_name " +
                "from films f join mpa m on f.mpa_id = m.mpa_id where f.film_id = ?", mpaFilmsRowMapper(), film.getId());
    }

    private List<Genre> getGenresForFilm(Long filmId) {
        return jdbcTemplate.query("select g.genre_id, g.name from films f " +
                "join film_genres fg on f.film_id = fg.film_id " +
                "join genre g on fg.genre_id = g.genre_id where f.film_id = ?;", genreRowMapper(), filmId);
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            return user;
        };
    }

    private RowMapper<Mpa> mpaFilmsRowMapper() {
        return (rs, rowNum) -> {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("mpa_id"));
            mpa.setName(rs.getString("mpa_name"));
            return mpa;
        };
    }

    private RowMapper<Mpa> mpaRowMapper() {
        return (rs, rowNum) -> {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("mpa_id"));
            mpa.setName(rs.getString("name"));
            return mpa;
        };
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("name"));
            return genre;
        };
    }

    public void checkUserAndFilmExists(Long id, Long userId) {
        try {
            jdbcTemplate.queryForObject(
                    "select * from films where film_id = ?",
                    filmRowMapper(), id);
        } catch (Exception e) {
            throw new FilmNotFoundException("Фильм не найден");
        }
        try {
            jdbcTemplate.queryForObject(
                    "select * from users where user_id = ?",
                    userRowMapper(), userId);
        } catch (Exception e) {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

//    public void test() {
//
//    }
}
