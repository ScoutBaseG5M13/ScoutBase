package es.dimecresalessis.scoutbase.infrastructure.user.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.infrastructure.shared.utils.JsonUtils;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDto;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("dev")
public class UserControllerIT {

    private final MockMvc mockMvc;
    private final MockUsersIT mockUsers;
    private final ObjectMapper objectMapper;

    private UserDto admin;
    private String jwtToken;

    private UserDto userTest;

    @BeforeAll
    void setUp() throws Exception {
        this.admin = mockUsers.createAdmin();
        this.jwtToken = mockUsers.login(admin);
    }

    @AfterAll
    void tearDown() throws Exception {
        mockUsers.deleteUser(admin, jwtToken);
    }

    @BeforeEach
    void beforeEach() {
        userTest = UserDto.getRandomInstance("ROLE_USER");
    }

    @Test
    void shouldCreateUser() {
        UserDto createdUser = null;
        try {
            createUser(userTest, status().isCreated());
            createdUser = findUserById(userTest.getId().toString(), status().isOk());

            assertNotNull(createdUser);
            assertNotNull(createdUser.getId());
            assertEquals(createdUser.getUsername(), userTest.getUsername());
            assertEquals(createdUser.getRole(), userTest.getRole());
        } finally {
            if (createdUser != null && createdUser.getId() != null) {
                deleteUserSilently(createdUser.getId().toString());
            }
        }
    }

    @Test
    void shouldCreateUser_WhenUserHasNoId() {
        UserDto createdUser = null;
        try {
            userTest.setId(null);
            createUser(userTest, status().isCreated());
            createdUser = findUserByUsername(userTest.getUsername(), status().isOk());

            assertNotNull(createdUser);
            assertNotNull(createdUser.getId());
            assertEquals(createdUser.getUsername(), userTest.getUsername());
        } finally {
            if (createdUser != null && createdUser.getId() != null) {
                deleteUserSilently(createdUser.getId().toString());
            }
        }
    }

    @Test
    void shouldFindUserById() {
        UserDto foundUser = null;
        try {
            createUser(userTest, status().isCreated());
            foundUser = findUserById(userTest.getId().toString(), status().isOk());

            assertNotNull(foundUser);
            assertEquals(foundUser.getId(), userTest.getId());
            assertEquals(foundUser.getUsername(), userTest.getUsername());
        } finally {
            if (userTest != null && userTest.getId() != null) {
                deleteUserSilently(userTest.getId().toString());
            }
        }
    }

    @Test
    void shouldThrowUserException_WhenUserIdDoesNotExist() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get(Routes.API_ROOT + Routes.USERS + "/" + nonExistentId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").value(UserException.class.getSimpleName()));
    }

    @Test
    void shouldUpdateUser() {
        UserDto oldUser = null;
        try {
            createUser(userTest, status().isCreated());
            oldUser = findUserById(userTest.getId().toString(), status().isOk());
            UserDto newUser = new UserDto(oldUser.getId(), "updatedUser", "newPass123", "ROLE_ADMIN");

            updateUser(oldUser.getId().toString(), newUser, status().isOk());
            UserDto finalUser = findUserById(oldUser.getId().toString(), status().isOk());

            assertNotNull(finalUser);
            assertEquals(finalUser.getUsername(), newUser.getUsername());
            assertEquals(finalUser.getRole(), newUser.getRole());
        } finally {
            if (oldUser != null && oldUser.getId() != null) {
                deleteUserSilently(oldUser.getId().toString());
            }
        }
    }

    @Test
    void shouldDeleteUser() {
        try {
            createUser(userTest, status().isCreated());
            deleteUser(userTest.getId().toString(), status().isOk());

            assertNull(findUserById(userTest.getId().toString(), status().isNotFound()));
        } finally {
            if (userTest != null && userTest.getId() != null) {
                deleteUserSilently(userTest.getId().toString());
            }
        }
    }

    @Test
    void shouldGetRoleFromToken() throws Exception {
        String expectedRole = admin.getRole();

        MvcResult result = mockMvc.perform(get(Routes.API_ROOT + Routes.USERS + Routes.ROLE_PATH)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn();

        String extractedRole = extractString(result);

        assertNotNull(extractedRole);
        assertEquals(expectedRole, extractedRole);
    }

    private UserDto findUserById(String id, ResultMatcher status) {
        try {
            MvcResult result = mockMvc.perform(get(Routes.API_ROOT + Routes.USERS + "/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status)
                    .andReturn();
            return extractUserDto(result);
        } catch (Exception e) {
            return null;
        }
    }

    private UserDto findUserByUsername(String username, ResultMatcher status) {
        try {
            MvcResult result = mockMvc.perform(get(Routes.API_ROOT + Routes.USERS + Routes.USERNAME_PATH + "/" + username)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status)
                    .andReturn();
            return extractUserDto(result);
        } catch (Exception e) {
            return null;
        }
    }

    private Boolean createUser(UserDto dto, ResultMatcher status) {
        try {
            MvcResult result = mockMvc.perform(post(Routes.API_ROOT + Routes.USERS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.toJson(dto))
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status)
                    .andExpect(jsonPath("$.success").value(true))
                    .andReturn();
            return extractBoolean(result);
        } catch (Exception e) {
            return null;
        }
    }

    private UserDto updateUser(String id, UserDto newDto, ResultMatcher status) {
        try {
            MvcResult result = mockMvc.perform(put(Routes.API_ROOT + Routes.USERS + "/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JsonUtils.toJson(newDto))
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status)
                    .andExpect(jsonPath("$.success").value(true))
                    .andReturn();
            return extractUserDto(result);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean deleteUser(String id, ResultMatcher status) {
        try {
            MvcResult result = mockMvc.perform(delete(Routes.API_ROOT + Routes.USERS + "/" + id)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status)
                    .andExpect(jsonPath("$.success").value(true))
                    .andReturn();
            return extractBoolean(result);
        } catch (Exception e) {
            return false;
        }
    }

    private void deleteUserSilently(String id) {
        try {
            // Usamos tu truco de I_AM_A_TEAPOT para forzar el error y que el catch Throwable lo ignore
            deleteUser(id, status().isIAmATeapot());
        } catch (Throwable ignored) {}
    }

    private UserDto extractUserDto(MvcResult result) throws Exception {
        String jsonResponse = result.getResponse().getContentAsString();
        return objectMapper.readValue(jsonResponse, new TypeReference<ApiResponse<UserDto>>() {}).data();
    }

    private Boolean extractBoolean(MvcResult result) throws Exception {
        String jsonResponse = result.getResponse().getContentAsString();
        return objectMapper.readValue(jsonResponse, new TypeReference<ApiResponse<Boolean>>() {}).data();
    }

    private String extractString(MvcResult result) throws Exception {
        String jsonResponse = result.getResponse().getContentAsString();
        return objectMapper.readValue(jsonResponse, new TypeReference<ApiResponse<String>>() {}).data();
    }
}