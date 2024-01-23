# java-filmorate
Template repository for Filmorate project.
## Это репозиторий проекта "Filmorate"  

------
#### Было сложно, но мы справились
Наше приложение **умеет**:
1. Создавать и обновлять фильмы по эндпоинтам.
2. Аналогичные запросы с пользователями.
3. Возможность ставить фильмам лайки, а также  
менять список жанров и возрастные рейтинги.
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
```

------

Структура БД представлена в .png файле проекта

**Для работы с Postgres необходимо:**  

*1. В строке spring.datasource.url=jdbc:postgresql://localhost:port/DBname*   
*заменить port и DBname на соответсвующие значения в настройках Вашей БД*  
*2. spring.datasource.username=*  
*3. spring.datasource.password=*  
*Аналогично п.1 указать логин и пароль*  
*4. Закомментировать текущие настройки h2*


