package todoapp.todoapp.model.projection;

import todoapp.todoapp.model.Task;

//podzbiory tasków pozwalające na wykorzystanie tasków w konkretnych przypadkach. DTO!
public class GroupTaskReadModel {
    private boolean done;
    private String descrption;

    //konstruktor dostaje właściwego taska z którego tworzy taskowe DTO
    public GroupTaskReadModel(Task source) {
    descrption = source.getDescription();
    done = source.isDone();
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }
}
