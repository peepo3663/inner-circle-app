package bu.edu.met673.api.innercircle.exception;

public class ChatRoomAlreadyExistedException extends Exception {
  public ChatRoomAlreadyExistedException() {
    super("This chat room is already existed.");
  }
}
