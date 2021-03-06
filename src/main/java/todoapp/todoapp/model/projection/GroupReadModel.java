package todoapp.todoapp.model.projection;

import todoapp.todoapp.model.Task;
import todoapp.todoapp.model.TaskGroup;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupReadModel {
    private String description;
    /**
     * Deadline from the latest task in group.
     */
    private LocalDateTime deadline;
    private Set<GroupTaskReadModel> tasks;

    public GroupReadModel(TaskGroup source) {
        description = source.getDescription();
        source.getTasks().stream().map(Task::getDeadline)
                .max(LocalDateTime::compareTo).ifPresent(date -> deadline = date);
               //comparing - comparator który wyciąga dane z wnętrza obiektu
        tasks = source.getTasks().stream()
                        .map(GroupTaskReadModel::new)
                        .collect(Collectors.toSet());
    }

    /////////////////////////////////////// getters and setters ////////////////////////////////////////////
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

    public Set<GroupTaskReadModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<GroupTaskReadModel> tasks) {
        this.tasks = tasks;
    }
}
