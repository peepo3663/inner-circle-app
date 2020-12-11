package bu.edu.met673.api.innercircle.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ChatRoom {
  private String chatRoomId;

  @JsonProperty("users")
  private User[] users;

  @JsonProperty("userIds")
  private List<String> userIds;

  @JsonProperty("users_count")
  private int usersCount;

  @JsonProperty("latestText")
  private String latestText;

  @JsonProperty("createdAt")
  private Date createdAt;

  @JsonProperty("updatedAt")
  private Date updatedAt;

  public ChatRoom() {
  }

  public ChatRoom(User[] users) {
    this.users = users;
  }

  public ChatRoom(String chatRoomId, Map<String, Object> data) {
    this.chatRoomId = chatRoomId;
    Object createdAt = data.get("createdAt");
    if (createdAt instanceof Date) {
      this.createdAt = (Date) createdAt;
    } else if (createdAt instanceof Timestamp) {
      this.createdAt = ((Timestamp) createdAt).toDate();
    }
    Object updatedAt = data.get("createdAt");
    if (updatedAt instanceof Date) {
      this.updatedAt = (Date) updatedAt;
    } else if (updatedAt instanceof Timestamp) {
      this.updatedAt = ((Timestamp) updatedAt).toDate();
    }
    Object usersCount =  data.get("users_count");
    if (usersCount instanceof Long) {
      this.usersCount = ((Long) usersCount).intValue();
    }
    Object userIds = data.get("userIds");
    if (userIds instanceof ArrayList<?>) {
      this.userIds = new ArrayList<>();
      ArrayList<?> userIdsArrayList = (ArrayList<?>) userIds;
      userIdsArrayList.forEach(element -> {
        if (element instanceof String) {
          this.userIds.add((String) element);
        }
      });
    }
    Object latestText = data.get("latestText");
    if (latestText instanceof String) {
      this.latestText = (String) latestText;
    }
    Object users = data.get("users");
    if (users instanceof ArrayList<?>) {
      ArrayList<User> usersResult = new ArrayList<User>();
      ArrayList<?> usersArrayList = (ArrayList<?>) users;
      usersArrayList.forEach(element -> {
        if (element instanceof Map<?, ?>) {
          Map<String, Object> map = (Map<String, Object>) element;
          User currentUser = new User(map);
          usersResult.add(currentUser);
        }
      });
      User[] usersArray = new User[usersResult.size()];
      this.users = usersResult.toArray(usersArray);
    }
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

  public List<String> getUserIds() {
    return userIds;
  }
}
