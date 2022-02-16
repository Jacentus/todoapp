package todoapp.todoapp.logic;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import todoapp.todoapp.model.TaskGroup;
import todoapp.todoapp.model.TaskGroupRepository;
import todoapp.todoapp.model.TaskRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when any task in the grop contains undone tasks")
    void toggleGroup_undoneTasks_throwsIllegalStateException() {
    //given
    var mockTaskRepository = mock(TaskRepository.class);
   // var mockTaskGroupRepository = mock(TaskGroupRepository.class);
    when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id((anyInt()))).thenReturn(true);
    //service to test
    var toTest = new TaskGroupService(null, mockTaskRepository);
    //when
    var exception = catchThrowable(() -> toTest.toggleGroup(0)); //assertJ
    //then
    assertThat(exception).isInstanceOf(IllegalStateException.class).hasMessageContaining("has undone tasks");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when group with such id does not exist")
    void toggleGroup_wrongId_throwsIllegalArgumentException(){
        //given
        var mockTaskRepository = mock(TaskRepository.class);
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id((anyInt()))).thenReturn(false);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.empty()); //aby Optional zwracał false
        //service to test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);
        //when
        var exception = catchThrowable(() -> toTest.toggleGroup(0));
        //then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("given id not found");
    }

    @Test
    @DisplayName("should amend done field in given Group if no exceptions are thrown")
    void toggleGroup_worksAsExpected(){
        //given
        var mockTaskRepository = mock(TaskRepository.class);
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        var taskGroup = new TaskGroup();
        var beforeToggle = taskGroup.isDone();
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id((anyInt()))).thenReturn(false);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(taskGroup));
        //service to test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);
        //when
        toTest.toggleGroup(0);
        //then
        assertThat(taskGroup.isDone()).isEqualTo(!beforeToggle); //isNotEqualTo(taskGroup.isDone());
    }
}


/*
    public void toggleGroup(int groupId){
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)){
            throw new IllegalStateException("Group has undone tasks. Do all the tasks first");
        }
        TaskGroup result = repository.findById(groupId).orElseThrow(()-> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }
 */