package todoapp.todoapp.model;

import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

//adnotacja z pakietu java persistance API - pod spodem
                // będzie tabela w bazie danych która odpowiada tej klasie.
                // Nie muszę dawać opisów do pól danej klasy, bo Hibernate już nada nazwy dla prywatnych pól nazwy kolumn
         //@Column(name = "desc") mogę tez nadać sam nazwę kolumny w tabeli (nadpisuje adnotację @Entity)
@Entity
//@Inheritance(InheritanceType.class) - ta adnotacja pozwala na określenie dziedziczenia i odzwierciedlenia w bazie zgodnie z trzema mozłiwymi strategiami
@Table (name = "tasks") //adnotacja pomocnicza, nadałem nazwę tabeli
public class Task /*extends BaseAuditableEntity*/{
    @Id //wymagane - dzięki temu wiemy które pole to ID dla tabeli
    @GeneratedValue(strategy = GenerationType.IDENTITY) //adnotacja nadająca numery ID zgodnie ze strategią (jest ich więcej niż 1, do wyboru, można zajrzeć w kod)
    private int id;
    @NotBlank(message = "Task's description must not be empty")
    private String description;
    private boolean done;
    private LocalDateTime deadline;//możliwość definicji kolumny przez @Column(). Jej brak oznacza, że wszystkie pola tworzą kolumny.
    //pola techniczne, dla zbierania danych o aktualizacji i dodawaniu danych
   //@Transient //oznacza, że pole nie będzie zapisywane do bazy danych. Nie będzie miało odzwierciedlenia w kolumnie
/*    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;*/ //przenosimy to do BaseAuditableEntity

    @Embedded //dzięki temu możemy osadzić w klasie obiekt, który ma adnotację @Embeddable. Możemy w ten sposób np. robić ID składające się z 3 pól jako klucz główny
    /*@AttributeOverrides({
    @AttributeOveride(column = @Column("name = "updatedOn", name = "updatedOn")); //każde pole możemy zidentyfikować i nadpisać
     })*/
    private Audit audit = new Audit(); //dzięki temu stworzy się przy powstaniu encji taska
    @ManyToOne// cascade pozwala na zarządzanie (cascade = CascadeType.REFRESH) //adnootacja z JPA informuje że wiele tasków moze trafić do jednej grupy (many to one z bazy danych)
    @JoinColumn(name = "task_group_id") //joinujemy po tej kolumnie
    private TaskGroup group; //wskazujemy na encję grupy


    //////////////////////////////////
    Task() {
    } //konstruktor potrzebny hibernate'owi.

    public Task(String description, LocalDateTime deadline){
        this.description = description;
        this.deadline = deadline;
    }

    public void updateFrom(final Task source){
        description = source.description;
        done = source.done;
        deadline = source.deadline; //PreUpdate i PrePersist zadziałają dzięki Hibernate'owi.
        group = source.group;
    }


    /////////////////// auto generated getters and setters ////////////////
    public int getId() { //(mapping na polu/geterze) bo Hibernate nadpisuje WSZYSTKIE ostatie na da dole instrukcje
        return id;
    }
                    //@Column(name = "id") - mogę też tak, ale UWAGA, trzymać się jednej koncepcji

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

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public TaskGroup getGroup() {
        return group;
    }

    public void setGroup(TaskGroup group) {
        this.group = group;
    }
}
