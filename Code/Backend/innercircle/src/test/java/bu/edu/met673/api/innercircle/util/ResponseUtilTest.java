package bu.edu.met673.api.innercircle.util;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ResponseUtilTest {

    @Test
    void buildErrorResponseEntity() {
        String message = "test";
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        HashMap<String, Object> additionalParams = null;

        ResponseEntity<Object> returnedResponse = ResponseUtil.buildErrorResponseEntity(message, httpStatus, additionalParams);

        assertEquals(returnedResponse.getStatusCode(), httpStatus);
    }
}