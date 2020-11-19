package bu.edu.met673.api.innercircle.controller;

import bu.edu.met673.api.innercircle.exception.ChatRoomAlreadyExistedException;
import bu.edu.met673.api.innercircle.model.ChatRoom;
import bu.edu.met673.api.innercircle.util.FirestoreUtil;
import java.util.concurrent.ExecutionException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
public class ChatController {

  @PostMapping(value = "/create")
  public ChatRoom createChatRoom(@RequestBody ChatRoom room)
      throws InterruptedException, ExecutionException, ChatRoomAlreadyExistedException {
    return FirestoreUtil.getInstance().createChatRoom(room);
  }
}
