package bu.edu.met673.api.innercircle.exception;

import bu.edu.met673.api.innercircle.model.User;

public class UserAlreadyExistedException extends Exception {

  public UserAlreadyExistedException(User user) {
    super(String.format("%s with email of %s is already existed.", user.getName(), user.getEmail()));
  }

}
