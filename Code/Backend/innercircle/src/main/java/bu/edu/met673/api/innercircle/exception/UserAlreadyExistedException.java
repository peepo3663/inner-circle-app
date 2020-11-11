package bu.edu.met673.api.innercircle.exception;

import bu.edu.met673.api.innercircle.model.User;

public class UserAlreadyExistedException extends Throwable {
  public UserAlreadyExistedException(User user) {
  }
}
