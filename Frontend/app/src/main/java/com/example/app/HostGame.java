package com.example.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;


import java.net.URI;
import java.net.URISyntaxException;

import androidx.annotation.Nullable;

/**
 * HostGame Class
 * @author Isaac
 * This class allows the user to create a new lobby. The lobby rules are set here for a game of trivia.
 * These include the number of questions, the theme of questions, and the lobby password.
 */
public class HostGame extends Activity {
    Button btnESC, btnCreateLobby, btnFullyRandom;
    EditText hostLobbyName;
    private WebSocketClient newLobby;
    String lobbyNameResponse;   //used to store server response when calling /lobby/available/{lobbyName}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);

        btnFullyRandom = (Button) findViewById(R.id.host_btnRandom);
        btnCreateLobby = (Button) findViewById(R.id.host_btnHost);
        btnESC = (Button) findViewById(R.id.host_btnESC);
        hostLobbyName = (EditText) findViewById(R.id.host_lobbyName);

        //FULLY RANDOM
        btnFullyRandom.setOnClickListener(view ->   {
            Intent intent = new Intent(getApplicationContext(), Lobby.class);
            //check to see if lobby name is already taken
            checkLobbyName();
            //if lobby name is available, connect to Websocket
            if (lobbyNameResponse.equals("available")) {
                randomTrivia();
                startActivity(intent);
            }
        });

        Bundle extra = getIntent().getExtras();

        //HOST LOBBY BUTTON
        btnCreateLobby.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Lobby.class);
            User user = (User)extra.getSerializable("user");
            intent.putExtra("user", user);
            startActivity(intent);
        });

        //ESC BUTTON
        btnESC.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(intent);
        });


    }


    /**
     * @author Isaac
     * checkLobbyName() checks to see if there is currently an active lobby with the same name
     *  - Response will be a string, either "available" or "unavailable"
     */
    private void checkLobbyName()   {
        String desiredLobbyName = hostLobbyName.getText().toString();
        String path = ("http://coms-309-054.class.las.iastate.edu:8080/lobby/available/" + desiredLobbyName);
        Log.i("checkLobbyName_path", path);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.GET, path, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("checkLobbyName Test", "onResponse hit");
                        lobbyNameResponse = response.toString();
                        Log.i("lobbyNameResponse", lobbyNameResponse);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("checkLobbyName Test", "onError hit");

            }
        });
    }


    //Connects to Websocket while asking for a random set of trivia
    private void randomTrivia() {
        String desiredLobbyName = hostLobbyName.toString();
        Log.i("desiredLobby Log.i", desiredLobbyName);

        URI uri;
        try {
            uri = new URI("ws://coms-309-054.class.las.iastate.edu:8080/lobby/Sid/" + desiredLobbyName);
        } catch (URISyntaxException e)  {
            e.printStackTrace();
            return;
        }

        newLobby = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i("Log.i test", "newLobby onOpen hit");
                newLobby.send("Hello from..." ); //FIXME: Add user's name to greeting
                Log.i("onOpen Websocket", handshakedata.toString());

            }

            @Override
            public void onMessage(String message) {
                Log.i("Log.i test", "newLobby onMessage hit");

            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i("Log.i test", "newLobby onClose hit");

            }

            @Override
            public void onError(Exception ex) {
                Log.i("Log.i test", "newLobby onError hit");

            }
        };


    }
    // ../trivia/random/{num}/{lobbyId}
}