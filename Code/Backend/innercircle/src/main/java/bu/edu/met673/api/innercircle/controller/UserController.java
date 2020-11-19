package bu.edu.met673.api.innercircle.controller;

import bu.edu.met673.api.innercircle.model.User;
import bu.edu.met673.api.innercircle.util.FirestoreUtil;
import java.util.Map;
import javax.servlet.annotation.MultipartConfig;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UserController {

  private static Logger logger = Logger.getLogger(UserController.class);

  @PostMapping("/create")
  public User createUsers(@RequestBody User user) throws Exception {
    return FirestoreUtil.getInstance().queryForUser(user);
  }

  @PostMapping("update/profile")
  public Map<String, String> updatePictureProfile(@RequestParam("file") MultipartFile file, @RequestBody User user) {

  }
}
