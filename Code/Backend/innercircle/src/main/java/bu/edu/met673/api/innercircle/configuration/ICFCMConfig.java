package bu.edu.met673.api.innercircle.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class ICFCMConfig {

  private static final Logger logger = Logger.getLogger(ICFCMConfig.class);

  @PostConstruct
  public void initFCM() {
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
