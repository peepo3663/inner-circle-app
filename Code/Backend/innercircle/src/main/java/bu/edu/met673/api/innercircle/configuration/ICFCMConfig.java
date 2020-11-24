package bu.edu.met673.api.innercircle.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class ICFCMConfig {

  private static final Logger logger = Logger.getLogger(ICFCMConfig.class);
  private boolean hasBeenInitialized = false;

  @PostConstruct
  public void initFCM() {
    List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
    int appSize = firebaseApps.size();
    for (int i = 0; i < appSize; i++) {
      FirebaseApp currentApp = firebaseApps.get(i);
      if (currentApp.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
        hasBeenInitialized = true;
        break;
      }
    }
    if (hasBeenInitialized) {
      return;
    }
    InputStream inputStream;
    try {
      inputStream = new ClassPathResource("/inner-circle-firebase-adminsdk.json").getInputStream();
      FirebaseOptions firebaseOptions =
          FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(inputStream))
              .setDatabaseUrl("https://inner-circle-fdd5f.firebaseio.com")
              .setStorageBucket("inner-circle-fdd5f.appspot.com").build();
      FirebaseApp.initializeApp(firebaseOptions);
    } catch (IOException e) {
      logger.error("Something wrong with firebase", e);
    }
  }
}
