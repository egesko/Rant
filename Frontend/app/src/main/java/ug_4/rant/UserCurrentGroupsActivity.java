package ug_4.rant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ug_4.rant.app.AppController;
import ug_4.rant.net_utils.Const;

import java.util.ArrayList;


public class UserCurrentGroupsActivity extends Activity {
    private RecyclerView recyclerView;
    private UsersGroupAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public static ArrayList<GroupContainer> groupList = new ArrayList<>();

    private ImageView addButton;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_current_groups);

        addButton = (ImageView) findViewById(R.id.imageView69);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserCurrentGroupsActivity.this, MainCreateGroupActivity.class));

                finish();
            }
        });

        postCheck();

    }

    private void initRecycler() {
        recyclerView = findViewById(R.id.groupRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        adapter = new UsersGroupAdapter(groupList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new UsersGroupAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                groupList.get(position).setSelected(true);

                startActivity(new Intent(UserCurrentGroupsActivity.this,MembersGroupActivity.class));

                adapter.notifyItemChanged(position);
            }
        });
    }


    private void postCheck() {
        JSONArray userIDArray = new JSONArray();
        JSONObject userID = new JSONObject();
        try {
            userID.put("userID", LoginActivity.userID);
            userIDArray.put(userID);


        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonArrayRequest strReq = new JsonArrayRequest(Request.Method.POST, Const.URL_ISUSERVER + "/groups/forUser/", userIDArray, new Response.Listener<JSONArray>() {

            /**
             * Receives response from server if JSONObject is successfully sent.
             * Requires a proper server connection.
             * @param response Server response in JSONArray format
             */
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<GroupContainer> temps = new ArrayList<>();
                    String name;
                    int totalMembers;
                    int groupID;
                    for (int i = 0; i < response.length(); i++) {
                        name = response.getJSONObject(i).getString("groupName");
                        totalMembers = response.getJSONObject(i).getInt("totalMembers");
                        groupID = response.getJSONObject(i).getInt("id");
                        GroupContainer temp = new GroupContainer(R.drawable.ic_car, name, totalMembers + " Members", groupID);
                        temps.add(temp);


                    }
                    groupList = temps;
                    initRecycler();




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
        AppController.getInstance().addToRequestQueue(strReq, "CheckGroupsAttempt");

    }


}
