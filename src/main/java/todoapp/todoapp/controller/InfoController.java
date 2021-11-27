package todoapp.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import todoapp.todoapp.TaskConfigurationProperties;

@RestController
public class InfoController {

    //@Autowired zmieniam adnotację na wstrzykiwanie przez konstruktor
    private DataSourceProperties dataSource; //wcześniej po prostu String url
    private TaskConfigurationProperties myProp;

    public InfoController(final DataSourceProperties dataSource, final TaskConfigurationProperties myProp) {
        this.dataSource = dataSource;
        this.myProp = myProp;
    }

    @GetMapping("/info/url")
    String url(){
        return dataSource.getUrl(); //dzięki temu mamy dostęp do metod z klasy Springa DataSourceProperties. Wstrzykiwanie zależności!
    }

    @GetMapping("/info/prop")
    boolean myProp() {
        return myProp.getTemplate().isAllowMultipleTasks();

    /*@Value("${task.allowMultipleTasksFromTemplate}")//@Value pozwala nam odwoływać się do application.properties (lub tutaj w formacie .yaml), w tym do swoich properties
        private boolean myProp;

        */

    }
 //tak wcześniej mogliśmy się dostać do swojego property lub url. Zastępujemy to @Autowired, @ConfigurationProperties

}
