package todoapp.todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import javax.validation.Validator; //wcześniej był springowy

@SpringBootApplication
public class TodoappApplication /*implements RepositoryRestConfigurer*/ {

	public static void main(String[] args) {
		SpringApplication.run(TodoappApplication.class, args);


	}

	@Bean //obiekt zwrócony z tej metody typu Validator będzie
		// zarządzany przez Springa. Dzięki temu rejestrujemy klasę do
		// Springa z biblioteki, której normalnie w nim nie ma
	Validator validator() {
		return new LocalValidatorFactoryBean();
	}

	/*@Override
	public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
		validatingListener.addValidator("beforeCreate", validator());
		validatingListener.addValidator("beforeSave", validator());
	}*/



}
