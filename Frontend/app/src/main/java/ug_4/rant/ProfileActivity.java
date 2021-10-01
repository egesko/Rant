package ug_4.rant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.RequiresApi;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ug_4.rant.app.AppController;
import ug_4.rant.net_utils.Const;

public class ProfileActivity extends Activity {

    private TextView name, rantTitle, rantTags, rantBody, rantLikes, rantDislikes;
    private EditText rantTitleEditText, rantTagsEditText, rantBodyEditText;
    private Button updateButton, deleteButton;
    private LinearLayout layout;

    int rantIndex = 0;
    JSONArray rants = null;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        name = (TextView) findViewById(R.id.name);
        rantTitle = (TextView) findViewById(R.id.rantTitle);
        rantTags = (TextView) findViewById(R.id.rantTags);
        rantBody = (TextView) findViewById(R.id.rantBody);
        rantLikes = (TextView) findViewById(R.id.rantLikes);
        rantDislikes = (TextView) findViewById(R.id.rantDislikes);
        rantTitleEditText = (EditText) findViewById(R.id.rantTitleEditText);
        rantTagsEditText = (EditText) findViewById(R.id.rantTagsEditText);
        rantBodyEditText = (EditText) findViewById(R.id.rantBodyEditText);
        updateButton = (Button) findViewById(R.id.updateButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        layout = findViewById(R.id.linearLayout);

        getUser();
        getRants();

        layout.setOnTouchListener(new OnSwipeTouchListener(ProfileActivity.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                rantIndex += 1;
                if (rantIndex <= rants.length()) {
                    rantIndex = 0;
                }
                updateFields();
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                rantIndex -= 1;
                if (rantIndex < 0) {
                    rantIndex = rants.length()-1;
                }
                updateFields();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rantTitleEditText.getText().toString().matches("")) {
                    rantTitle.setText(rantTitleEditText.getText().toString());
                }
                if (!rantTagsEditText.getText().toString().matches("")) {
                    rantTags.setText(rantTagsEditText.getText().toString());
                }
                if (!rantBodyEditText.getText().toString().matches("")) {
                    rantBody.setText(rantBodyEditText.getText().toString());
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                rants.remove(rantIndex);
                if (rantIndex == rants.length()-1) {
                    rantIndex -= 1;
                }
                updateFields();
            }
        });
    }

    private void updateFields() {
        try {
            rantTitle.setText(rants.getJSONObject(rantIndex).getString("title"));
            rantTags.setText(rants.getJSONObject(rantIndex).getString("tags"));
            rantBody.setText(rants.getJSONObject(rantIndex).getString("rant"));
            rantLikes.setText("Likes: " + rants.getJSONObject(rantIndex).getString("likes"));
            rantDislikes.setText("Dislikes: " + rants.getJSONObject(rantIndex).getString("disLikes"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getRants() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            object.put("userID", LoginActivity.userID);
            array.put(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST, Const.URL_ISUSERVER + "/rants/forUser", array, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Response", response.toString());
                rants = response;
                updateFields();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: ", error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(req, "attemptGetRants");
    }

    private void getUser() {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,Const.URL_ISUSERVER + "/users/" + LoginActivity.userID, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                try {
                    name.setText(response.getString("fullName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: ", error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(req, "attemptGetRants");
    }
}
