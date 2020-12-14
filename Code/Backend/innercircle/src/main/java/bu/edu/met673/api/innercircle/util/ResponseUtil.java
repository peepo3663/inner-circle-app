package bu.edu.met673.api.innercircle.util;

import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
  public static ResponseEntity<Object> buildErrorResponseEntity(String message,
                                                                HttpStatus httpStatus,
                                                                HashMap<String, Object> additionalParams) {
    HashMap<String, Object> response;
    if (additionalParams == null) {
      response = new HashMap<>();
    } else {
      response = new HashMap<>(additionalParams);
    }
    response.put("errorMsg", message);
    response.put("status", httpStatus.value());
    return new ResponseEntity<>(response, httpStatus);
  }
}
