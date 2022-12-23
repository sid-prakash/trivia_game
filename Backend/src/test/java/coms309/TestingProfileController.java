package coms309;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TestingProfileController {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    private void createProfile(String username) throws JSONException {
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", username);
        requestParams.put("password", "password");
        request.header("Content-Type", "application/json");
        request.body(requestParams.toString());
        request.post("/profile");
    }

    @Test
    public void createProfileTest() throws JSONException {
        // Profile successful created
        RequestSpecification request = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "Sid");
        requestParams.put("password", "pass123");
        request.header("Content-Type", "application/json");
        request.body(requestParams.toString());
        Response response = request.post("/profile");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Profile already exists
        RequestSpecification request2 = RestAssured.given();
        request2.body(requestParams.toString());
        request2.header("Content-Type", "application/json");
        Response response2 = request2.post("/profile");
        int statusCode2 = response2.getStatusCode();
        assertEquals(208, statusCode2);

        // Missing username or password
        RequestSpecification request3 = RestAssured.given();
        JSONObject requestParams3 = new JSONObject();
        requestParams3.put("username", "Mason");
        request3.header("Content-Type", "application/json");
        request3.body(requestParams3.toString());
        Response response3 = request3.post("/profile");
        int statusCode3 = response3.getStatusCode();
        assertEquals(400, statusCode3);
    }

    @Test
    public void getProfileTest() throws JSONException {
        // Profile created
        createProfile("Sid");

        // Getting the profile created by ID
        Response response2 = RestAssured.given().get("/profile/1");
        int statusCode2 = response2.statusCode();
        assertEquals(200, statusCode2);

        // Getting a profile that is not in the database
        Response response3 = RestAssured.given().get("/profile/4");
        int statusCode3 = response3.statusCode();
        assertEquals(400, statusCode3);
    }

    @Test
    public void loginProfileTest() throws JSONException {
        // Profile created
        createProfile("Sid");

        // Successfully logging in
        Response response2 = RestAssured.given().get("/profile/Sid/password");
        int statusCode2 = response2.statusCode();
        assertEquals(200, statusCode2);

        // Wrong username or password
        Response response3 = RestAssured.given().get("/profile/Sid/pass123");
        int statusCode3 = response3.statusCode();
        assertEquals(401, statusCode3);
    }

    @Test
    public void updateProfileTest() throws JSONException {
        // Profile created
        createProfile("Sid");

        // Updating the profile created by ID
        RequestSpecification request2 = RestAssured.given();
        JSONObject requestParams2 = new JSONObject();
        requestParams2.put("displayname", "Mason");
        requestParams2.put("highScore", 10);
        request2.header("Content-Type", "application/json");
        request2.body(requestParams2.toString());
        Response response2 = request2.put("/profile/1");
        int statusCode2 = response2.statusCode();
        assertEquals(200, statusCode2);

        // Updating a profile that is not in the database
        Response response3 = request2.put("/profile/3");
        int statusCode3 = response3.statusCode();
        assertEquals(400, statusCode3);
    }

    @Test
    public void deleteProfileTest() throws JSONException {
        // Profile created
        createProfile("Sid");

        // Deleting the profile created by ID
        Response response2 = RestAssured.given().delete("/profile/1");
        int statusCode2 = response2.statusCode();
        assertEquals(200, statusCode2);

        // Deleting a profile that is not in the database
        Response response3 = RestAssured.given().delete("/profile/4");
        int statusCode3 = response3.statusCode();
        assertEquals(400, statusCode3);
    }

    @Test
    public void getActiveProfileTest() throws JSONException {
        // Profile created
        createProfile("Sid");
        createProfile("Mason");

        // Getting all active profiles
        Response response2 = RestAssured.given().get("/profile/active");
        int statusCode2 = response2.statusCode();
        assertEquals(200, statusCode2);
    }

}
