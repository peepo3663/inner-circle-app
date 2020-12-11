package bu.edu.met673.api.innercircle.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import bu.edu.met673.api.innercircle.InnercircleApplicationTests;
import bu.edu.met673.api.innercircle.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.Timestamp;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void testLoginUser() throws Exception {
    Map<String, Object> mockUserData = new HashMap<>();
    mockUserData.put("createdAt", Timestamp.now());
    mockUserData.put("updatedAt", Timestamp.now());
    mockUserData.put("name", "Wasupol Tungsakultong");
    mockUserData.put("uid", "LJOIboMJR9Y4mfDl9FDIbf43RVf2");
    mockUserData.put("email", "peepo157@gmail.com");
    User testUser = new User(mockUserData);

    this.mockMvc.perform(
        post("/users/create").contentType(InnercircleApplicationTests.APPLICATION_JSON_UTF8)
            .content(InnercircleApplicationTests.writeObjectToJSONString(testUser)))
        .andExpect(status().isOk());
  }

  @Test
  void testEditUserPictureProfile() throws Exception {
    Map<String, Object> mockUserData = new HashMap<>();
    mockUserData.put("createdAt", Timestamp.now());
    mockUserData.put("updatedAt", Timestamp.now());
    mockUserData.put("name", "Wasupol Tungsakultong");
    mockUserData.put("uid", "LJOIboMJR9Y4mfDl9FDIbf43RVf2");
    mockUserData.put("email", "peepo157@gmail.com");
    User testUser = new User(mockUserData);

    InputStream testImage = new ClassPathResource("/test_image.jpeg").getInputStream();
    MockMultipartFile testFile =
        new MockMultipartFile("file", "my_project.jpeg", MediaType.IMAGE_JPEG_VALUE, testImage);
    ObjectMapper mapper = new ObjectMapper();
    this.mockMvc.perform(multipart("/users/update/profile/" + testUser.getUid()).file(testFile))
        .andDo(mvcResult -> {
          Map<String, Object> response =
              mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), Map.class);
          testUser.setPictureUrl((String) response.get("profile_picture"));
        })
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.profile_picture", is(testUser.getPictureUrl())));
  }
}
