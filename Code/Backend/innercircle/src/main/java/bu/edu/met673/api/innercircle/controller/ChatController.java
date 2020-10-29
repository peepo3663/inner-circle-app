package bu.edu.met673.api.innercircle.controller;

import bu.edu.met673.api.innercircle.model.ChatRoom;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
public class ChatController {

  private CollectionReference chatsRef = FirestoreClient.getFirestore().collection("chats");

  @PostMapping("/create")
  public String createChatRoom(@RequestBody ChatRoom room) {
    // check that the two user already have chat room

    // add new chat room if need

    
    ApiFuture<DocumentReference> result = chatsRef.add({});
    return result.get();
  }
}
