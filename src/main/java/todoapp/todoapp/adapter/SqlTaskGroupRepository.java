package todoapp.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import todoapp.todoapp.model.TaskGroup;
import todoapp.todoapp.model.TaskGroupRepository;

import java.util.List;

//adapter, dostosowany do naszych potrzeb styl zaimplementowania repository przy pomocy Springa
@Repository
public interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {

    @Override
    @Query("select distinct g from TaskGroup g join fetch g.tasks") //na odmianę nie Native. Bliżej składniowo do Javy. HPQ/JPQL czyli Hibernate Persistance Querry Language/Java Persistance Querry Language
    // to samo co np. SELECT * FROM Task_Group JOIN tasks ON foo [...]. Hibernate patrzy po nazwie @Entity (domyślnie jak nazwa klasy, można nadpisać przez @Entity(name = "FOO")).
    //to rozwiązuje problem n+1 selectów, ignorujemy lazyloading. join fetch - domyslnie jest to inner join. Jeżeli grupa nie ma żadnych tasków to się nie zwróci. Select jest opcjonalny
    List<TaskGroup> findAll();

    @Override
    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);
}
