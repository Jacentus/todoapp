package todoapp.todoapp.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import todoapp.todoapp.TaskConfigurationProperties;
import todoapp.todoapp.model.Task;
import todoapp.todoapp.model.TaskGroup;
import todoapp.todoapp.model.TaskGroupRepository;
import todoapp.todoapp.model.TaskRepository;
import todoapp.todoapp.model.projection.GroupReadModel;
import todoapp.todoapp.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

//serwis - warstwa pośrednia między repozytorium a kontrolerem!
//serwis aplikacyjny, blisko user story. Serwis pośredniczy między dwoma repozytoriami, daje nam elastyczność. Pozwala obudowywać wyjątki, przetwarzać dane.
//gdybyśmy pchali to wszystko do kontrolera, to nie wiedzielibyśmy co jest logiką biznesową, a co odpowiedziami http i ich obsługą.
// w kontrolerze HATEOAS, w serwisie - procesowanie na klasach dostosowanych do DTO (readmodel, writemodel lub wprost do encji (POJO))
// każda klasa w springu która nie jest repozytorium albo modelem danych powinna być serwisem, jeśłi chcemy aby była bean'em.

//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) //zakres, w jakim dany serwis może obowiązywać. Jak ten obiekt powinien być wstrzykiwany (default: Singleton).

//websocket - przedłużony request
@Service
@RequestScope //proxy mode. Informacja dla Springa kiedy stworzyć obiekt.
public class TaskGroupService {

    private TaskGroupRepository repository; // główne repozytorium
    private TaskRepository taskRepository; // bardziej złożone repozytorium. Jest nam potrzebne zeby dowiedzieć się o stanie Tasków z danej grupy

    TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository){
        this.repository = repository;//wstrzyknięcie przez konstruktor. Spring wie, co z tym zrobić
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source){
        TaskGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll(){
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }
    //wymaganie biznesowe - nie możemy zamknąć grupy (zaznaczyć jako done) jesli chociaż jeden task nie jest done.
    public void toggleGroup(int groupId){
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)){
            throw new IllegalStateException("Group has undone tasks. Do all the tasks first");
        }
        TaskGroup result = repository.findById(groupId).orElseThrow(()-> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }

























    /*
    @Autowired
    List<String> temp(TaskGroupRepository repository){
        // FIXME: n + 1 selects
        // pobieramy wszystkie grupy, taski i ich opisy. Strzelamy N razy po wszystkie grupy Tasków, dostajemy N Grup. Dla każdej z grupy robimy Select * from TASK where ID = X
        // strzelimy N razy po wszystkie taski i dodatkowo jeszcze raz. Iterowanie po geterach za kolekcje LAZY jest problematyczne.
        // poprawę tego modelujemy w naszym adapterze SqlTaskGroupRepository!
        return repository.findAll().stream()
                .flatMap(taskGroup -> taskGroup.getTasks().stream())
                        .map(Task::getDescription)
                        .collect(Collectors.toList());
    } //wyciągamy wszystkie taski, następnie ich opisy, zbieramy to w kolekcji typu List
*/

}
