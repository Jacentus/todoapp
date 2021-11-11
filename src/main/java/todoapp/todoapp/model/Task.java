package todoapp.todoapp.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity //adnotacja z pakietu java persistance API - pod spodem
// będzie tabela w bazie danych która odpowiada tej klasie.
// Nie muszę dawać opisów do pól danej klasy, bo Hibernate już nada nazwy dla prywatnych pól nazwy kolumn
@Table (name = "tasks") //adnotacja pomocnicza, nadałem nazwę tabeli
public class Task {
    @Id //wymagane - dzięki temu wiemy które pole to ID dla tabeli
    @GeneratedValue(strategy = GenerationType.IDENTITY) //adnotacja nadająca numery ID zgodnie ze strategią (jest ich więcej niż 1, do wyboru, można zajrzeć w kod)
    private int id;
    //@Column(name = "desc") mogę tez nadać sam nazwę kolumny w tabeli (nadpisuje adnotację @Entity

    @NotBlank(message = "Task's description cannot be null")
    private String description;

    private boolean done;


    public Task() {
    }

    /////////////////// auto generated getters and setters //////////////////////////////
    //@Column(name = "id") - mogę też tak, ale UWAGA,
    // trzymać się jednej koncepcji (mapping na polu/geterze) bo Hibernate nadpisuje WSZYSTKIE ostatie na da dole instrukcje
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
}
