package bu.edu.met673.api.innercircle.controller;

import bu.edu.met673.api.innercircle.exception.UserAlreadyExistedException;
import bu.edu.met673.api.innercircle.exception.UserNotFoundException;
import bu.edu.met673.api.innercircle.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import java.util.concurrent.ExecutionException;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  private static Logger logger = Logger.getLogger(UserController.class);
  private CollectionReference usersRef = FirestoreClient.getFirestore().collection("users");

  @PostMapping("/create")
  public User createUsers(@RequestBody User user) throws Exception {
    // check that the user email already exist.
    ApiFuture<QuerySnapshot> query = usersRef.whereEqualTo("email", user.getEmail().trim()).get();
    QuerySnapshot querySnapshot = query.get();
    if (!querySnapshot.isEmpty()) {
      // user exist
      throw new UserAlreadyExistedException(user);
    }
    // new user

    ApiFuture<WriteResult> result = usersRef.document().set(user.toMapData());
    if (result.get() != null) {
      return user;
    } else {
      throw new UserNotFoundException(user);
    }
  }
}
