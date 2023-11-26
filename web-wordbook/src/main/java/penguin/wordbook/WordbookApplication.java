package penguin.wordbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		scanBasePackages = {"penguin.wordbook","penguin.auth"}
)
public class WordbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(WordbookApplication.class, args);
	}

}
