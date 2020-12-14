package bu.edu.met673.api.innercircle.util;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ResponseUtilTest {

  @Test
  void buildErrorResponseEntity() {
    String message = "test";
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    HashMap<String, Object> additionalParams = null;

    ResponseEntity<Object> returnedResponse =
        ResponseUtil.buildErrorResponseEntity(message, httpStatus,
            null);

    assertEquals(returnedResponse.getStatusCode(), httpStatus);
  }
}