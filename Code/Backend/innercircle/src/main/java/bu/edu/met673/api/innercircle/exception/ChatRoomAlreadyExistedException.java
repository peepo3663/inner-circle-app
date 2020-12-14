package bu.edu.met673.api.innercircle.exception;

public class ChatRoomAlreadyExistedException extends Exception {

  private String chatRoomId;

  public ChatRoomAlreadyExistedException(String chatRoomId) {
    super("This chat room is already existed.");
    this.chatRoomId = chatRoomId;
  }

  public String getChatRoomId() {
    return chatRoomId;
  }
}
