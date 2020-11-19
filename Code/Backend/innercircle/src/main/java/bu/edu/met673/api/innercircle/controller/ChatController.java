package bu.edu.met673.api.innercircle.controller;

import bu.edu.met673.api.innercircle.exception.ChatRoomAlreadyExistedException;
import bu.edu.met673.api.innercircle.model.ChatRoom;
import bu.edu.met673.api.innercircle.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
public class ChatController {

  private CollectionReference chatsRef = FirestoreClient.getFirestore().collection("chats");

  @PostMapping(value = "/create")
  public ChatRoom createChatRoom(@RequestBody ChatRoom room)
      throws ExecutionException, InterruptedException, ChatRoomAlreadyExistedException {
    // check that the two user already have chat room
    User[] users = room.getAllUsers();
    if (users.length == 0) {
      throw new NullPointerException();
    }
    Query chatQuery = null;
    ArrayList<Map<String, Object>> usersForChatRoom = new ArrayList<>();
    ArrayList<String> userIds = new ArrayList<>();
    for (User user : users) {
      chatQuery = chatsRef.whereArrayContains("users", user.getUid());
      usersForChatRoom.add(user.toMapDataForChatRoom());
      userIds.add(user.getUid());
    }
    ApiFuture<QuerySnapshot> result = chatQuery.get();
    if (!result.get().isEmpty()) {
      throw new ChatRoomAlreadyExistedException();
    }
    // add new chat room if need
    DocumentReference chatDocument = chatsRef.document();
    Map<String, Object> chatRoom = new HashMap<>();
    chatRoom.put("createdAt", Timestamp.now());
    chatRoom.put("userIds", userIds);
    chatRoom.put("users", usersForChatRoom);
    chatRoom.put("users_count", userIds.size());

    ApiFuture<WriteResult> writeResult = chatDocument.set(chatRoom);
    if (writeResult.get() != null) {
      room.setChatRoomId(chatDocument.getId());
    }
    return room;
  }
}
