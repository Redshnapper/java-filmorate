package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Primary
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User add(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        Map<String, String> params = Map.of(
                "login", user.getLogin(),
                "name", user.getName(),
                "email", user.getEmail(),
                "birthday", user.getBirthday().toString());
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        user.setId(id.longValue());
        return user;
    }

    @Override
    public User update(User user) {
        try {
            User exist = jdbcTemplate.queryForObject(
                    "select * from users where user_id = ?",
                    userRowMapper(), user.getId());
        } catch (Exception e) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        jdbcTemplate.update("update users set login = ?, name = ?, email = ?, birthday = ? where user_id = ?",
                user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        checkUserAndFriendExists(id, friendId);
        jdbcTemplate.update("insert into friends (user_id, friend_id) values (?,?)", id, friendId);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        checkUserAndFriendExists(id, friendId);
        jdbcTemplate.update("delete from friends where user_id = ? and friend_id = ?", id, friendId);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "select * from users",
                userRowMapper());
    }

    @Override
    public User getById(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                    "select * from users where user_id = ?",
                    userRowMapper(), id);
        } catch (Exception e) {
            throw new UserNotFoundException("Пользователь не найден");
        }

    }

    @Override
    public List<User> getUserFriends(Long id) {
        return jdbcTemplate.query(
                "select u2.user_id, u2.email, u2.login, u2.name, u2.birthday " +
                        "from users u join friends f on u.user_id = f.user_id " +
                        "join users u2 on f.friend_id = u2.user_id where u.user_id = ?",
                userRowMapper(), id);
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        return jdbcTemplate.query("select * from users where user_id in (" +
                        "select fr1.user_id common_friends " +
                        "from (" +
                        "select u2.user_id, u2.email, u2.login, u2.name, u2.birthday " +
                        "from users u join friends f on u.user_id = f.user_id " +
                        "join users u2 on f.friend_id = u2.user_id " +
                        "where u.user_id = ?" +
                        ") fr1 " +
                        "join (" +
                        "select u2.user_id, u2.email, u2.login, u2.name, u2.birthday " +
                        "from users u join friends f on u.user_id = f.user_id " +
                        "join users u2 on f.friend_id = u2.user_id " +
                        "where u.user_id = ? " +
                        ") fr2 on fr1.user_id = fr2.user_id );",
                userRowMapper(), id, otherId);
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

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getLong("film_id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));

            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("mpa_id"));
            mpa.setName(rs.getString("name"));
            film.setMpa(mpa);
            return film;
        };
    }

    public void checkUserAndFriendExists(Long id, Long friendId) {
        try {
            jdbcTemplate.queryForObject(
                    "select * from users where user_id = ?",
                    userRowMapper(), id);
        } catch (Exception e) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        try {
            jdbcTemplate.queryForObject(
                    "select * from users where user_id = ?",
                    userRowMapper(), friendId);
        } catch (Exception e) {
            throw new UserNotFoundException("Друг пользователя не найден");
        }
    }
}
