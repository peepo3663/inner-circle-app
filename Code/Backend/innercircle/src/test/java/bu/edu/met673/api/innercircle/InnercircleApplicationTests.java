package bu.edu.met673.api.innercircle;

import bu.edu.met673.api.innercircle.controller.UserController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class InnercircleApplicationTests {

	@Autowired
	private UserController userController;

	@Test
	void contextLoads() {
		Assertions.assertThat(userController).isNotNull();
	}

}
