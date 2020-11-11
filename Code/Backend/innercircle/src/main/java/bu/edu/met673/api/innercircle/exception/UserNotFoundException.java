package bu.edu.met673.api.innercircle.exception;

import bu.edu.met673.api.innercircle.model.User;

public class UserNotFoundException extends Throwable {
  public UserNotFoundException(User user) {
  }
}
