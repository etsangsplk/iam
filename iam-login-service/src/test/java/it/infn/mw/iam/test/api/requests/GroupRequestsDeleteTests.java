package it.infn.mw.iam.test.api.requests;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import it.infn.mw.iam.IamLoginService;
import it.infn.mw.iam.api.requests.model.GroupRequestDto;
import it.infn.mw.iam.test.util.WithAnonymousUser;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {IamLoginService.class})
@WebAppConfiguration
@Transactional
public class GroupRequestsDeleteTests extends GroupRequestsTestUtils {

  private final static String DELETE_URL = "/iam/group_requests/{uuid}";

  @Autowired
  private WebApplicationContext context;

  private MockMvc mvc;
  private GroupRequestDto request;

  @Before
  public void setup() {
    mvc = MockMvcBuilders.webAppContextSetup(context)
      .apply(springSecurity())
      .alwaysDo(print())
      .build();

    request = savePendingGroupRequest(TEST_USERNAME, TEST_GROUPNAME);
  }

  @Test
  @WithMockUser(roles = {"ADMIN"})
  public void deletePendingGroupRequestAsAdmin() throws Exception {
    // @formatter:off
    mvc.perform(delete(DELETE_URL, request.getUuid()))
      .andExpect(status().isNoContent());
    // @formatter:on
  }

  @Test
  @WithMockUser(roles = {"ADMIN"})
  public void deleteApprovedGroupRequestAsAdmin() throws Exception {
    request = saveApprovedGroupRequest(TEST_USERNAME, TEST_GROUPNAME);
    // @formatter:off
    mvc.perform(delete(DELETE_URL, request.getUuid()))
      .andExpect(status().isNoContent());
    // @formatter:on
  }

  @Test
  @WithMockUser(roles = {"USER"}, username = TEST_USERNAME)
  public void deletePendingGroupRequestAsUser() throws Exception {
    // @formatter:off
    mvc.perform(delete(DELETE_URL, request.getUuid()))
      .andExpect(status().isNoContent());
    // @formatter:on
  }

  @Test
  @WithMockUser(roles = {"USER"}, username = TEST_USERNAME)
  public void deleteApprovedGroupRequestAsUser() throws Exception {

    request = saveApprovedGroupRequest(TEST_USERNAME, TEST_GROUPNAME);

    // @formatter:off
    mvc.perform(delete(DELETE_URL, request.getUuid()))
      .andExpect(status().isBadRequest());
    // @formatter:on
  }

  @Test
  @WithMockUser(roles = {"USER"}, username = TEST_USERNAME)
  public void deleteGroupRequestOfAnotherUser() throws Exception {

    request = savePendingGroupRequest("test_101", TEST_GROUPNAME);

    // @formatter:off
    mvc.perform(delete(DELETE_URL, request.getUuid()))
      .andExpect(status().isForbidden());
    // @formatter:on
  }

  @Test
  @WithAnonymousUser
  public void deleteGroupRequestAsAnonymous() throws Exception {
    // @formatter:off
    mvc.perform(delete(DELETE_URL, request.getUuid()))
      .andExpect(status().isUnauthorized());
    // @formatter:on
  }

  @Test
  @WithMockUser(roles = {"ADMIN"})
  public void deleteNotExitingGroupRequest() throws Exception {

    String fakeRequestUuid = UUID.randomUUID().toString();

    // @formatter:off
    mvc.perform(delete(DELETE_URL, fakeRequestUuid))
      .andExpect(status().isBadRequest());
    // @formatter:on
  }

}
