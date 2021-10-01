package ug_4.rant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ug_4.rant.app.AppController;
import ug_4.rant.net_utils.Const;

import java.lang.reflect.Array;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateRantActivity extends Activity {
    private EditText titleText, messageText, tagsText;
    private Button everyoneButton, selectGroupsButton, rantCreateButton;
    private String userTitle, userMessage, userTags;
    private boolean isEveryone = false;
    private String[] tagsList;
    private int[] groupListCreate;
    private JSONArray intGroups;
    private JSONArray stringTags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_rant);

        /*Init Components*/
        titleText = (EditText) findViewById(R.id.titleTextEdit);
        messageText = (EditText) findViewById(R.id.messageTextEdit);
        tagsText = (EditText) findViewById(R.id.tagTextEdit);

        everyoneButton = (Button) findViewById(R.id.everyoneButton);
        selectGroupsButton = (Button) findViewById(R.id.selectGroupsButton);
        rantCreateButton = (Button) findViewById(R.id.rantCreateButton);
        /**/

        onTextFieldChange(titleText);
        onTextFieldChange(messageText);
        onTextFieldChange(tagsText);

        everyoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEveryone == false) {
                    isEveryone = true;
                    everyoneButton.setBackgroundResource(R.drawable.round_corners_0f1923);
                } else if (isEveryone == true) {
                    isEveryone = false;
                    everyoneButton.setBackgroundResource(R.drawable.round_corners_464c6b);
                }
                System.out.println(isEveryone);
            }
        });

        selectGroupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateRantActivity.this, CurrentGroupsActivity.class));
            }
        });

        rantCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userTitle = titleText.getText().toString();
                userMessage = messageText.getText().toString();
                userTags = tagsText.getText().toString();
                if ((!userTitle.isEmpty()) && (!userMessage.isEmpty()) && (!userTags.isEmpty())) {
                    stringTags = new JSONArray();
                    tagsList = userTags.split(",");
                    for (int i = 0; i < tagsList.length; i++) {
                        tagsList[i] = tagsList[i].trim();
                        stringTags.put(tagsList[i]);
                    }
                    postData();
                }

            }
        });


    }

    private void postData() {
        JSONObject userDataCreateRant = new JSONObject();
        try {
            userDataCreateRant.put("title", userTitle);
            userDataCreateRant.put("rant", userMessage);

            userDataCreateRant.put("tags", stringTags);
            userDataCreateRant.put("userID", LoginActivity.userID);
            userDataCreateRant.put("postAll", isEveryone);

            int numSelected = 0;
            for (GroupContainer i : CurrentGroupsActivity.groupList) {
                if (i.isSelected()) {
                    numSelected++;
                }
            }
            groupListCreate = new int[numSelected];
            intGroups = new JSONArray();
            for (int i = 0; i < groupListCreate.length; i++) {
                groupListCreate[i] = CurrentGroupsActivity.groupList.get(i).getGroupID();
                intGroups.put(groupListCreate[i]);

            }
            userDataCreateRant.put("groups", intGroups);


        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, Const.URL_ISUSERVER + "/rants/create", userDataCreateRant, new Response.Listener<JSONObject>() {

            /**
             * Receives response from server if JSONObject is successfully sent.
             * Requires a proper server connection.
             * @param response Server response in JSONArray format
             */
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("message") && response.getString("message").equals("success")) {
                        System.out.println("Sucessfully Posted the Method!");
                        startActivity(new Intent(CreateRantActivity.this, HomeActivity.class));
                    }
                } catch (Exception e) {
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
                userTitle = titleText.getText().toString();
                userMessage = messageText.getText().toString();
                userTags = tagsText.getText().toString();


                if (userTitle.isEmpty() || userMessage.isEmpty() || userTags.isEmpty()) {
                    rantCreateButton.setBackgroundResource(R.drawable.round_corners_0f1923);
                } else {
                    rantCreateButton.setBackgroundResource(R.drawable.round_corners_464c6b);
                }
            }
        });
    }
}
