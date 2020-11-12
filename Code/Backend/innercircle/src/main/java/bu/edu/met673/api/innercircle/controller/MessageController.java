package bu.edu.met673.api.innercircle.controller;

import bu.edu.met673.api.innercircle.model.Message;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.firebase.cloud.FirestoreClient;
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

  @PostMapping("/send")
  public Message createAMessageNode(@RequestBody Message message)
      throws ExecutionException, InterruptedException {
    String chatRoomId = message.getChatRoomId();
    if (chatRoomId == null) {
      throw new NullPointerException();
    }
    ApiFuture<DocumentReference> result = chatsRef.document(chatRoomId).collection(messageNodeName).add(message);
    if (result.isDone()) {
      DocumentReference messageDocumentRef = result.get();
      message.setMessageId(messageDocumentRef.getId());
      return message;
    } else {
      throw new NullPointerException();
    }
  }
}
