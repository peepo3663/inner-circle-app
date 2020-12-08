package bu.edu.met673.api.innercircle.util;

import bu.edu.met673.api.innercircle.exception.ChatRoomAlreadyExistedException;
import bu.edu.met673.api.innercircle.exception.UserAlreadyExistedException;
import bu.edu.met673.api.innercircle.exception.UserNotFoundException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Object> handleUserNotFoundException(HttpServletRequest request, Exception ex) {
    return ResponseUtil.buildErrorResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND, null);
  }

  @ExceptionHandler(UserAlreadyExistedException.class)
  public ResponseEntity<Object> handleUserAlreadyExistedException(HttpServletRequest request, Exception ex) {
    return ResponseUtil.buildErrorResponseEntity(ex.getMessage(), HttpStatus.CONFLICT, null);
  }

  @ExceptionHandler(ChatRoomAlreadyExistedException.class)
  public ResponseEntity<Object> handleChatRoomAlreadyExistedException(HttpServletRequest request, Exception ex) {
    ChatRoomAlreadyExistedException chatRoomAlreadyExistedException = (ChatRoomAlreadyExistedException) ex;
    HashMap<String, Object> additionalData = new HashMap<>();
    additionalData.put("chatRoomId", chatRoomAlreadyExistedException.getChatRoomId());
    return ResponseUtil.buildErrorResponseEntity(chatRoomAlreadyExistedException.getMessage(), HttpStatus.CONFLICT,
        additionalData);
  }
}
