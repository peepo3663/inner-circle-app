package bu.edu.met673.api.innercircle.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.Timestamp;

public class Message {
  private String chatRoomId;
  private String messageId;
  private String text;
  private String userId;
  private String userName;

  @JsonProperty("profile_picture")
  private String profilePicture;
  private Timestamp createdAt;

  @JsonProperty("profile_picture")
  public String getProfilePicture() {
    return profilePicture;
  }

  public String getChatRoomId() {
    return chatRoomId;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public String getText() {
    return text;
  }

  public String getUserId() {
    return userId;
  }

  public String getUserName() {
    return userName;
  }
}
