package ug_4.rant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import ug_4.rant.app.AppController;
import ug_4.rant.net_utils.Const;

import java.util.regex.Pattern;

/**
 * <h1>Rant Register Activity</h1>
 * Implements functionality for register.xml.
 * Allows a user to register for the app Rant by sending
 * credentials to the database.
 *
 * @author  Sergio Perez-Valentin
 * @version 1.0
 * @since   2021-01-25
 */
public class RegisterActivity extends Activity {

    private EditText fullNameField, emailField, passwordField;
    private Button register;
    private String fullName, email, password;
    private TextView login;


    /**
     * Creates and initializes the RegisterActivity screen.
     * All design buttons and views are initialized and set appropriate listeners.
     * @param savedInstanceState Stores Dynamic data once screen is closed in Bundle format.
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        fullNameField = (EditText) findViewById(R.id.fullNameEditText);
        emailField = (EditText) findViewById(R.id.emailEditText);
        passwordField = (EditText) findViewById(R.id.passwordEditText);
        register = (Button) findViewById(R.id.signupButton);
        login = (TextView) findViewById(R.id.login);

        onTextFieldChange(fullNameField);
        onTextFieldChange(emailField);
        onTextFieldChange(passwordField);

        register.setOnClickListener(new View.OnClickListener() {
            /**
             * When sign up button is clicked, attempts to register user.
             * @param v The object that is being handled.
             */
            @Override
            public void onClick(View v) {
                if (isValidFullName(fullName,true) && isValidEmail(email, true) && isValidPassword(password, true)) {
                    postData();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            /**
             * When login text is clicked, changes screen to login page.
             * @param v The object that is being handled.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    /**
     * Calls readyToSubmit function when field is modified.
     * @param field The textfield being modified.
     */
    private void onTextFieldChange(EditText field) {
        field.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                fullName = fullNameField.getText().toString();
                email = emailField.getText().toString();
                password = passwordField.getText().toString();

                if (isValidFullName(fullName,false) && isValidEmail(email, false) && isValidPassword(password, false)) {
                    register.setBackgroundResource(R.drawable.round_corners_464c6b);
                } else {
                    register.setBackgroundResource(R.drawable.round_corners_0f1923);
                }
            }
        });
    }

    /**
     * This method is used to validate if a string is a valid full name.
     * If the user is submitting, an error will be thrown with proper
     * details if the full name is not valid.
     * @param fullName The string that needs to be validated
     * @param throwError  If a error hint needs to be given to user
     * @return boolean Returns if the string is a valid full name.
     */
    public boolean isValidFullName(String fullName, Boolean throwError) {
        if (fullName.matches("[a-zA-Z- ]+")) {
            return true;
        } else {
            if (throwError) {
                fullNameField.setText("");
                fullNameField.setHintTextColor(Color.parseColor("#D42F2F"));
                fullNameField.setHint("Full Name must contain only characters");
            }
            return false;
        }
    }

    /**
     * This method is used to validate if a string is a valid email.
     * If the user is submitting, an error will be thrown with proper
     * details if the email is not valid.
     * @param email The string that needs to be validated
     * @param throwError  If a error hint needs to be given to user
     * @return boolean Returns if the string is a valid email.
     */
    public boolean isValidEmail(String email, Boolean throwError) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null || !pat.matcher(email).matches()) {
            if (throwError) {
                emailField.setText("");
                emailField.setHintTextColor(Color.parseColor("#D42F2F"));
                emailField.setHint("Email must be in a proper format");
            }
            return false;
        }
        return true;
    }

    /**
     * This method is used to validate if a string is a valid password.
     * If the user is submitting, an error will be thrown with proper
     * details if the password is not valid.
     * @param password The string that needs to be validated
     * @param throwError  If a error hint needs to be given to user
     * @return boolean Returns if the string is a valid password.
     */
    public boolean isValidPassword(String password, Boolean throwError) {
        if (password.length() < 8) {
            if (throwError) {
                passwordField.setText("");
                passwordField.setHintTextColor(Color.parseColor("#D42F2F"));
                passwordField.setHint("Password must be at least 8 characters");
            }
            return false;
        }
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (ch == ' ') { //(ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')
                if (throwError) {
                    passwordField.setText("");
                    passwordField.setHintTextColor(Color.parseColor("#D42F2F"));
                    passwordField.setHint("Password must contain no spaces");
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Sends a JSON post to server with fields
     * [Full Name, Email, Password, Is Admin]
     */
    private void postData() {
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("fullName", fullName);
            object.put("email", email);
            object.put("password", password);
            object.put("isAdmin", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Const.URL_ISUSERVER+"/users", object, new Response.Listener<JSONObject>() {
            /**
             * Receives response from server if proper connection
             * is acquired with database. Sends user to login screen
             * if fields accepted, throws error hint if declined.
             * @param response Server response in JSON format
             */
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", String.valueOf(response));
                try {
                    if (response.getString("message").equals("success")) {
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        emailField.setText("");
                        emailField.setHintTextColor(Color.parseColor("#D42F2F"));
                        emailField.setHint("Email already in use");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            /**
             * Provides error information if proper connection
             * is not acquired with database.
             * @param error Server error in Volley format
             */
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", String.valueOf(error));
            }
        });
        AppController.getInstance().addToRequestQueue(req, "attemptRegister");
    }
}