package bu.edu.met673.api.innercircle.controller;

import bu.edu.met673.api.innercircle.model.User;
import bu.edu.met673.api.innercircle.util.FirestoreUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
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

  @PostMapping("update/profile/{userId}")
  public Map<String, String> updatePictureProfile(@RequestParam("file") MultipartFile file, @PathVariable("userId") String userId) {
    // TODO: return new url json response.
    Map<String, String> response = new HashMap<>();
    if (userId == null || userId.isEmpty()) {
      return null;
    }
    String resultFromUpload = null;
    try {
      resultFromUpload = FirestoreUtil.getInstance().uploadPictureProfileFor(userId, file);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (resultFromUpload == null) {
      throw new NullPointerException();
    }
    response.put("profile_picture", resultFromUpload);
    return response;
  }
}
