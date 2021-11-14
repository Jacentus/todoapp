package todoapp.todoapp.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

//klasa do komunikacji z bazą danych. Coś ala API, punkt wejścia do działania na kolekcji (w tym przypadku tasków)
//interfejs dziedziczący po interfejsie z pakietu
// JpaRepository. Przekazujemy info z jakiej encji
// powinno brać dane oraz jaki jest jej identyfikator (mamy int)

@RepositoryRestResource//możemy dać dodatkowe parametry, min:(path = "todos", collectionResourceRel = "todos") //nasze repozytorium działa na obiektach TASK, stąd w adresie URL Spring daje defaultowo Tasks. Tu zmieniamy odpowiednio. Mogę o tym przeczytać w dokuementacji (ctrl+b)
        //tworzymy interfejs REST dający nam metody dostępu do baz danych (SPRING wie co dać)
        //już jedna adnotacja (wyżej) i interfejs bazowy pozwala na wszystkie operacje typu CRUD w oparciu o stronicowanie i sortowanie
public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Override
    @RestResource(exported = false) //informacja że mogę tak zrobić jest w dokumentacji. Dzięki temu blokuję dostęp do danego zapytania (DELETE), nie mogę wysłać metody http DELETE.
    //adnotacja RestResource, ctrl + spacja pokazuje co mogę tam wpisać.
    void deleteById(Integer integer);
    // repozytoria Spring (SpringData) to DSL - domain specific language.
    // Domena to procesowanie kolekcji, pod spodem łączenie się z bazą danych. Wszystkie metody są tłumaczone na zapytania SQL (bo mamy JPA). Możemy stwożyć własne metody albo nadpisywać dostępne.
    @Override
    @RestResource(exported = false)
    void delete(Task entity);

    @RestResource(path = "done", rel = "done"/*relacja, hipermedia, łącze do adresu*/) //zmieniam adres, podobnie
    List<Task> findByDone(@Param("state")boolean done);//wersja Ultimate IntelliJ podpowiedziałaby mi jakie mam opcje i po czym mogę szukać, opcji jest dużo (włącznie z szukaniem w opisie). Można określać wartość flagi.
                                    // Ta metoda, dzięki @RepositoryRestResource jest już dostępna pod jakimś adresem.
                                    //dzięki @Param mogę po adresie przekazać wartość. Wysyłając medotę HTML na adres http://localhost:8080/tasks/search/done?state=true dostaję w odpowiedzi taski z wartością done true

    //tag "_embedded w odpowiedzi JSON oznacza obiekty
    // _links to pomocnicze nakierowania co z kolekcją możemy zrobić. To wszystko bierze się z RestRepository Springa.
    //HATEOAS - stan aplikacji reprezentowany przez hipermedia (metadane). Właśnie _embedded, _links.
    // Lista tasków jest zwracan aobudowana w obiekt _embedded. To rozszerzony REST.
}
