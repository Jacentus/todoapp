package todoapp.todoapp.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//klasa do komunikacji z bazą danych. Coś ala API, punkt wejścia do działania na kolekcji (w tym przypadku tasków)
//interfejs dziedziczący po interfejsie z pakietu
// JpaRepository. Przekazujemy info z jakiej encji
// powinno brać dane oraz jaki jest jej identyfikator (mamy int)

@RepositoryRestResource
    //tworzymy interfejs REST dający nam metody dostępu do baz danych (SPRING wie co dać)
    //już jedna adnotacja (wyżej) i interfejs bazowy pozwala na wszystkie operacje typu CRUD w oparciu o stronicowanie i sortowanie
interface TaskRepository extends JpaRepository<Task, Integer> {




}
