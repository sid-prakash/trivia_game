package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends Activity {
    Button btnLogin, btnCreateAccount;
    EditText etxtUser, etxtPassword;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnCreateAccount = (Button) findViewById(R.id.login_createAccount);
        btnLogin = (Button) findViewById(R.id.login_login);
        etxtUser = (EditText) findViewById(R.id.editTextTextPersonName);
        etxtPassword = (EditText) findViewById(R.id.editTextTextPassword);


        //Create Account Button Action
        btnCreateAccount.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view)  {
                Intent intent = new Intent(getApplicationContext(), CreateAccount.class);
                startActivity(intent);
            }
        });

        //Login Button Action
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                checkUserNamePassword(etxtUser.getText().toString(), etxtPassword.getText().toString());
            }
        });
    }

    public void checkUserNamePassword(String username, String password){
        String path = "http://coms-309-054.class.las.iastate.edu:8080/profile" + "/" + username + "/" + password;

        Response.Listener<JSONObject> loginListener = new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                if(response != null){
                    try {
                        User user = new User(response.getInt("id"), response.getString("username"), response.getString("displayname"), response.getString("password"), response.getInt("highScore"), response.getBoolean("isActive"));
                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast t = Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_LONG);
                    t.show();
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

        JsonObjectRequest checkCredentials = new JsonObjectRequest(Request.Method.GET, path, null, loginListener, errorListener);
        AppController.getInstance().addToRequestQueue(checkCredentials);
    }



}