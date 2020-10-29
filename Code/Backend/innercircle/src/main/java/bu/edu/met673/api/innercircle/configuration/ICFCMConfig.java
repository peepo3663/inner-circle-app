package bu.edu.met673.api.innercircle.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileInputStream;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ICFCMConfig {

  private static final Logger logger = Logger.getLogger(ICFCMConfig.class);

  @PostConstruct
  public void initFCM() {
    FileInputStream inputStream;
    try {
      inputStream = new FileInputStream("inner-circle-firebase-adminsdk.json");
      FirebaseOptions firebaseOptions =
          FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(inputStream))
              .setDatabaseUrl("https://inner-circle-fdd5f.firebaseio.com").build();
      FirebaseApp.initializeApp(firebaseOptions);
    } catch (IOException e) {
      logger.error("Something wrong with firebase", e);
    }
  }
}
