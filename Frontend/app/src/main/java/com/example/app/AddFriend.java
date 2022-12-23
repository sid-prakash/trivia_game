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
import android.widget.Toast;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddFriend extends Activity {
    Button btnAddFriendSearch;
    Button btnAddFriendESC;
    EditText usernameInput;
    String friendUsername;
    private TextView friendListTextBox;
    private TextView requestTextBox;

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        btnAddFriendESC = (Button) findViewById(R.id.addFriend_ESC);
        btnAddFriendSearch = (Button) findViewById(R.id.addFriend_search);
        usernameInput = (EditText) findViewById(R.id.addFriend_plainText);
        //testing
        friendListTextBox = (TextView) findViewById((R.id.addFriend_currentFriendsDisplay));
        requestTextBox = (TextView) findViewById((R.id.addFriends_friendRequestDisplay));

        listFriends();

        /**
         * ADD FRIEND BUTTON OnClick
         * @author Isaac
         * When btnAddFriendSearch is pressed, it will send a addFriendReq Volley call to define the new relationship
         */
        btnAddFriendSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //record username (string)
                //username = usernameInput.getText().toString();
                //Toast.makeText(AddFriend.this, username, Toast.LENGTH_LONG).show();

                //attempt to add friend
                addFriend();
                //update friend's list
                listFriends();

                //testing, put name in request textbox

                Log.v("EditText", friendListTextBox.getText().toString());
                //msgResponse.setText(msgResponse.toString());
            }
        });

        /**
         * ESC BUTTON OnClick
         * @author Isaac
         * When btnAddFriendESC is pressed, it will transfer the activity from AddFriends to the Dashboard
         */
        btnAddFriendESC.setOnClickListener(view -> {
            Intent toDashboard = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(toDashboard);
        });
    }



    /**  listFriends JSON Array/String Call
    * @author Isaac
    * This call sends to the database a request to add a new friend relationship between the user-specified user and themselves.
    * On tutorials it's worded as it's a JSON Object request, it may be too.
    */
    private void listFriends() {
        Log.i("Test", "get current friends request");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Const.URL_friends_list
              ,new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       Log.i("test", "Friends list onResponse");
//                       username = usernameInput.getText().toString();
//                       Toast.makeText(AddFriend.this, (Const.URL_friends + "4/" + username), Toast.LENGTH_LONG).show();
                       //set Friends List Display to show request results
                       //friendListTextBox.setText(response.toString());
                       friendListTextBox.setText(response);
                    }
              }, new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       Log.i("Test", "unable to display friends");
                       friendListTextBox.setText(error.toString());
                       //Toast.makeText(AddFriend.this, "Username Not Found OR Unable to fulfill request", Toast.LENGTH_LONG).show();
                       friendUsername = usernameInput.getText().toString();
                   }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);

    }


    /**
     * Add Friend POST Volley Call
     * @author Isaac
     * When btnAddFriendSearch is pressed, this method will:
     *      - Create the rest of the url parameter: "userId/friendUsername"
     *      - Send Volley call
     *      - Checks the response for success OR cannot friend self
     *      - Updates friendListTextBox when complete
     * steps: display textfield in request box
     */
    private void addFriend()    {
        Log.i("test", "add friend method called");
        String url2 = (Const.URL_friends + "4/" + friendUsername);

        StringRequest strReq = new StringRequest(Request.Method.POST, (Const.URL_friends + Const.USERID + "/" + friendUsername)
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //make call
                Toast.makeText(AddFriend.this, "onResponse for addFriend", Toast.LENGTH_LONG).show();
                requestTextBox.setText(response.toString());

                //update friends list
                //listFriends();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                requestTextBox.setText((error.toString() + "    " + url2));
                Toast.makeText(AddFriend.this, "unable to request friendship", Toast.LENGTH_LONG).show();

            }
        });

        //add request to volley queue
        AppController.getInstance().addToRequestQueue(strReq);
    }




//        /**
//         * Add Friend Volley POST request
//         * @author Isaac
//         * When btnAddFriendSearch is pressed, this method takes the text in "addFriend_plainText"
//         * and makes a POST request attempting to add it to the database
//         *
//         * -Should not be able to friend yourself
//         *
//         */
//        private void  addFriend(String friendId) {   //I did have here tempFriendName
//                Log.i("test", "Add Friend Request Sent");
//            String url = Const.URL_friends + User.id + "/" +
//
//                StringRequest request = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.i("Test", "Add Friend Button Clicked");
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i("Test", "Add Friend Button Error");
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams()   {
//                //create a map to store our key and value pair to post
//                Map<String, String> params = new HashMap<String, String>();
//                //parse our key and value pair to our parameters
//                params.put("userID", User.id);
//                params.put("friendId", tempFriendName);
//                return params;
//            }
//        };
//        //add the request to the volley queue
//        AppController.getInstance().addToRequestQueue(postAddFriend);
//    }


}