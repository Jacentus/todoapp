package todoapp.todoapp.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import todoapp.todoapp.TaskConfigurationProperties;
import todoapp.todoapp.model.*;
import todoapp.todoapp.model.projection.GroupReadModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock; //import static - czemu?
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test //test jednostkowy - test wydzielonej jednostki, w tym wypadku metody, w jednym przypadku.
    //można pisać długie nazwy, bo mają nam coś powidzieć
    @DisplayName("should throw IllegalStateException when configured to allow juest 1 group and the other undone group exists")
        //informacja jaka pójdzie do raportu
    void createGroup_noMultipleGroupsConfig_And_openGroupExists_throwsIllegalStateException() {
        //given
        var mockGroupRepository = mock(TaskGroupRepository.class); //metoda udostępniona przez bibliotekę Mockito. Mockito zaczytuje tę klasę i przez mechanizm reflekcji w javie nadpisuje metody,
        // które się tam znajdują.
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true); //ANYiNT = DOWOLNT INTEGER
        //jeżeli wywołamy mocka klasy GroupRepository i na nim funkcję jw., niezależnie od podanego parametru zwróć true;
        //and
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(false);
        //zawsze odpowiemy w ten sam sposób - true. Mamy grupy, które są nieskończone, i dla dowolnego ID zawsze istnieją. Funkcje z MOCK. Zawsze da true (anyInt).
        //and
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        // system under test
        var toTest = new ProjectService(null, mockGroupRepository, mockConfig); //nasza encja Project service dostaje mocki do konstruktora
        //when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        //then
        assertThat(exception).isInstanceOf(IllegalStateException.class).hasMessageContaining("one undone group");
        //then
        //assertTrue(mockGroupRepository.existsByDoneIsFalseAndProject_Id(500)); //asercja z JUnit
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no project for a given ID")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupExists_And_noProjects_throwsIllegalArgumentException() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty()); //Optional?
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true); //wyjęliśmy funkcję na zewnątrz
        // and
        // system under test
        var toTest = new ProjectService(mockRepository, null, mockConfig);
        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("ID not found");
            }

    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just 1 group and no groups and no projects for a given ID")
    void createGroup_configurationOK_And_noProjects_throwsIllegalArgumentException() {
        //given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty()); //Optional?
        // and
       TaskGroupRepository mockGroupRepository = groupRepositoryReturning(false);
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true); //wyjęliśmy funkcję na zewnątrz
        // system under test
        var toTest = new ProjectService(mockRepository, mockGroupRepository, mockConfig);
        // when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        // then
        assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("ID not found");
    }

    @Test
    @DisplayName("should create a new group from project")
    void createGroup_configurationOK_existingProject_createsAndSavesNewGroup() {
        //given
        var today = LocalDate.now().atStartOfDay(); //data dzisiejsza z zerowym czasem
        //and
        var project = projectWith("bar", Set.of(-1,-2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(project)); //zwracamy projekt z dwoma krokami
        // and
        TaskConfigurationProperties mockConfig = configurationReturning(true);
        //and
        InMemoryGroupRepository inMemoryGroupRepository = inMemoryGroupRepository(); //metoda niżej, nadpisuje repozytorium na potrzeby testu. Faktycznie symuluje użycie aplikacji przez użytkownika
        int countBeforeCall = inMemoryGroupRepository.count();
        //system under test
        var toTest = new ProjectService(mockRepository, inMemoryGroupRepository, mockConfig);//nasz obiekt

        // when
        GroupReadModel result = toTest.createGroup(today, 1);
        // then assert that result
        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task->task.getDescrption().equals("foo"));
        assertThat(countBeforeCall + 1)
                .isEqualTo(inMemoryGroupRepository.count());
    }

    //wyciągamy metody których używamy więcej niż raz i metody pomocnicze (zwiększają czytelność testów)

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline){

        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        Set<ProjectStep> steps = daysToDeadline.stream()
                        .map(days -> {
                            var step = mock(ProjectStep.class);
                            when(step.getDescription()).thenReturn("foo");
                            when(step.getDaysToDeadline()).thenReturn(days);
                            return step;
                })
                        .collect(Collectors.toSet());
        when(result.getSteps()).thenReturn(steps);
        return result; //w mock repository będziemy robić optional of.
        //NIE MOZE BYĆ MOCKA W MOCKU STĄD DEKLARUJEMY sET STEPÓW
    }


    private InMemoryGroupRepository inMemoryGroupRepository() {
        return new InMemoryGroupRepository();
    } //zmuszamy metody żeby współpracowały ze sobą. De facto nadpisujemy repozytorium na potrzeby testu.


    private static class InMemoryGroupRepository implements TaskGroupRepository {
        private int index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count(){
            return map.values().size();
        }

        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(final Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) {
            if (entity.getId() == 0) {
                try {
                    var field = TaskGroup.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++index); //ustawianie przez refleksję (gdy setter jest prywatny) nie zadziałało.
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }
    }

            private TaskConfigurationProperties configurationReturning(final boolean result) {
                var mockTemplate = mock(TaskConfigurationProperties.Template.class);
                when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
                var mockConfig = mock(TaskConfigurationProperties.class);
                when(mockConfig.getTemplate()).thenReturn(mockTemplate);
                return mockConfig;
            }

            private TaskGroupRepository groupRepositoryReturning(final boolean result) {
                var mockGroupRepository = mock(TaskGroupRepository.class);
                when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
                return mockGroupRepository;
            }
        }


        /*
        when + then
        assertThatIllegalStateException().isThrownBy(()->toTest.createGroup(LocalDateTime.now(), 0)); //fluent assertion!

        oraz jak wyżej

        assertThatExceptionOfType(IllegalStateException.class).
                isThrownBy(()->toTest.createGroup(LocalDateTime.now(), 0));

        możliwe również jak wyżej, również metoda z AssertJ

        assertThatThrownBy(()->toTest.createGroup(LocalDateTime.now(), 0)).isInstanceOf(IllegalStateException.class);


        metoda powyżej dostępna przez AssertJ zastępuje to poniżej. Sama w sobie ma blok try + catch.

        try {
            toTest.createGroup(LocalDateTime.now(), 0);
        } catch (IllegalStateException e){
            //then
            assertThat(e).isEqualTo("Only one undone group from project is allowed");    //assertJ - fluent assertions. Przekazujemy obiekt, na którym sprawdzamy różne rzeczy.
        }

        //testy wato podzielić na części:
        // given - miejsce gdzie przygotowujemy i przekazujemy dane do testu by osiągnąć dające się wytestować, warunki do testu
        //aby użyć TaskGroupRepository, które jest interfejsem, musimy nadpisac nasze metody w teście. Dzięki temu że mamy repozytorium kontraktowe (nasze "lekkie") nie musimy nadpisywać wszystkich metod z JPArepository
        //takie repozytorium zaciemniałoby obraz. Mamy na to narzędzie - MOCKITO. Zautomatyzowany sposób tworzenia klas i interfejsów
        var mockGroupRepository = new TaskGroupRepository(){

            @Override
            public List<TaskGroup> findAll() {
                return null;
            }

            @Override
            public Optional<TaskGroup> findById(Integer id) {
                return Optional.empty();
            }

            @Override
            public TaskGroup save(TaskGroup entity) {
                return null;
            }

            @Override
            public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
                return false;
            }

        }; //mock czyli wydmuszka, repozytorium które działa jak chcę. Zakładam, że ono jest już wytestowane. Mnie intresuje ProjectService


        //when - gdzie wołamy metodę
        // then - sprawdzamy czy przetestowana metoda zaskutkowała tym co chcemy

         */