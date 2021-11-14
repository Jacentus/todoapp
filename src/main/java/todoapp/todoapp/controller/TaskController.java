package todoapp.todoapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import todoapp.todoapp.model.Task;
import todoapp.todoapp.model.TaskRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RepositoryRestController //powiązanie klasy z istniejącym reporyztorium. Obiekty tych klas są zarządzane przez Springa. Skanuje klasy aby znaleźć powiązania.
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class); //ta fabryka logów będzie tworzyła logi z klasy taskcontroller
    private final TaskRepository repository; //repozytorium na którym działamy

    public TaskController(final TaskRepository repository) { //taskrepository jest Beanem Springowym i zostanie tu wstrzyknięte
        this.repository = repository;
    }

    //dzięki temu co wyżej mamy już dostępne repozytorium i możemy nadpisać metody z @RepositoryRest

    //nadpisuję mapowanie, informuję że ta metoda powinna być używana zamiast domyślnej. Również adnotacja @RequestMapping ("poziom wyżej")
    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"}) //tutaj przekazuję parametry, dzieki temu widzę metadane w odpowiedzi z serwera (mam controller, stąd to wynika. Repozytorium robi to samo.)
    ResponseEntity<?> /*List<Task> */readAllTasks(){
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());  //dostajemy kolekcję wszystkich tasków. Zwrócony obiekt jav'owy. Nie musimy nawet używać adnotacji ResponceEntity, Spring sam zwraca obiekt typu Lista (który dostał w JSON). Abstrakcja @ResponceEntity pozwala nam łatwiej dawać parametry.
    } //nadpisując tą metodę straciliśmy dostęp do danych typu _embedded, _links (a więc tych pozwalających na pisanie API zgodnie z HATEOAS). Aby to odzyskać można skorzystać z projektu Spring HATEOAS
    //metoda zwracająca listę wszystkich Tasków


    @GetMapping(value = "/tasks") //przeciążamy metodę. Odpali się, jeśli nie podamy żadnych parametrów.
    ResponseEntity<?> readAllTasks(Pageable page){ //Pageable to struktura danych Springa, obiekt który pozwala nam na stronicowanie (ctrl + b - do dokumentacji).
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());


}

