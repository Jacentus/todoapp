package todoapp.todoapp;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Configuration;

//profile Springowe!! w zależności od profilu możemy ustawić inne wartości z Properties. Utworzyłem drugi plik z properties. Pierwsza linijka spring.profiles.active=local (juz nie yaml)
// VM Options w konfiguracji aplikakacj: -Dspring.profiles.active=FOO nadpisuje aktywny profil. Jeśli odpalam progam z cmd to: set spring_profiles_active=prod && java -jar todoapp-0.0.1-SNAPSHOT.jar (muszę mieć zbudowaną apkę pod tą ścieżką)
// oprócz tego mogę używać serwera konfiguracyjnego. Do tego służy plik bootstrap.yml/bootstrap.properties, który mogę dodać w resources. Odpala się pierwszy, ewentualnie waliduje mnie na serwerze (po kluczu) i jeśli OK, wysyła mi konfigurację.

@Configuration
@ConfigurationProperties("task") //dwie adnotacje to zaszłość, można zajrzeć do dokumentacji zobaczyć jak to jest poprawione
public class TaskConfigurationProperties {

    private Template template;

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public static class Template{ //nested class, dzięki temu możemy ustawiać hierarchię w ApplicationProperties.yaml

        boolean allowMultipleTasks;

        /////////////////////////////////// nested class getters and setters /////////////////////////////////////////////////

        public boolean isAllowMultipleTasks() {
            return allowMultipleTasks;
        }

        public void setAllowMultipleTasks(boolean allowMultipleTasks) {
            this.allowMultipleTasks = allowMultipleTasks;
        }
    }
}
