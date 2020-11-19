package bu.edu.met673.api.innercircle.service;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Service;

@Service
public class FCMService {
  public void sendMessage(List<String> deviceTokens, String title, String textMessage)
      throws ExecutionException, InterruptedException {
    AndroidConfig config = getAndroidConfig();
    MulticastMessage message = MulticastMessage.builder().setAndroidConfig(config).setNotification(
        Notification.builder().setTitle(title).setBody(textMessage).build()
    ).addAllTokens(deviceTokens).build();
    sendAndGetResponse(message);
  }

  private BatchResponse sendAndGetResponse(MulticastMessage message)
      throws ExecutionException, InterruptedException {
    ApiFuture<BatchResponse> result =  FirebaseMessaging.getInstance().sendMulticastAsync(message);
    return result.get();
  }

  private AndroidConfig getAndroidConfig() {
    return AndroidConfig.builder().setPriority(AndroidConfig.Priority.HIGH).build();
  }
}
