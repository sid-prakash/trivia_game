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
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import android.net.Uri;
import java.net.URL;

/**
 * JoinGame Class
 * @author Isaac
 * This class allows the user to join a publically displayed lobby.
 * When the user enters this page, a volley call will be sent to see all active lobbies and display them in join_displayLobbies (TextView)
 * The user will
 */
public class JoinGame extends Activity {
    Button btnESC;
    Button btnJoin;
    EditText lobbyName;
    private TextView lobbyFeed;
    boolean exitThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        btnESC = (Button) findViewById(R.id.joinGame_ESC);
        btnJoin = (Button) findViewById(R.id.joinGame_join);
        lobbyFeed = (TextView) findViewById(R.id.join_displayLobbies);
        lobbyName = (EditText) findViewById(R.id.join_lobbyNamePlainText);

        //initial Volley request for seeing active lobbies, refreshes every second
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!exitThread) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("Log.i", "thread running for updating display");
                                //volley call for refreshing active lobbies
                                updateActiveLobbies();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        thread.start();


        //JOIN BUTTON
        btnJoin.setOnClickListener(view -> {
            //create intent for moving to Lobby
            Intent intent = new Intent(getApplicationContext(), Lobby.class);
            //stops thread running updates to lobby list
            stopThread();
            //Websocket call for joining a user-specified lobby
            joinLobby();
            //moves to Lobby
            startActivity(intent);
        });

        //ESC BUTTON
        btnESC.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            stopThread();
            startActivity(intent);
        });
    }

    //for stopping the thread that updates the textview, used in while loop
    public void stopThread()  {
        exitThread = true;
    }

    /**
     * Volley Call for updating the textview to display active lobbies
     *  - This call will update the Join Lobby TextView (join_displayLobbies) with
     *    a list of all current active lobbies.
     */
    private void updateActiveLobbies()  {
        Log.i("test", "updateActiveLobbies Called");

        StringRequest strReq = new StringRequest(Method.GET, Const.URL_lobby_all, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("test", "onResponse for updateActiveLobbies hit");
                lobbyFeed.setText(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("test", "Failed: onErrorResponse hit for updateActiveLobbies");
            }
        });

        //Adding request to queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private void joinLobby()    {
        Log.i("test", "joinLobby Called");

        //FIXME: replace static names with info from login/create account
        String websocketURL = "ws://coms-309-054.class.las.iastate.edu:8080/lobby" + "Mason" + "/" + "TestLobby";
        Log.i("test", websocketURL);

    }
}