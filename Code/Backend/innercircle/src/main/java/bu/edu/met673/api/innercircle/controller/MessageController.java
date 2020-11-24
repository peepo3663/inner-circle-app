package bu.edu.met673.api.innercircle.controller;

import bu.edu.met673.api.innercircle.model.Message;
import bu.edu.met673.api.innercircle.model.User;
import bu.edu.met673.api.innercircle.model.UserDevice;
import bu.edu.met673.api.innercircle.service.FCMService;
import bu.edu.met673.api.innercircle.util.FirestoreUtil;
import com.google.cloud.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {
  private static Logger logger = Logger.getLogger(MessageController.class);
  private FCMService fcmService;

  public MessageController(FCMService fcmService) {
    this.fcmService = fcmService;
  }

  @PostMapping("/send")
  public Message createAMessageNode(@RequestBody Message message)
      throws ExecutionException, InterruptedException {
    Message result = FirestoreUtil.getInstance().createAMessageNode(message);
    sendNotifications(result);
    return result;
  }

  private void sendNotifications(Message message) {
    // query all device token in the chat room
    try {
      List<String> deviceTokens = FirestoreUtil.getInstance().queryForUserDeviceTokens(message);
      this.fcmService.sendMessage(deviceTokens, message.getUserName(), message.getText());
    } catch (ExecutionException | InterruptedException e) {
      logger.error("Something wrong with notification feature.", e);
    }
  }
}
