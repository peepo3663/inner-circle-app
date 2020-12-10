package bu.edu.met673.api.innercircle.controller;

import bu.edu.met673.api.innercircle.model.User;
import bu.edu.met673.api.innercircle.model.UserDevice;
import bu.edu.met673.api.innercircle.util.FirestoreUtil;
import com.google.cloud.storage.Blob;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
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
    Map<String, String> response = new HashMap<>();
    if (userId == null || userId.isEmpty()) {
      return null;
    }
    String resultURL = null;
    FirestoreUtil firestoreUtil = FirestoreUtil.getInstance();
    try {
      Blob resultFromUpload = firestoreUtil.uploadPictureProfileFor(userId, file);
      resultURL = resultFromUpload.signUrl(365, TimeUnit.DAYS).toString();
      firestoreUtil.updateProfilePicture(userId, resultURL);
    } catch (IOException | ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
    if (resultURL == null) {
      throw new NullPointerException();
    }
    response.put("profile_picture", resultURL);
    return response;
  }

  @PostMapping("update/{userId}/deviceToken")
  public Map<String, String> updateDeviceToken(@PathVariable("userId") String userId, @RequestBody
                                               UserDevice userDevice) {
    Map<String, String> response = new HashMap<>();
    try {
      FirestoreUtil.getInstance().updateUserDeviceToken(userId, userDevice);
      response.put("message", "add device successfully.");
    } catch (Exception e) {
      response.put("errorMsg", "can't this add device");
    }
    return response;
  }
}
