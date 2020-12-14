package bu.edu.met673.api.innercircle.model;

import com.google.cloud.firestore.QueryDocumentSnapshot;

public class UserDevice {
  private String userDeviceId;
  private String deviceToken;
  private String model;
  private String osVersion;

  public UserDevice(String deviceToken, String model, String osVersion) {
    this.deviceToken = deviceToken;
    this.model = model;
    this.osVersion = osVersion;
  }

  public UserDevice(QueryDocumentSnapshot device) {
    this(device.getString("deviceToken"), device.getString("model"), device.getString("osVersion"));
    this.userDeviceId = device.getId();
  }

  public String getUserDeviceId() {
    return userDeviceId;
  }

  public String getDeviceToken() {
    return deviceToken;
  }

  public void setDeviceToken(String deviceToken) {
    this.deviceToken = deviceToken;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getOsVersion() {
    return osVersion;
  }

  public void setOsVersion(String osVersion) {
    this.osVersion = osVersion;
  }
}
