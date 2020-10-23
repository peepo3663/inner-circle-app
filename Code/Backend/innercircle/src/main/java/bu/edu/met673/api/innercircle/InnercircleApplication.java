package bu.edu.met673.api.innercircle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class InnercircleApplication {
	public static void main(String[] args) {
		SpringApplication.run(InnercircleApplication.class, args);
	}
}
