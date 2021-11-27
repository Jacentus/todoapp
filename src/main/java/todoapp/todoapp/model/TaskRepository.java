package todoapp.todoapp.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

//nasze repozytorium. Będą tu wyłącznie te metody dostępu do repozytorium Tasków, jakie chcemy udostępnić (w przeciwieństwie do korzystania z JPArestrepository,
// która domyślnie ma ich dużo więcej. Interfejs jest publiczny, w przeciwieństwie do interfejsu SqlTaskRepository który ma dostęp package private (ENKAPSULACJA)
//i który ma publiczne metody z interfejsów dostępnych przez biblioteki (JPArepository)
// IMPLEMENTACJA - SqlTaskRepository. KONTRAKT - TaskRepository
public interface TaskRepository{
    List<Task> findAll();
    Page<Task> findAll(Pageable page);
    List<Task>findByDone(@Param("state")boolean done);
    Optional<Task> findById(Integer id);
    Task save(Task entity);
    boolean existsById(Integer id);
    Task getById(Integer id);
}
