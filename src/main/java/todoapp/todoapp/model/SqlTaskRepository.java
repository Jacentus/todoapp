package todoapp.todoapp.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//klasa do komunikacji z bazą danych. Coś ala API, punkt wejścia do działania na kolekcji (w tym przypadku tasków)
//interfejs dziedziczący po interfejsie z pakietu JPARepository, specyfikajca Javy jak podejść do ORM (obiect relational mapping)
//JPA - SPEFYCIKACJA(interfejs) HIBERNATE - jego implementacja.

//w JPA mamy w środku JDBC - konektor javowy do rozmowy z bazą danych.

//HIKARICP (Hikary connection pool). Pula połączeń do bazy danych.

// JpaRepository. Przekazujemy info z jakiej encji
// powinno brać dane oraz jaki jest jej identyfikator (mamy int)
// rezygnujemy z adnotacji @RepositoryRestResource na rzecz @Repository, też zarządza tym Spring ale //możemy dać dodatkowe parametry, min:(path = "todos", collectionResourceRel = "todos") //nasze repozytorium działa na obiektach TASK, stąd w adresie URL Spring daje defaultowo Tasks. Tu zmieniamy odpowiednio. Mogę o tym przeczytać w dokuementacji (ctrl+b)
//tworzymy interfejs REST dający nam metody dostępu do baz danych (SPRING wie co dać)
//już jedna adnotacja (wyżej) i interfejs bazowy pozwala na wszystkie operacje typu CRUD w oparciu o stronicowanie i sortowanie
@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task, Integer> {

    @Override//metoda existsbyID jest nadpisywana TaskRepository, wykonuje się query natywne SQL.
    @Query(nativeQuery = true, value = "select count(*) > 0 from tasks where id=:id") //adnotacja springowa, pozwala korzystac z "czystego" SQL. Dwukropek ID - dzięki temu możemy używać @Param. Mając SELECT* moglibyśmy zwracać obiekt klasy Task!!!
    boolean existsById(@Param("id")Integer id);
} //dzięki dodaniu TaskRepository w extends Spring wie, że przywołując TaskRepository idziemy do Bean'a Springowego SQLTaskRepository













    /*

    @Override
    @RestResource(exported = false) //informacja że mogę tak zrobić jest w dokumentacji. Dzięki temu blokuję dostęp do danego zapytania (DELETE), nie mogę wysłać metody http DELETE.
    //adnotacja RestResource, ctrl + spacja pokazuje co mogę tam wpisać.
    void deleteById(Integer integer);
    // repozytoria Spring (SpringData) to DSL - domain specific language.
    // Domena to procesowanie kolekcji, pod spodem łączenie się z bazą danych. Wszystkie metody są tłumaczone na zapytania SQL (bo mamy JPA). Możemy stwożyć własne metody albo nadpisywać dostępne.
    @Override
    @RestResource(exported = false)
    void delete(Task entity);



    @RestResource(path = "done", rel = "done")//relacja, hipermedia, łącze do adresu //zmieniam adres, podobnie
    List<Task> findByDone(@Param("state")boolean done);//wersja Ultimate IntelliJ podpowiedziałaby mi jakie mam opcje i po czym mogę szukać, opcji jest dużo (włącznie z szukaniem w opisie). Można określać wartość flagi.
                                    // Ta metoda, dzięki @RepositoryRestResource jest już dostępna pod jakimś adresem.
                                    //dzięki @Param mogę po adresie przekazać wartość. Wysyłając medotę HTML na adres http://localhost:8080/tasks/search/done?state=true dostaję w odpowiedzi taski z wartością done true

    //tag "_embedded w odpowiedzi JSON oznacza obiekty
    // _links to pomocnicze nakierowania co z kolekcją możemy zrobić. To wszystko bierze się z RestRepository Springa.
    //HATEOAS - stan aplikacji reprezentowany przez hipermedia (metadane). Właśnie _embedded, _links.
    // Lista tasków jest zwracan aobudowana w obiekt _embedded. To rozszerzony REST.
    */
