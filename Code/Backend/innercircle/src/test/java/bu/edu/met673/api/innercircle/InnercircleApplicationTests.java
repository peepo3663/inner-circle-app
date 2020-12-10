package bu.edu.met673.api.innercircle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest
public class InnercircleApplicationTests {

  public static final MediaType
      APPLICATION_JSON_UTF8 =
      new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(),
          StandardCharsets.UTF_8);

  public static String writeObjectToJSONString(Object object)
      throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    return ow.writeValueAsString(object);
  }

  @Test
  void contextLoads() {
  }

}
