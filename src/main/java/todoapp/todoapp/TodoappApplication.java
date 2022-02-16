package todoapp.todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import javax.validation.Validator; //wcześniej był springowy

@SpringBootApplication //klasa konfiguracyjna. Możemy dokleić inne, np. @Import(TaskConfigurationProperties.class). Nie jest to potrzebne, bo Sprign biega po pakiecie i wie, że to musi dołączyć.
//możliwość dołączania ręcznie konfiguracji, np przez:
//@ComponentScan(basePackages = "db.migration"). SKanowanie Beanów, skanowanie encji, adnotacji;
public class TodoappApplication /*implements RepositoryRestConfigurer*/ {

	public static void main(String[] args) {
		SpringApplication.run(TodoappApplication.class, args);


	}

	@Bean //obiekt zwrócony z tej metody typu Validator będzie
		// zarządzany przez Springa. Dzięki temu rejestrujemy klasę do
		// Springa z biblioteki, której normalnie w nim nie ma
		// adnotacja @Bean to zależność, to, co Spring zaczyta do kontekstu i będzie traktował jako Singleton. Rejestrowanie węzła przy pomocy metody. Przepis na stworzenie tego, co Spring ma zarejestrować.
	Validator validator() {
		return new LocalValidatorFactoryBean();
	}

	/*@Override
	public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
		validatingListener.addValidator("beforeCreate", validator());
		validatingListener.addValidator("beforeSave", validator());
	}*/


}
