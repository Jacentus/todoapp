package todoapp.todoapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table (name = "tasks_groups")
public class TaskGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Task group's description must not be empty")
    private String description;
    private boolean done;
/*    @Embedded
    private Audit audit = new Audit();*/
    //@OneToMany(fetch = FetchType.LAZY) //informacja że jeden TaskGroup idzie do wielu Tasków.
    // Hibernate ma własne implementacje kolekcji (lista itp)! Fetch pozwala określić kiedy zaciągamy listę. Lista z Hibernate nie jest posortowana

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group") //gdy usuwamy/dodajemy grupę to dzieje się to ze wszystkimi Taskami w grupie
    //@JoinColumn(name = "task_group_id")
    private Set<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public TaskGroup() {
    }

    /////////////////// auto generated getters and setters ////////////////
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Set<Task> getTasks() { //publiczny getter do seta z Taskami z danej grupy. Dochodzi do LazyLoadingu (fetchtype.Lazy, czyli dopiero gdy potrzebujemy tasków
        // to hibernate generuje zapytanie SQL)
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
