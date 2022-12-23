package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * Lobby Class
 * @author Isaac
 * This class holds all the players in a lobby until all players, including the host, are ready to begin playing Trivia
 */
public class Lobby extends Activity {
    Button btnESC;
    Button btnStartTrivia;

    Question questions[];
    Bundle extras;

    String START = "start";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        btnStartTrivia = findViewById(R.id.lobby_startTrivia);
        btnESC = findViewById(R.id.lobby_ESC);

        btnStartTrivia.setVisibility(View.INVISIBLE);

        extras = getIntent().getExtras();
        User user = (User)extras.getSerializable("user");
        //extras.getString("lobby")
        //checkHost(user.id);
        WebSocketClient ws = joinLobby(user.userName, "TestLobby");
        ws.connect();
        //START TRIVIA (ONLY HOST can see this button)
        btnStartTrivia.setOnClickListener(view -> {
            //ws.send(START);
            questions = getQuestions(0);
            Intent intent = questions[0].prepareActivity(getApplicationContext());//I'm assuming a non-empty list of questions
            intent.putExtra("client", 0);
            intent.putExtra("triviaQ", questions);
            intent.putExtra("currentQ", 0);
            intent.putExtra("score", 0);
            startActivity(intent);
        });

        //ESC BUTTON
        btnESC.setOnClickListener(view -> {
            //if the host presses 'ESE' then everyone is kicked to the dashboard
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(intent);
        });
    }

    private void checkHost(String lobbyId){
        String path = "http://coms-309-054.class.las.iastate.edu:8080/lobby" + "/" + lobbyId;
        Response.Listener<JSONObject> loginListener = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    int hostId = response.getInt("hostId");
                    if(((User)extras.getSerializable("user")).id == hostId){
                        btnStartTrivia.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError e){
                Toast t = Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_LONG);
                t.show();
            }
        };

        JsonObjectRequest checkHost = new JsonObjectRequest(Request.Method.GET, path, null, loginListener, errorListener);
        AppController.getInstance().addToRequestQueue(checkHost);
    }

    private Question[] getQuestions(int lobbyId){
        return null;
    }

    private WebSocketClient joinLobby(String userName, String lobbyName){
        URI uri = null;
        try {
            uri = new URI("ws://coms-309-054.class.las.iastate.edu:8080/" + userName + "/" + lobbyName);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        WebSocketClient ws = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {

            }

            @Override
            public void onMessage(String message) {
                if(message.equals(START)){

                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

            }

            @Override
            public void onError(Exception ex) {
                Toast t = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG);
                t.show();
            }
        };
        return ws;
    }
}