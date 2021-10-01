package ug_4.rant;

import android.app.Activity;
import android.content.Intent;
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

public class MainCreateGroupActivity extends Activity {
    private Button createGroup;
    private EditText title, message;
    private String userTitle, userMessage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_groups);
        createGroup = (Button) findViewById(R.id.groupCreateButton);
        title = (EditText) findViewById(R.id.createGroupName);
        message = (EditText) findViewById(R.id.createGroupDesp);
        onTextFieldChange(title);
        onTextFieldChange(message);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!userTitle.isEmpty()) && !userMessage.isEmpty()) {
                    postCheck();
                }


            }
        });


    }

    private void postCheck() {
        JSONObject groupData = new JSONObject();
        try {
            groupData.put("userID", 2);
            groupData.put("groupName", userTitle);
            groupData.put("information", userMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, Const.URL_ISUSERVER + "/groups/createFor", groupData, new Response.Listener<JSONObject>() {

            /**
             * Receives response from server if JSONObject is successfully sent.
             * Requires a proper server connection.
             * @param response Server response in JSONArray format
             */
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("message") && response.getString("message").equals("success")) {
                        startActivity(new Intent(MainCreateGroupActivity.this, UserCurrentGroupsActivity.class));

                        finish();
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
                userTitle = title.getText().toString();
                userMessage = message.getText().toString();


                if (userTitle.isEmpty() || userMessage.isEmpty()) {
                    createGroup.setBackgroundResource(R.drawable.round_corners_0f1923);
                } else {
                    createGroup.setBackgroundResource(R.drawable.round_corners_464c6b);
                }
            }
        });
    }
}
