package com.example.app;

import android.content.Intent;
import android.os.Bundle;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.JsonArrayRequest;


public class CreateAccount extends Activity {
    Button btnBack, btnToDashboard;
    EditText etxtNewUsername, etxtNewPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        btnBack = findViewById(R.id.createAccount_back);
        btnToDashboard = findViewById(R.id.createAccount_to_dashboard);

        etxtNewUsername = findViewById(R.id.editTextNewUsername);
        etxtNewPassword = findViewById(R.id.editTextNewPassword);

        //BACK BUTTON
        btnBack.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(CreateAccount.this, Login.class);
                startActivity(intent);

            }
        });

        //TO DASHBOARD BUTTON
        btnToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                //Toast t = Toast.makeText(getApplicationContext(), "You clicked the right button", Toast.LENGTH_LONG);
                //t.show();
                createNewAccount(etxtNewUsername.getText().toString(), etxtNewPassword.getText().toString());
            }
        });
    }

    public void createNewAccount(String username, String password) {
        String path = "http://coms-309-054.class.las.iastate.edu:8080/profile" + "/";

        Response.Listener<JSONObject> createListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast t = Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_LONG);
                t.show();

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                intent.putExtra("user", username);
                startActivity(intent);
            }
        };

        String format = "{ \"displayname\": " + "\"" + username + "\", \"highScore\": 0, \"id\": 0, \"isActive\": true, \"password\": \"" + password + "\", \"username\": \"" + username +"\"}";
        JSONObject body = null;
        try {
            body = new JSONObject(format);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest createAccRequest = new JsonObjectRequest(Request.Method.POST, path, body, createListener, errorListener);

        AppController.getInstance().addToRequestQueue(createAccRequest);
    }

}