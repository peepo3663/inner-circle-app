package bu.edu.met673.api.innercircle.controller;

import bu.edu.met673.api.innercircle.model.Message;
import bu.edu.met673.api.innercircle.model.User;
import bu.edu.met673.api.innercircle.model.UserDevice;
import bu.edu.met673.api.innercircle.service.FCMService;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firebase.cloud.FirestoreClient;
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

  private final String messageNodeName = "messages";
  private final CollectionReference chatsRef = FirestoreClient.getFirestore().collection("chats");
  private final CollectionReference usersRef = FirestoreClient.getFirestore().collection("users");

  private FCMService fcmService;

  public MessageController(FCMService fcmService) {
    this.fcmService = fcmService;
  }

  @PostMapping("/send")
  public Message createAMessageNode(@RequestBody Message message)
      throws ExecutionException, InterruptedException {
    String chatRoomId = message.getChatRoomId();
    if (chatRoomId == null) {
      throw new NullPointerException();
    }
    ApiFuture<DocumentReference> result = chatsRef.document(chatRoomId).collection(messageNodeName).add(message.toData());
//    ApiFutures.addCallback(result, new ApiFutureCallback<DocumentReference>() {
//      @Override
//      public void onFailure(Throwable throwable) {
//        logger.error("Can not write a message.", throwable);
//      }
//
//      @Override
//      public void onSuccess(DocumentReference documentReference) {
//        message.setMessageId(documentReference.getId());
//        sendNotifications(message);
//      }
//    }, MoreExecutors.directExecutor());
    DocumentReference documentReference = result.get();
    if (documentReference != null) {
      message.setMessageId(documentReference.getId());
      sendNotifications(message);
      return message;
    } else {
      throw new NullPointerException();
    }
  }

  private void sendNotifications(Message message) {
    // query all device token in the chat room
    List<String> deviceTokens = new ArrayList<>();
    try {
      DocumentSnapshot chatSnapshot = chatsRef.document(message.getChatRoomId()).get().get();
      List<String> users = (List<String>) chatSnapshot.get("users");
      if (users == null) {
        return;
      }
      users.remove(message.getUserId());
      for (String userId: users) {
        Map<String, Object> userNode = usersRef.document(userId).get().get().getData();
        if (userNode == null) {
          continue;
        }
        User user = new User(userNode);
        List<String> userDevices = getUserDeviceTokens(user);
        deviceTokens.addAll(userDevices);
      }
      this.fcmService.sendMessage(null, message.getUserName(), message.getText());
    } catch (ExecutionException | InterruptedException e) {
      logger.error("Something wrong with notification feature.", e);
    }
  }

  private List<String> getUserDeviceTokens(User user) {
    List<String> deviceTokens = new ArrayList<>();
    int deviceTokensSize = user.getDevices().size();
    for (int i = 0; i < deviceTokensSize; i++) {
      UserDevice userDevice = user.getDevices().get(i);
      deviceTokens.add(userDevice.getDeviceToken());
    }
    return deviceTokens;
  }
}
