package ug_4.rant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import ug_4.rant.app.AppController;
import ug_4.rant.net_utils.Const;

import java.util.regex.Pattern;

public class InviteMembersActivity extends Activity {
    private Button inviteUser, makeModerator;
    private EditText email;
    private String userEmail;
    private boolean isModerator = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_members);
        inviteUser = (Button) findViewById(R.id.inviteMemberToGroupButton);
        makeModerator = (Button) findViewById(R.id.makeModeratorButton);
        email = (EditText) findViewById(R.id.enterEmailToInvite);
        onTextFieldChange(email);
        makeModerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isModerator == false) {
                    isModerator = true;
                    makeModerator.setBackgroundResource(R.drawable.round_corners_0f1923);
                } else if (isModerator == true) {
                    isModerator= false;
                    makeModerator.setBackgroundResource(R.drawable.round_corners_464c6b);
                }
                System.out.println(isModerator);
            }
        });
        inviteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidEmail(userEmail,true)){
                    postCheck();
                }
            }
        });

    }

    private void postCheck() {
        JSONObject groupData = new JSONObject();
        try {
            groupData.put("userName", userEmail);
            groupData.put("groupID", MembersGroupActivity.groupIDForPost);
            groupData.put("isModerator", isModerator);

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, Const.URL_ISUSERVER + "/groups/addUser", groupData, new Response.Listener<JSONObject>() {

            /**
             * Receives response from server if JSONObject is successfully sent.
             * Requires a proper server connection.
             * @param response Server response in JSONArray format
             */
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("message") && response.getString("message").equals("success")) {
                        startActivity(new Intent(InviteMembersActivity.this, UserCurrentGroupsActivity.class));
                        finish();
                    }
                    else if(response.has("message")&& response.getString("message").equals("user already in group")){
                        email.setText("");
                        email.setHintTextColor(Color.parseColor("#D42F2F"));
                        email.setHint("User Already In Group");
                    }
                    else if(response.has("message")&&response.getString("message").equals("user not found")){
                        email.setText("");
                        email.setHintTextColor(Color.parseColor("#D42F2F"));
                        email.setHint("User not found");
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
        AppController.getInstance().addToRequestQueue(strReq, "CreateGroupAttempt");

    }

    public boolean isValidEmail(String email, Boolean throwError) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null || !pat.matcher(email).matches()) {
            if (throwError) {
                this.email.setText("");
                this.email.setHintTextColor(Color.parseColor("#D42F2F"));
                this.email.setHint("Email must be in a proper format");
            }
            return false;
        }
        return true;
    }

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
                userEmail = email.getText().toString();


                if (isValidEmail(userEmail, false)) {
                    inviteUser.setBackgroundResource(R.drawable.round_corners_464c6b);
                } else {
                    inviteUser.setBackgroundResource(R.drawable.round_corners_0f1923);

                }
            }
        });
    }
}
