package bu.edu.met673.api.innercircle.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatRoom {
  private String chatRoomId;

  @JsonProperty("users")
  private User[] users;

  public ChatRoom() { }

  public ChatRoom(User[] users) {
    this.users = users;
  }

  public String getChatRoomId() {
    return chatRoomId;
  }

  public void setChatRoomId(String chatRoomId) {
    this.chatRoomId = chatRoomId;
  }

  @JsonProperty("users")
  public User[] getAllUsers() {
    return users;
  }
}
