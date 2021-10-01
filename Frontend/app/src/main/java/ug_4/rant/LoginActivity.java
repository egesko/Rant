package ug_4.rant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.helpers.Util;
import ug_4.rant.app.AppController;
import ug_4.rant.net_utils.Const;
import java.util.Date;
import java.util.Calendar;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * <h1>Rant Login Activity</h1>
 * The LoginActivity program allows a user to enter
 * their credentials within designated areas.
 * Includes logic to return data from the server and parse
 * information to check whether user inputs are correct.
 * <p>
 * Implements functionality from LoginActivity.xml
 * <p>
 * Supports Volley Integration
 *
 * @author Damandeep Riat
 * @version 1.0
 * @since 2021-03-14
 */
public class LoginActivity extends Activity {
    private EditText emailField, passwordField;
    private Button login;
    private String email;
    private String password;
    private TextView toSignUp;
    public static int userID; // stores userID upon successful login.
    private WebSocketClient mWebSocketClient;
    private String msgHolder;


    /**
     * Creates and initializes the LoginActivity screen.
     * All design buttons and views are initialized
     * and set appropriate listeners.
     *
     * @param savedInstanceState Stores Dynamic data once screen is closed in Bundle format.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        /*Init Button and Textfields*/
        emailField = (EditText) findViewById(R.id.loginEditText);
        passwordField = (EditText) findViewById(R.id.passEditText);
        login = (Button) findViewById(R.id.loginButton);
        toSignUp = (TextView) findViewById(R.id.register);



        onTextFieldChange(passwordField);
        onTextFieldChange(emailField);


        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * When "Sign Up" is clicked, the screen is changed to the Register Screen
             * @param v the object that is being handled.
             */
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            /**
             * Signifies when a user clicks on the "Sign Up" button.
             * Grabs user inputs and runs the server request to compare
             * user inputted credentials with stored database credentials.
             *
             * @param v Holds all the components of the current screen in a saved state.
             */
            @Override
            public void onClick(View v) {
                System.out.println(email + " \n" + password);
                postCheck(); // communicate with Server

            }
        });


    }

    /**
     * Method that constantly saves the user typed content in each text field upon a change.
     * Logic is set upon changes regarding the correct format that each text field requires for
     * proper login.
     *
     * @param field textfield that method watches for changes.
     */
    private void onTextFieldChange(EditText field) {
        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Grab data that user inputs into fields and convert into String format
                email = emailField.getText().toString();
                password = passwordField.getText().toString();


                if (email.isEmpty() || password.isEmpty()) {
                    login.setBackgroundResource(R.drawable.round_corners_0f1923);
                } else {
                    login.setBackgroundResource(R.drawable.round_corners_464c6b);
                }
            }
        });
    }


    /**
     * Makes a JsonObject of user inputs and sends data to the server.
     * Server will perform logic and respond to
     * Data is retrieved as a JsonObject of String responses.
     * <p>
     * If user inputs are correct, user id (int) will be included as well as a string response
     */
    public void postCheck() {
        JSONObject userData = new JSONObject();
        try {
            userData.put("email", email);
            userData.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, Const.URL_ISUSERVER + "/users/login", userData, new Response.Listener<JSONObject>() {

            /**
             * Receives response from server if JSONObject is successfully sent.
             * Requires a proper server connection.
             * @param response Server response in JSONArray format
             */
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("result") && response.getString("result").equals("success")) {
                        connectWebSocket();
                        while(msgHolder== null){

                        }
                    createBox();

                        //startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        userID = response.getInt("id");
                        System.out.println("User ID: " + userID);


                    } else if (response.has("failure") && response.getString("failure").equals("no email")) {
                        incorrectDetailsNoEmail();

                    } else if (response.has("failure") && response.getString("failure").equals("password dont match")) {
                        incorrectDetailsWrongPassword();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            /**
             * Provides error information if proper connection
             * is not acquired with database.
             * Located in log.
             * @param error Server error in Volley format
             */
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Login", "Error: " + error.getMessage());

            }
        });


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "UserLoginAttempt");

    }


    /**
     * Creates a hint that is displayed if
     * the user inputted the incorrect credentials when compared with
     * server response. No email could be found in the database
     */
    private void incorrectDetailsNoEmail() {
        emailField.setText("");
        emailField.setHintTextColor(Color.parseColor("#D42F2F"));
        emailField.setHint("Email is incorrect");
    }
    private void connectWebSocket() {
        URI uri;
        try {
            /*
             * To test the clientside without the backend, simply connect to an echo server such as:
             *  "ws://echo.websocket.org"
             */
             uri = new URI("ws://10.24.227.186:8080/chat/test");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                System.out.println("Connected with the server");
                mWebSocketClient.send(email);
            }

            @Override
            public void onMessage(String msg) {
                Log.i("Websocket", "Message Received");
                System.out.println(msg);
                msgHolder=msg;


            }

            @Override
            public void onClose(int errorCode, String reason, boolean remote) {
                Log.i("Websocket", "Closed " + reason);
            }


            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();


    }



    /**
     * Creates a hint that is displayed if
     * the user inputted the incorrect credentials when compared with
     * server response. Password associated with email is incorrect.
     */
    private void incorrectDetailsWrongPassword() {
        passwordField.setText("");
        passwordField.setHintTextColor(Color.parseColor("#D42F2F"));
        passwordField.setHint("Password is incorrect");
    }

    private void createBox(){
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Greeting")

                .setMessage(msgHolder)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }
                })


                // A null listener allows the button to dismiss the dialog and take no further action.

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
