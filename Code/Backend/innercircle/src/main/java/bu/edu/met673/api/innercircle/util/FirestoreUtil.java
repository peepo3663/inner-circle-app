package bu.edu.met673.api.innercircle.util;

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
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
  private final String deviceTokensNodeName = "deviceTokens";

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
      throws ExecutionException, InterruptedException {
    List<User> users = Arrays.asList(room.getAllUsers());
    if (users.size() == 0) {
      throw new NullPointerException();
    }
    ArrayList<Map<String, Object>> usersForChatRoom = new ArrayList<>();
    ArrayList<String> userIds = new ArrayList<>();
    for (User user : users) {
      usersForChatRoom.add(user.toMapDataForChatRoom());
      userIds.add(user.getUid());
    }
    ApiFuture<QuerySnapshot> result = chatsRef.whereArrayContains("userIds", userIds.get(0)).get();
    QuerySnapshot chatRoomQuery = result.get();
    if (chatRoomQuery.isEmpty()) {
      return createNewChatroom(room, usersForChatRoom, userIds);
    }
    int documentSize = chatRoomQuery.size();
    List<QueryDocumentSnapshot> documentSnapshots = chatRoomQuery.getDocuments();
    for (int i = 0; i < documentSize; i++) {
      QueryDocumentSnapshot documentSnapshot = documentSnapshots.get(i);
      ChatRoom chatRoom = new ChatRoom(documentSnapshot.getId(), documentSnapshot.getData());
      if (chatRoom.getUserIds().containsAll(userIds)) {
        return chatRoom;
      }
    }
    return createNewChatroom(room, usersForChatRoom, userIds);
  }

  private ChatRoom createNewChatroom(ChatRoom room, ArrayList<Map<String, Object>> usersForChatRoom,
                                     ArrayList<String> userIds)
      throws InterruptedException, ExecutionException {
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
    for (String userId: users) {
      if (userId.equals(message.getUserId())) {
        continue;
      }
      Map<String, Object> userNode = usersRef.document(userId).get().get().getData();
      if (userNode == null) {
        continue;
      }
      userNode.put("uid", userId);
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
      updateUserIsOnline(returnUser);
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
    // update users' node
    DocumentReference userDocument = usersRef.document(userId);
    Map<String, Object> profileProject = new HashMap<>();
    profileProject.put("profile_picture", pictureURL);
    userDocument.update(profileProject).get();

    // update chat's node
    QuerySnapshot chatSnapshot = chatsRef.whereArrayContains("userIds", userId).get().get();
    if (chatSnapshot == null || chatSnapshot.isEmpty()) {
      return;
    }
    int chatQueryDocumentSnapshotSize = chatSnapshot.size();
    List<QueryDocumentSnapshot> chatQueryDocumentSnapshots = chatSnapshot.getDocuments();
    for (int i = 0; i < chatQueryDocumentSnapshotSize; i++) {
      QueryDocumentSnapshot chatQueryDocumentSnapshot = chatQueryDocumentSnapshots.get(i);
      ChatRoom chatRoom = new ChatRoom(chatQueryDocumentSnapshot.getId(), chatQueryDocumentSnapshot.getData());
      List<User> users = Arrays.asList(chatRoom.getAllUsers());
      List<Map<String, Object>> usersToSave = new ArrayList<>();
      for (int j = 0; j < users.size(); j++) {
        User user = users.get(j);
        if (user.getUid().equals(userId)) {
          user.setPictureUrl(pictureURL);
        }
        usersToSave.add(user.toMapDataForChatRoom());
      }
      Map<String, Object> updateImages = new HashMap<>();
      updateImages.put("users", usersToSave);
      DocumentReference userChatRef = chatsRef.document(chatRoom.getChatRoomId());
      userChatRef.update(updateImages).get();

      // update message's node
      CollectionReference messagesRef = userChatRef.collection(messageNodeName);
      QuerySnapshot messageQuery = messagesRef.whereEqualTo("userId", userId).get().get();
      if (!messageQuery.isEmpty()) {
        int messageSize = messageQuery.size();
        List<QueryDocumentSnapshot> messageQueryDocuments = messageQuery.getDocuments();
        for (int k = 0; k < messageSize; k++) {
          Map<String, Object> updatePicture = new HashMap<String, Object>() {{
            put("profile_picture", pictureURL);
          }};
          messagesRef.document(messageQueryDocuments.get(k).getId()).update(updatePicture).get();
        }
      }
    }
  }

  private List<String> getUserDeviceTokens(User user)
      throws ExecutionException, InterruptedException {
    List<String> deviceTokens = new ArrayList<>();
    CollectionReference userDevicesCollectionReference = usersRef.document(user.getUid()).collection(deviceTokensNodeName);
    QuerySnapshot userDevicesQuerySnapshot = userDevicesCollectionReference.get().get();
    List<QueryDocumentSnapshot> userDevicesDocuments = userDevicesQuerySnapshot.getDocuments();
    int devicesSize = userDevicesQuerySnapshot.size();
    for (int i = 0; i < devicesSize; i++) {
      QueryDocumentSnapshot device = userDevicesDocuments.get(i);
      UserDevice userDevice = new UserDevice(device);
      deviceTokens.add(userDevice.getDeviceToken());
    }
    return deviceTokens;
  }

  public void updateUserDeviceToken(String userId, UserDevice userDevice)
      throws Exception {
    CollectionReference userDevicesCollectionReference = usersRef.document(userId).collection(deviceTokensNodeName);
    // check the existing
    Query checkExistingDevice = userDevicesCollectionReference.whereEqualTo("deviceToken", userDevice.getDeviceToken());

    QuerySnapshot userDevicesQuerySnapshot = checkExistingDevice.get().get();
    // is not exist
    if (userDevicesQuerySnapshot == null || userDevicesQuerySnapshot.isEmpty()) {
      // create node
      userDevicesCollectionReference.document().set(userDevice, SetOptions.merge());
    } else {
      throw new Exception("This device already registered to the system.");
    }
  }

  public Blob uploadPictureProfileFor(String userId, MultipartFile file) throws IOException {
    String blobName = String.format("%s/profile_picture.%s", userId, FilenameUtils.getExtension(file.getOriginalFilename()));
    return bucket.create(blobName, file.getBytes(), file.getContentType());
  }
}
