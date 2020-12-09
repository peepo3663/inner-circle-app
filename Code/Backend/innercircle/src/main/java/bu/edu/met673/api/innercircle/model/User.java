package bu.edu.met673.api.innercircle.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
  private String uid;
  private String name;
  private String email;

  @JsonProperty("profile_picture")
  private String pictureUrl;

  @JsonProperty("createdAt")
  private Date createdAt;

  @JsonProperty("updatedAt")
  private Date updatedAt;

  private boolean isUserOnline = false;

  @JsonProperty("devices")
  private List<UserDevice> devices;

  public User() { }

  public User(Map<String, Object> userNode) {
    this.uid = (String) userNode.get("uid");
    Timestamp createdAt = (Timestamp) userNode.get("createdAt");
    Timestamp updatedAt = (Timestamp) userNode.get("updatedAt");
    if (createdAt != null) {
      this.createdAt = createdAt.toDate();
    }
    if (updatedAt != null) {
      this.updatedAt = updatedAt.toDate();
    }
    this.name = (String) userNode.get("name");
    this.email = (String) userNode.get("email");
    this.pictureUrl = (String) userNode.get("profile_picture");
    this.devices = (List<UserDevice>) userNode.get("devices");
  }

  public User(QueryDocumentSnapshot querySnapshot) {
    this.uid = querySnapshot.getId();
    Timestamp createdAt = querySnapshot.get("createdAt", Timestamp.class);
    if (createdAt != null) {
      this.createdAt = createdAt.toDate();
    }
    Timestamp updatedAt = querySnapshot.get("updatedAt", Timestamp.class);
    if (updatedAt != null) {
      this.updatedAt = updatedAt.toDate();
    }
    this.name = querySnapshot.get("name", String.class);
    this.email = querySnapshot.get("email", String.class);
    this.pictureUrl = querySnapshot.get("profile_picture", String.class);
    this.devices = (List<UserDevice>) querySnapshot.get("devices");
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  @JsonProperty("devices")
  public List<UserDevice> getDevices() {
    return devices;
  }

  public Map<String, Object> toMapData() {
    HashMap<String, Object> userData = new HashMap<>();
    userData.put("name", this.name);
    userData.put("email", this.email);
    userData.put("profile_picture", this.pictureUrl);
    if (this.createdAt == null) {
      this.createdAt = new Date();
    }
    if (this.updatedAt == null) {
      this.updatedAt = new Date();
    }
    userData.put("createdAt", this.createdAt);
    userData.put("updatedAt", this.updatedAt);
    userData.put("isUserOnline", true);
    return userData;
  }

  public Map<String, Object> toMapDataForChatRoom() {
    HashMap<String, Object> userData = new HashMap<>();
    userData.put("uid", this.getUid());
    userData.put("name", this.name);
    userData.put("email", this.email);
    userData.put("profile_picture", this.pictureUrl);
    return userData;
  }

  public String getPictureUrl() {
    return pictureUrl;
  }

  public void setPictureUrl(String pictureUrl) {
    this.pictureUrl = pictureUrl;
  }

  public boolean isUserOnline() {
    return isUserOnline;
  }

  public void setUserOnline(boolean userOnline) {
    isUserOnline = userOnline;
  }
}
