package bu.edu.met673.api.innercircle.model;

import com.google.cloud.Timestamp;

public class User {
  private String uid;
  private String name;
  private String email;
  private String pictureUrl;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private boolean isUserOnline;

  public User() { }
}
