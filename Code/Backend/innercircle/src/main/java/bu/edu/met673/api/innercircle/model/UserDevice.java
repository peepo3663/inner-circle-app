package bu.edu.met673.api.innercircle.model;

public class UserDevice {
  private String deviceToken;
  private String model;
  private String osVersion;

  public UserDevice(String deviceToken, String model, String osVersion) {
    this.deviceToken = deviceToken;
    this.model = model;
    this.osVersion = osVersion;
  }

  public String getDeviceToken() {
    return deviceToken;
  }
}
