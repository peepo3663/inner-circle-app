package bu.edu.met673.api.innercircle.util;

//import org.junit.jupiter.api.Test;
import bu.edu.met673.api.innercircle.configuration.Constants;
import bu.edu.met673.api.innercircle.exception.UserAlreadyExistedException;
import bu.edu.met673.api.innercircle.exception.UserNotFoundException;
import bu.edu.met673.api.innercircle.model.User;
import bu.edu.met673.api.innercircle.util.FirestoreUtil;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Timestamp;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class FirestoreUtilTest {

    @Test
    void userAddedSuccessfullyTest() throws InterruptedException, ExecutionException, UserAlreadyExistedException, UserNotFoundException, IOException {
        //Test DAta
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("name", "Ananya");
        userData.put("email", "anshatest13");
        userData.put("profile_picture", "/test/url");
        userData.put("createdAt", Timestamp.MIN_VALUE);
        userData.put("updatedAt", Timestamp.MAX_VALUE);

        //Actual
        if (!checkFirebaseApp()) {
            InputStream inputStream = new ClassPathResource(Constants.googleServiceJSON).getInputStream();
            FirebaseOptions firebaseOptions =
                FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(inputStream))
                    .setDatabaseUrl(Constants.firebaseDatabaseURL)
                    .setStorageBucket(Constants.firebaseBucketURL).build();
            FirebaseApp.initializeApp(firebaseOptions);
        }

        User addedUser = FirestoreUtil.getInstance().queryForUser(new User(userData));
        assertEquals(addedUser.getEmail(),userData.get("email"));
        //TEst case when firebsae is not working
        //assertThrows(ExceptionInInitializerError.class, () -> FirestoreUtil.getInstance().queryForUser(new User(userData)));
        //Test case when firebase is working, but usernotfound
        //assertThrows(UserNotFoundException.class, () -> FirestoreUtil.getInstance().queryForUser(new User(userData)));
//      assertThrows(UserAlreadyExistedException.class, () -> FirestoreUtil.getInstance().queryForUser(new User(userData)));
    }
    @Test
    void userAlreadyExistsTest() throws InterruptedException, ExecutionException, UserAlreadyExistedException, UserNotFoundException, IOException {
        //Test DAta
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("name", "Ananya");
        userData.put("email", "anshatest13");
        userData.put("profile_picture", "/test/url");
        userData.put("createdAt", Timestamp.MIN_VALUE);
        userData.put("updatedAt", Timestamp.MAX_VALUE);

        //Actual
        if (!checkFirebaseApp()) {
            InputStream inputStream = new ClassPathResource(Constants.googleServiceJSON).getInputStream();
            FirebaseOptions firebaseOptions =
                FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(inputStream))
                    .setDatabaseUrl(Constants.firebaseDatabaseURL)
                    .setStorageBucket(Constants.firebaseBucketURL).build();
            FirebaseApp.initializeApp(firebaseOptions);
        }

        //TEst case when firebsae is not working
        //assertThrows(ExceptionInInitializerError.class, () -> FirestoreUtil.getInstance().queryForUser(new User(userData)));
        //Test case when firebase is working, but usernotfound
        //assertThrows(UserNotFoundException.class, () -> FirestoreUtil.getInstance().queryForUser(new User(userData)));
        assertNotNull(FirestoreUtil.getInstance().queryForUser(new User(userData)));
        // assertThrows(UserAlreadyExistedException.class, () -> FirestoreUtil.getInstance().queryForUser(new User(userData)));
    }

    private boolean checkFirebaseApp() {
        boolean hasBeenInitialized = false;
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        int appSize = firebaseApps.size();
        for (int i = 0; i < appSize; i++) {
            FirebaseApp currentApp = firebaseApps.get(i);
            if (currentApp.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
                hasBeenInitialized = true;
            }
        }
        return hasBeenInitialized;
    }
}