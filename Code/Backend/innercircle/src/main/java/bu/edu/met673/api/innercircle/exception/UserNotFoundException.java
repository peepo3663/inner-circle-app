package bu.edu.met673.api.innercircle.exception;

import bu.edu.met673.api.innercircle.model.User;

public class UserNotFoundException extends Exception {
  public UserNotFoundException(User user) {
    super(String.format("This email %s can not be added or found.", user.getEmail()));
  }
}
