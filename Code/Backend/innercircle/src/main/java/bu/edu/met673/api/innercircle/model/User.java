package bu.edu.met673.api.innercircle.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
  private String uid;
  private String name;
  private String email;
  private String pictureUrl;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private boolean isUserOnline = false;

  @JsonProperty("devices")
  private List<UserDevice> devices;

  public User() { }

  public User(QueryDocumentSnapshot querySnapshot) {
    this.uid = querySnapshot.getId();
    this.createdAt = querySnapshot.get("createdAt", Timestamp.class);
    this.updatedAt = querySnapshot.get("updatedAt", Timestamp.class);
    this.name = querySnapshot.get("name", String.class);
    this.email = querySnapshot.get("email", String.class);
    this.pictureUrl = querySnapshot.get("profile_picture", String.class);
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

  public Map<String, Object> toMapData() {
    HashMap<String, Object> userData = new HashMap<>();
    userData.put("name", this.name);
    userData.put("email", this.email);
    userData.put("profile_picture", this.pictureUrl);
    userData.put("createdAt", this.createdAt);
    userData.put("updatedAt", this.updatedAt);
    return userData;
  }
}
