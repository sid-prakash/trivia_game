//profile java

package com.example.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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


public class Profile extends Activity {
    private String TAG = Profile.class.getSimpleName();
    private Button btnProfileEsc, btnJsonObj;
    private String paramID, paramUsername, paramDisplayname, paramPassword, paramHighScore, paramIsActive;
    private TextView msgResponse, UsernameDisplay;
    private ProgressDialog pDialog;

    //variables used for parsing profile information, [volley call "makeJsonObjReqInitial()"]
//    private int parsedID, parsedHighScore;
//    private String parsedDisplayName, parsedPassword;
//    private Boolean parsedIsActive;

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnJsonObj = (Button) findViewById(R.id.profile_json_object);
        btnProfileEsc = (Button) findViewById(R.id.profile_esc);
        msgResponse = (TextView) findViewById(R.id.profile_textbox);
        UsernameDisplay = (TextView) findViewById(R.id.profile_displayname);

        //RequestQueue queue = Volley.newRequestQueue(Profile.this);

        //Initial Volley Calls for Displaying Info
        makeJsonObjReqInitial();

        //ESC BUTTON
        btnProfileEsc.setOnClickListener(new View.OnClickListener()    {
            @Override
            public void onClick(View view)  {
                Intent toDashboard = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(toDashboard);
            }
        });

        //JSON OBJECT
        btnJsonObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeJsonObjReq();
            }
        });
    }

//    private void showProgressDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }
//
//    private void hideProgressDialog() {
//        if (pDialog.isShowing())
//            pDialog.hide();
//    }


    /**
     * Initial JSON Object Call
     * - Populates User's Name at top of page
     */
    private void makeJsonObjReqInitial() {
        //showProgressDialog();
        Log.i("Test", "testing: Initial Volley Call");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.GET,
                Const.URL_profile_ID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //parse "/profile/id"
                        try {

                            //fetch JSONObject user information
                            //JSONObject parsedID = response.getInt("id");
                            //JSONObject parsedDisplayName = jsonObjectRequest.getString("username");
                            //JSONObject parsedPassword = jsonObjectRequest.getString("password");
                            //JSONObject parsedHighScore = obj.getInt("highscore");
                            //JSONObject parsedIsActive = obj.getBoolean("isActive");

                            Integer parsedID = response.getInt("id");
                            String parsedDisplayName = response.getString("username");
                            String parsedDisplayname = response.getString("displayname");
                            String parsedPassword = response.getString("password");
                            Integer parsedHighScore = response.getInt("highscore");
                            Boolean parsedIsActive = response.getBoolean("isActive");

                            Log.i("Test", paramDisplayname);
                            //set user information on profile activity
                        } catch (JSONException e)   {
                            //this will execute if there was problems extracting data from json object
                            e.printStackTrace();
                        }

                        Log.i("Test", "testing: Initial Profile Json Object Called");
                        //UsernameDisplay.setText(response.toString());
                        UsernameDisplay.setText(paramDisplayname);
                        //Toast.makeText(Profile.this, "username set", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Test", "unable to set username display");
                //UsernameDisplay.setText(error.toString());
                Toast.makeText(Profile.this, "unable to set username display", Toast.LENGTH_LONG).show();
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", paramID);
                params.put("username", paramUsername);
                params.put("displayname", paramDisplayname);
                params.put("password", paramPassword);
                params.put("highScore", paramHighScore);
                params.put("isActive", paramIsActive);

                JSONObject initialActivity = new JSONObject(getParams());

                //Nest the object at "userCredentials"
                params.put("userCredentials", initialActivity.toString());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    /**
     * Making json object request for JSON_OBJECT BUTTON
     * - Output displays in the textbox within profile
     */
    private void makeJsonObjReq() {
        //showProgressDialog();
        Log.i("Test", "json");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.GET,
                Const.URL_profile_ID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //parse "../profile/id"
//                        try {
//                            JSONObject obj = response.getJSONObject("");
//                            int id = obj.getInt("id");
//                            String username = response.getString("username");
//                            String displayname = obj.getString("displayname");
//                        }

                        Log.i("Test", "response");
                        msgResponse.setText(response.toString());
                        Toast.makeText(Profile.this, "Json Object Requested", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Test", "response2");
                        msgResponse.setText(error.toString());
                        Toast.makeText(Profile.this, "Json Object Requested Failed!", Toast.LENGTH_LONG).show();
                        //VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                }) {

                    /**
                     * Passing some request headers
                     * */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", paramID);
                        params.put("username", paramUsername);
                        params.put("displayname", paramDisplayname);
                        params.put("password", paramPassword);
                        params.put("highScore", paramHighScore);
                        params.put("isActive", paramIsActive);

                        JSONObject initialActivity = new JSONObject(getParams());

                        //Nest the object at "userCredentials"
                        params.put("userCredentials", initialActivity.toString());

                        return params;
                    }
                };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

}

//    @Override
//    public void onClick(View view)  {
//        switch (view.getId())   {
//            case R.id.profile_json_object:
//                makeJsonObjReq();
//                break;
////            case R.id.profile_json_array:
////                makeJsonArryReq();
////                break;
//        }
//    }


//    @Override
//    public void onClick(View view) {
//        if(view.getId() == R.id.profile_json_object) {
//            String url = "https://coms-309-054.class.las.iastate.edu/profile/1";
//            StringRequest stringRequest = new StringRequest(JsonObjectRequest.Method.POST, url,
//                    response -> Toast.makeText(Profile.this, "Json Object Pressed.", Toast.LENGTH_LONG).show(),
//                    error -> Toast.makeText(Profile.this, "Json Object not made", Toast.LENGTH_LONG).show());
//        }
//
//        //JSON_OBJECT BUTTON
//        btnJsonObj.setOnClickListener(new View.OnClickListener()    {
//            @Override
//            public void onClick(View view)  {
//                String url = "https://coms-309-054.class.las.iastate.edu/profile/1";
//                //StringRequest stringRequest = new StringRequest(JsonObjectRequest.Method.POST);
//
//                StringRequest stringRequest = new StringRequest(JsonObjectRequest.Method.POST, url,
//                        response -> Toast.makeText(Profile.this, "Json Object Pressed.", Toast.LENGTH_LONG).show(),
//                        error -> Toast.makeText(Profile.this, "Json Object not made", Toast.LENGTH_LONG).show());
//
//                //RequestQueue.add(jsonObjectRequest);
//                makeJsonObjReq(); //this should do the same thing as all that above
//            }
//        });



/**
 * Making json array request
 */
//    private void makeJsonArrayReq() {
////                showProgressDialog();
//        JsonArrayRequest req = new JsonArrayRequest(Const.URL_profile_ID,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d(TAG, response.toString());
//                        msgResponse.setText(response.toString());
////                                hideProgressDialog();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
////                        hideProgressDialog();
//            }
//        });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(req, tag_json_arry);
//
//        // Cancelling request
//        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_arry);
//    }


