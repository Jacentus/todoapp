package todoapp.todoapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todoapp.todoapp.model.Task;
import todoapp.todoapp.model.TaskRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/* @RestController adnotacja Spring, dodaje @Controller i @ResponseBody. @Controller/*
/*@RepositoryRestController*/
//powiązanie klasy z istniejącym reporyztorium. Obiekty tych klas są zarządzane przez Springa. Skanuje klasy aby znaleźć powiązania.

@RestController //pod spodem @Controller, niżej @Component. SPRING MA KONTEKST - MIEJSCE, GDZIE ZNAJDUJĄ SIE WSZYSTKIE BEAN'Y.
class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class); //ta fabryka logów będzie tworzyła logi z klasy taskcontroller
    private final TaskRepository repository; //repozytorium na którym działamy. Rezygnujemy ze SpringData.REST. Clean architecture

    TaskController(final TaskRepository repository) { //taskrepository jest Beanem Springowym i zostanie tu wstrzyknięte
        this.repository = repository;
    } //dzięki temu co wyżej mamy już dostępne repozytorium i możemy nadpisać metody z @RepositoryRest

    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"}) //nadpisuję mapowanie, informuję że ta metoda powinna być używana zamiast domyślnej. Również adnotacja @RequestMapping ("poziom wyżej")
    //tutaj przekazuję parametry, dzieki temu widzę metadane w odpowiedzi z serwera (mam controller, stąd to wynika. Repozytorium robi to samo.)
    ResponseEntity/*<?>*/<List<Task>> readAllTasks() { //wcześniej miałem <?> i też działało
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());  //dostajemy kolekcję wszystkich tasków. Zwrócony obiekt jav'owy. Nie musimy nawet używać adnotacji ResponceEntity, Spring sam zwraca obiekt typu Lista (który dostał w JSON). Abstrakcja @ResponceEntity pozwala nam łatwiej dawać parametry.
    } //nadpisując tą metodę straciliśmy dostęp do danych typu _embedded, _links (a więc tych pozwalających na pisanie API zgodnie z HATEOAS). Aby to odzyskać można skorzystać z projektu Spring HATEOAS
    //metoda zwracająca listę wszystkich Tasków

    @GetMapping("/tasks")
        //przeciążamy metodę. Odpali się, jeśli nie podamy żadnych parametrów.
    ResponseEntity<List<Task>> readAllTasks(Pageable page) { //Pageable to struktura danych Springa, obiekt który pozwala nam na stronicowanie (ctrl + b - do dokumentacji). Spring wstrzykuje obiekty do metod jeśli zmapuje matodę na podstawie rządania http.
        logger.warn("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent()); //z hierarchi wynika, że nasze repozytorium dziedziczy z JPARepository, a ono z PagingAndSortingRepository (która ma metodę findAll())
    }//getter getContent jest z klasy Page.

    @PutMapping("/tasks/{id}") //id mamy zdefiniowane w adresie
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom(toUpdate);
                    repository.save(task);//zadziała tak samo jak @Transactional, zostanie zapisane w bazie danych
                });
        return ResponseEntity.noContent().build();
    }

     //aspekt. Dla Springa jest to znak, że na początku ma być begin,
    // na końcu end, warunek public. Dzięki temu Hibernate zmieni w bazie danych status jak w metodzie (zrobi commit).


    @Transactional //jesli adnotacja Transactional, to zawsze metoda publiczna. Jeśli poleci wyjątek (unchecked) to cała transakcja się nie wykona.
    @PatchMapping("/tasks/{id}") //Aspect Oriented Programming.
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone())); //dla danego task ustawiamy Done na zaprzeczenie jego aktualnego stanu
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tasks/{id}")
    ResponseEntity<Task>readTask(@PathVariable int id){
        logger.warn("Reading a task with id no: " + id);
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/tasks")
    ResponseEntity<Task>createTask(@RequestBody @Valid Task toCreate){
        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/"+result.getId())).body(result);
    }
}

