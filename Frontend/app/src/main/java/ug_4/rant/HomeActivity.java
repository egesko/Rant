package ug_4.rant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
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

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends Activity {

    private TextView rantTitle, rantBody;
    private Button likeButton, dislikeButton;
    private ImageView featuredNavButton, createNavButton, groupsNavButton, profileNavButton, likeIconButton, dislikeIconButton;
    private EditText searchBar;
    private Switch filterSwitch;
    private ScrollView layout;


    int rantIndex = 0;
    JSONArray rants = new JSONArray();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        rantTitle = (TextView) findViewById(R.id.rantTitle);
        rantBody = (TextView) findViewById(R.id.rantBody);
        likeButton = (Button) findViewById(R.id.likeButton);
        dislikeButton = (Button) findViewById(R.id.dislikeButton);
        likeIconButton = (ImageView) findViewById(R.id.likeIconButton);
        dislikeIconButton = (ImageView) findViewById(R.id.dislikeIconButton);
        createNavButton = (ImageView) findViewById(R.id.createNavButton);
        featuredNavButton = (ImageView) findViewById(R.id.featuredNavButton);
        groupsNavButton = (ImageView) findViewById(R.id.groupsNavButton);
        profileNavButton = (ImageView) findViewById(R.id.profileNavButton);
        searchBar = (EditText) findViewById(R.id.searchBar);
        filterSwitch = (Switch) findViewById(R.id.filterSwitch);
        layout = findViewById(R.id.scrollView);

        //Initializers
        setFilter(0);
        getRants();

        featuredNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FeaturedActivity.class));
            }
        });

        createNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CreateRantActivity.class));
            }
        });

        groupsNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, UserCurrentGroupsActivity.class));
            }
        });

        profileNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });

        layout.setOnTouchListener(new OnSwipeTouchListener(HomeActivity.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                rantIndex += 1;
                if (rantIndex == rants.length()) {
                    getRants();
                } else {
                    updateFields();
                }
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                rantIndex -= 1;
                if (rantIndex < 0) {
                    rantIndex = 0;
                }
                updateFields();
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (rants.getJSONObject(rantIndex).getInt("vote") != 1) {
                        voteRant("" + rants.getJSONObject(rantIndex).getInt("id") + "/like", true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (rants.getJSONObject(rantIndex).getInt("vote") != 2) {
                        voteRant("" + rants.getJSONObject(rantIndex).getInt("id") + "/dislike", false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                getTags(searchBar.getText().toString());
            }
        });

        filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setFilter(1);
                } else {
                    setFilter(2);
                }
            }
        });
    }

    private void updateFields() {
        try {
            rantTitle.setText(rants.getJSONObject(rantIndex).getString("title"));
            rantBody.setText(rants.getJSONObject(rantIndex).getString("rant"));
            likeButton.setText(rants.getJSONObject(rantIndex).getString("likes"));
            dislikeButton.setText(rants.getJSONObject(rantIndex).getString("disLikes"));
            if (rants.getJSONObject(rantIndex).getInt("vote") == 0) {
                likeButton.setTextColor(Color.parseColor("#000000"));
                dislikeButton.setTextColor(Color.parseColor("#000000"));
                likeIconButton.setBackgroundResource(R.drawable.volume_up_icon_disabled);
                dislikeIconButton.setBackgroundResource(R.drawable.volume_off_icon_disabled);
            } else if (rants.getJSONObject(rantIndex).getInt("vote") == 1) {
                likeButton.setTextColor(Color.parseColor("#ffffff"));
                dislikeButton.setTextColor(Color.parseColor("#000000"));
                likeIconButton.setBackgroundResource(R.drawable.volume_up_icon_active);
                dislikeIconButton.setBackgroundResource(R.drawable.volume_off_icon_disabled);
            } else if (rants.getJSONObject(rantIndex).getInt("vote") == 2) {
                dislikeButton.setTextColor(Color.parseColor("#ffffff"));
                likeButton.setTextColor(Color.parseColor("#000000"));
                likeIconButton.setBackgroundResource(R.drawable.volume_up_icon_disabled);
                dislikeIconButton.setBackgroundResource(R.drawable.volume_off_icon_active);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<String> getTags(String tags) {
        List<String> tagArray = Arrays.asList(tags.split(","));
        System.out.println(tagArray);
        return tagArray;
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
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST, Const.URL_ISUSERVER + "/rants/forYou", array, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Response", response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        rants.put(response.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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

    private void voteRant(final String format, final boolean isLiked) {
        JSONObject object = new JSONObject();
        try {
            object.put("userID", LoginActivity.userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Const.URL_ISUSERVER + "/rants/" + format, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                try {
                    if (isLiked) {
                        rants.getJSONObject(rantIndex).put("likes", rants.getJSONObject(rantIndex).getInt("likes") + 1);
                        if (rants.getJSONObject(rantIndex).getInt("vote") != 0) {
                            rants.getJSONObject(rantIndex).put("disLikes", rants.getJSONObject(rantIndex).getInt("disLikes") - 1);
                        }
                        rants.getJSONObject(rantIndex).put("vote", 1);
                    } else {
                        rants.getJSONObject(rantIndex).put("disLikes", rants.getJSONObject(rantIndex).getInt("disLikes") + 1);
                        if (rants.getJSONObject(rantIndex).getInt("vote") != 0) {
                            rants.getJSONObject(rantIndex).put("likes", rants.getJSONObject(rantIndex).getInt("likes") - 1);
                        }
                        rants.getJSONObject(rantIndex).put("vote", 2);
                    }
                    updateFields();
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
        AppController.getInstance().addToRequestQueue(req, "attemptLikeRant");
    }

    private void setFilter(int flag) {
        JSONObject object = new JSONObject();
        try {
            object.put("userID", LoginActivity.userID);
            object.put("flag", flag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, Const.URL_ISUSERVER + "/users/setWantsProfanity", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                try {
                    filterSwitch.setChecked(Boolean.parseBoolean(response.getString("state")));
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
        AppController.getInstance().addToRequestQueue(req, "attemptLikeRant");
    }
}