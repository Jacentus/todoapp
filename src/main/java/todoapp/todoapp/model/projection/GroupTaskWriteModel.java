package todoapp.todoapp.model.projection;

import todoapp.todoapp.model.Task;

import java.time.LocalDateTime;
//DTO - data transfer object. Obiekt krótego używamy gdy chcemy przesyłać dane, nie udostępniamy więcej niż musimy
public class GroupTaskWriteModel {
    private String description;
    private LocalDateTime deadline;

    public Task toTask(){
        return new Task(description, deadline);
    }

    /////////////////////////////////// getters & setters ////////////////////////////////////////////////

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
}
