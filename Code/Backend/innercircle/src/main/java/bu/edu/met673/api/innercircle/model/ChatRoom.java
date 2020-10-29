package bu.edu.met673.api.innercircle.model;

import java.util.List;

public class ChatRoom {
  private String chatRoomId;
  private List<User> allUsers;

  public ChatRoom() {

  }

  public ChatRoom(List<User> users) {
    this.allUsers = users;
  }
}
