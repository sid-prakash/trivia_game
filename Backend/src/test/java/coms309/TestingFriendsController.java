package coms309;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TestingFriendsController {

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
    public void sendFriendRequestTest() throws JSONException {
        //Create two Profiles
        createProfile("Mason");
        createProfile("Sid");

        //Send friend request
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        Response response = request.post("/friends/2/Mason");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void  acceptFriendRequestTest() throws JSONException {
        //Create two Profiles
        createProfile("Mason");
        createProfile("Sid");

        //Send friend request
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.post("/friends/2/Mason");

        //Accept friend request
        RequestSpecification request2 = RestAssured.given();
        request.header("Content-Type", "application/json");
        Response response = request2.post("/friends/1/Sid");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void sendFriendRequestToSelfTest() throws JSONException {
        //Create profile
        createProfile("Mason");

        //Send friend request
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        Response response = request.post("/friends/1/Mason");
        int statusCode = response.getStatusCode();
        assertEquals(409, statusCode);
    }

    @Test
    public void getFriendRequestListTest() throws JSONException {
        //Create Profiles
        createProfile("Mason");
        createProfile("Sid");
        createProfile("Isaac");

        //Send Friend requests
        RequestSpecification request = RestAssured.given();
        request.header("Content-Types", "application/json");
        request.post("/friends/2/Mason");
        request.post("/friends/3/Mason");

        //Get friend request list
        RequestSpecification request2 = RestAssured.given();
        request2.header("Content-Types", "application/json");
        Response response = request2.get("/friends/requests/1");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void getEmptyFriendRequestList() throws JSONException {
        //Create Profile
        createProfile("Mason");

        //Get friend request list
        RequestSpecification request = RestAssured.given();
        request.header("Content-Types", "application/json");
        Response response = request.get("/friends/requests/1");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void getFriendsList() throws JSONException {
        //Create Profiles
        createProfile("Mason");
        createProfile("Sid");
        createProfile("Isaac");

        //Send Friend requests
        RequestSpecification request = RestAssured.given();
        request.header("Content-Types", "application/json");
        request.post("/friends/2/Mason");
        request.post("/friends/3/Mason");
        request.post("/friends/1/Sid");
        request.post("/friends/1/Isaac");

        //Get friends list
        RequestSpecification request2 = RestAssured.given();
        request2.header("Content-Types", "application/json");
        Response response = request2.get("/friends/1");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void getEmptyFriendsList() throws JSONException {
        //Create Profile
        createProfile("Mason");

        //Get friends list
        RequestSpecification request = RestAssured.given();
        request.header("Content-Types", "application/json");
        Response response = request.get("/friends/1");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void deleteFriends() throws JSONException {
        //Create Profiles
        createProfile("Mason");
        createProfile("Sid");

        //Delete friendship
        RequestSpecification request = RestAssured.given();
        request.header("Content-Types", "application/json");
        Response response = request.delete("/friends/1/2");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
    }
}
