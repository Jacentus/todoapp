package todoapp.todoapp.model;

import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

//@MappedSuperclass //adnotacja oznacza, że jest to klasa bazowa która będzie pozwalać zamodelować na bazie danych
// klasa bazowa która nie ma odzwierciedlenia w tabeli.
//3 strategie odzwierciedlenia hierarchi klas na bazie (bazy nie wspierają dziedziczenia):
// - 1 tabela z kolumna wskazującą czym jestem
// - 2 klucze obce + join do tabeli nadrzędnej (relacja 1 do 1)
// - 3 dwie osobne tabele bez wspólnych pól
//pojawiają się problemy gdy dziedziczy się z więcej niż innej klasy, stąd inna propozycja - @Embeddable.
// oznacza to że ta klasa jest osadzalna, do osadzenia, w innym miejscu (np. audit w tym przypadku)
@Embeddable
public class /*BaseAuditableEntity*/ Audit {
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    @PrePersist
        //Persist to operacja służąca do zapisu na bazie. PrePersist oznacza, że funkcja odpali się przed zapisem na bazie danych.
    void prePersist() {
        createdOn = LocalDateTime.now();
    }
    @PreUpdate
        //
    void preMerge(){
        updatedOn = LocalDateTime.now();
    }



}
