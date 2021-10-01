package ug_4.rant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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


public class MembersGroupActivity extends Activity {
    private RecyclerView recyclerView;
    private MemberAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public static ArrayList<MemberContainer> memberList = new ArrayList<>();
    private boolean flag;
    private TextView groupName, groupInfo;
    private TextView inviteMembers;
    public static int groupIDForPost;
    public static String groupNameHolder, groupInfoHolder;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_group_members);

        groupName = (TextView) findViewById(R.id.group_title_members);
        groupInfo = (TextView) findViewById(R.id.group_info_members);
        inviteMembers = (TextView) findViewById(R.id.inviteMembers);
        inviteMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MembersGroupActivity.this, InviteMembersActivity.class));
                finish();


            }
        });

        for (GroupContainer c : UserCurrentGroupsActivity.groupList) {
            if (c.isSelected()) {
                groupInfoHolder = c.getGroupInfo();
                groupNameHolder = c.getGroupName();
                groupName.setText(groupNameHolder);
                groupInfo.setText(groupInfoHolder);
                groupIDForPost = c.getGroupID();
                c.setSelected(false);
            }
        }

        postCheck();


    }

    private void initRecycler() {
        recyclerView = findViewById(R.id.groupMembersRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        adapter = new MemberAdapter(memberList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


    }


    private void postCheck() {
        JSONArray groupIDArray = new JSONArray();
        JSONObject groupID = new JSONObject();
        try {
            groupID.put("groupID", groupIDForPost);
            groupIDArray.put(groupID);


        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonArrayRequest strReq = new JsonArrayRequest(Request.Method.POST, Const.URL_ISUSERVER + "/groups/getUsers/", groupIDArray, new Response.Listener<JSONArray>() {

            /**
             * Receives response from server if JSONObject is successfully sent.
             * Requires a proper server connection.
             * @param response Server response in JSONArray format
             */
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<MemberContainer> temps = new ArrayList<>();
                    String email, role;
                    int userID;

                    for (int i = 0; i < response.length(); i++) {
                        email = response.getJSONObject(i).getString("email");

                        role = response.getJSONObject(i).getString("role");
                        userID = response.getJSONObject(i).getInt("userID");
                        MemberContainer temp = new MemberContainer(email, role, userID);
                        temps.add(temp);


                    }
                    memberList = temps;
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
