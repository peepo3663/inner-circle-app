package bu.edu.met673.api.innercircle.util;

import bu.edu.met673.api.innercircle.exception.ChatRoomAlreadyExistedException;
import bu.edu.met673.api.innercircle.exception.UserAlreadyExistedException;
import bu.edu.met673.api.innercircle.exception.UserNotFoundException;
import bu.edu.met673.api.innercircle.model.ChatRoom;
import bu.edu.met673.api.innercircle.model.Message;
import bu.edu.met673.api.innercircle.model.User;
import bu.edu.met673.api.innercircle.model.UserDevice;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.database.core.operation.Merge;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class FirestoreUtil {
  private static final FirestoreUtil instance = new FirestoreUtil();
  private Firestore firestore;
  private Bucket bucket;
  private CollectionReference chatsRef;
  private CollectionReference usersRef;
  private final String messageNodeName = "messages";

  private FirestoreUtil() {
    firestore = FirestoreClient.getFirestore();
    this.chatsRef = firestore.collection("chats");
    this.usersRef = firestore.collection("users");
    this.bucket = StorageClient.getInstance().bucket();
  }

  public static FirestoreUtil getInstance() {
    return instance;
  }

  public CollectionReference getChatsRef() {
    return chatsRef;
  }

  public CollectionReference getUsersRef() {
    return usersRef;
  }

  public Bucket getBucket() {
    return bucket;
  }

  public ChatRoom createChatRoom(ChatRoom room)
      throws ExecutionException, InterruptedException, ChatRoomAlreadyExistedException {
    User[] users = room.getAllUsers();
    if (users.length == 0) {
      throw new NullPointerException();
    }
    Query chatQuery = null;
    ArrayList<Map<String, Object>> usersForChatRoom = new ArrayList<>();
    ArrayList<String> userIds = new ArrayList<>();
    for (User user : users) {
      if (chatQuery == null) {
        chatQuery = chatsRef.whereArrayContains("userIds", user.getUid());
      } else {
        chatQuery.whereArrayContains("userIds", user.getUid());
      }
      usersForChatRoom.add(user.toMapDataForChatRoom());
      userIds.add(user.getUid());
    }
    ApiFuture<QuerySnapshot> result = chatQuery.get();
    QuerySnapshot chatRoomQuery = result.get();
    if (!chatRoomQuery.isEmpty()) {
      throw new ChatRoomAlreadyExistedException(chatRoomQuery.getDocuments().get(0).getId());
    }
    // add new chat room if need
    DocumentReference chatDocument = chatsRef.document();
    Map<String, Object> chatRoom = new HashMap<>();
    chatRoom.put("createdAt", Timestamp.now());
    chatRoom.put("userIds", userIds);
    chatRoom.put("users", usersForChatRoom);
    chatRoom.put("users_count", userIds.size());

    ApiFuture<WriteResult> writeResult = chatDocument.set(chatRoom);
    if (writeResult.get() != null) {
      room.setChatRoomId(chatDocument.getId());
    }
    return room;
  }

  public Message createAMessageNode(Message message)
      throws ExecutionException, InterruptedException {
    String chatRoomId = message.getChatRoomId();
    if (chatRoomId == null) {
      throw new NullPointerException();
    }
    ApiFuture<DocumentReference> result = chatsRef.document(chatRoomId).collection(messageNodeName).add(message.toData());
    DocumentReference documentReference = result.get();
    if (documentReference != null) {
      message.setMessageId(documentReference.getId());
      Map<String, Object> latestText = new HashMap<>();
      latestText.put("latestText", message.getText());
      latestText.put("updatedAt", Timestamp.now());
      chatsRef.document(chatRoomId).set(latestText, SetOptions.merge());
      return message;
    } else {
      throw new NullPointerException();
    }
  }

  public List<String> queryForUserDeviceTokens(Message message)
      throws ExecutionException, InterruptedException {
    DocumentSnapshot chatSnapshot = chatsRef.document(message.getChatRoomId()).get().get();
    List<String> users = (List<String>) chatSnapshot.get("userIds");
    if (users == null) {
      return null;
    }
    ArrayList<String> deviceTokens = new ArrayList<>();
    users.remove(message.getUserId());
    for (String userId: users) {
      Map<String, Object> userNode = usersRef.document(userId).get().get().getData();
      if (userNode == null) {
        continue;
      }
      User user = new User(userNode);
      List<String> userDevices = getUserDeviceTokens(user);
      deviceTokens.addAll(userDevices);
    }
    return deviceTokens;
  }

  public User queryForUser(User user)
      throws ExecutionException, InterruptedException, UserAlreadyExistedException,
      UserNotFoundException {
    // check that the user email already exist.
    ApiFuture<QuerySnapshot> query = usersRef.whereEqualTo("email", user.getEmail().trim()).get();
    QuerySnapshot querySnapshot = query.get();
    if (!querySnapshot.isEmpty()) {
      // user exist
      QueryDocumentSnapshot queryDocumentSnapshot = querySnapshot.getDocuments().get(0);
      User returnUser = new User(queryDocumentSnapshot);
      returnUser = updateUserIsOnline(returnUser);
      return returnUser;
    }
    // new user
    DocumentReference userDocument = usersRef.document();
    ApiFuture<WriteResult> result = userDocument.set(user.toMapData());
    if (result.get() != null) {
      user.setUid(userDocument.getId());
      return user;
    } else {
      throw new UserNotFoundException(user);
    }
  }

  private User updateUserIsOnline(User returnUser) {
    Map<String, Object> fieldsToUpdate = new HashMap<>();
    fieldsToUpdate.put("isUserOnline", true);
    try {
      usersRef.document(returnUser.getUid()).set(fieldsToUpdate, SetOptions.merge()).get();
      returnUser.setUserOnline(true);
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return returnUser;
  }

  public void updateProfilePicture(String userId, String pictureURL)
      throws ExecutionException, InterruptedException {
    DocumentReference userDocument = usersRef.document(userId);
    Map<String, String> profileProject = new HashMap<>();
    profileProject.put("profile_picture", pictureURL);
    userDocument.set(profileProject, SetOptions.merge());
  }

  private List<String> getUserDeviceTokens(User user) {
    List<String> deviceTokens = new ArrayList<>();
    int deviceTokensSize = user.getDevices().size();
    for (int i = 0; i < deviceTokensSize; i++) {
      UserDevice userDevice = user.getDevices().get(i);
      deviceTokens.add(userDevice.getDeviceToken());
    }
    return deviceTokens;
  }

  public Blob uploadPictureProfileFor(String userId, MultipartFile file) throws IOException {
    String blobName = String.format("%s/profile_picture.%s", userId, FilenameUtils.getExtension(file.getOriginalFilename()));
    return bucket.create(blobName, file.getBytes(), file.getContentType());
  }
}
