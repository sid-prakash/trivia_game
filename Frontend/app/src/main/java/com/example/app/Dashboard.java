package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Dashboard
 * @author Lance
 * @author Isaac
 * This class is the central location of our app and is used to navigate to the user's profile, AddFriends, HostGame, JoinGame, and the user can logout.
 */
public class Dashboard extends Activity {
    Button btnJoinGame;
    Button btnHostGame;
    Button btnAddFriends;
    Button btnLogout;
    Button btnProfile;
    TextView bulletinDisplay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        btnJoinGame = findViewById(R.id.dashboard_joinGame);
        btnHostGame = findViewById(R.id.dashboard_hostGame);
        btnAddFriends = findViewById(R.id.dashboard_addFriends);
        btnLogout = findViewById(R.id.dashboard_logout);
        btnProfile = findViewById(R.id.dashboard_profile);
        bulletinDisplay = findViewById(R.id.dashboard_textView);

        Bundle extra = getIntent().getExtras();

        //update the dashboard bulletin
        dashboardBulletin();

        //JOIN GAME (using new format)
        btnJoinGame.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), JoinGame.class);
            startActivity(intent);
        });

        //HOST GAME
        btnHostGame.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), HostGame.class);
            User user = (User)extra.getSerializable("user");
            intent.putExtra("user", user);
            startActivity(intent);
        });

        //ADD FRIENDS
        btnAddFriends.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AddFriend.class);
            startActivity(intent);
        });

        //LOGOUT
        btnLogout.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        });

        //PROFILE (using old format)
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Dashboard Bulletin Assembly
     * @author Isaac
     * Updates the Dashboard's Bulletin view with the following from User.java:
     *      - Welcome "displayName"
     *      - Username (USERID)
     *      - isActive:
     *      - High Score
     *      - other stuff
     * Updates whenever the user enters the screen.
     */
    private void dashboardBulletin() {
        bulletinDisplay.setText("User Stats:");
    }

}