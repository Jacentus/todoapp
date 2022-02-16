package todoapp.todoapp.model;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository { //nasz kontrakt odwołujący się do JPA
     List<Project> findAll();

     Optional<Project> findById(Integer id);

     Project save (Project entity);


}
