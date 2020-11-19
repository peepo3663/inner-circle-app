package bu.edu.met673.api.innercircle.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import bu.edu.met673.api.innercircle.model.User;
import com.google.cloud.Timestamp;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class)
@ActiveProfiles("test")
class UserControllerTests {

  @MockBean
  private MockMvc mockMvc;

  @Test
  void testEditUserPictureProfile() throws Exception {
    Map<String, Object> mockUserData = new HashMap<>();
    mockUserData.put("createdAt", Timestamp.now());
    mockUserData.put("updatedAt", Timestamp.now());
    mockUserData.put("name", "Wasupol Tungsakultong");
    mockUserData.put("uid", "LJOIboMJR9Y4mfDl9FDIbf43RVf2");
    mockUserData.put("email", "peepo157@gmail.com");
    User testUser = new User(mockUserData);

    this.mockMvc.perform(post("/users/update/profile", testUser))
    .andExpect(status().isOk()).andExpect(jsonPath("$.profile_picture", is(testUser.getPictureUrl())));
  }
}
