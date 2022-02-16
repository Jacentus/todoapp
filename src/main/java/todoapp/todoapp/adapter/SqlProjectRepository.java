package todoapp.todoapp.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import todoapp.todoapp.model.Project;
import todoapp.todoapp.model.ProjectRepository;
import todoapp.todoapp.model.TaskGroup;
import todoapp.todoapp.model.TaskGroupRepository;

import java.util.List;

//adapter, dostosowany do naszych potrzeb styl zaimplementowania repository przy pomocy Springa
@Repository
public interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {

    @Override
    @Query("select distinct p from Project p join fetch p.steps")
    List<Project> findAll();

}
