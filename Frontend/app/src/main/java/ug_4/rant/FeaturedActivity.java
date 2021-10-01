package ug_4.rant;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONException;
import ug_4.rant.app.AppController;
import ug_4.rant.net_utils.Const;

public class FeaturedActivity extends Activity {

    private TextView rantTitle1;
    private TextView rantMessage1;
    private TextView rantTitle2;
    private TextView rantMessage2;
    private TextView rantTitle3;
    private TextView rantMessage3;
    private TextView rantTitle4;
    private TextView rantMessage4;
    private TextView rantTitle5;
    private TextView rantMessage5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.featured);

        rantTitle1 = (TextView) findViewById(R.id.rantTitle1);
        rantMessage1 = (TextView) findViewById(R.id.rantMessage1);

        rantTitle2 = (TextView) findViewById(R.id.rantTitle2);
        rantMessage2 = (TextView) findViewById(R.id.rantMessage2);

        rantTitle3 = (TextView) findViewById(R.id.rantTitle3);
        rantMessage3 = (TextView) findViewById(R.id.rantMessage3);

        rantTitle4 = (TextView) findViewById(R.id.rantTitle4);
        rantMessage4 = (TextView) findViewById(R.id.rantMessage4);

        rantTitle5 = (TextView) findViewById(R.id.rantTitle5);
        rantMessage5 = (TextView) findViewById(R.id.rantMessage5);

        getFeatured();
    }

    private void getFeatured() {
        JsonArrayRequest req = new JsonArrayRequest(Const.URL_ISUSERVER + "/rants/mostLiked", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Response", response.toString());
                try {
                    rantTitle1.setText(response.getJSONObject(0).getString("title"));
                    rantMessage1.setText(response.getJSONObject(0).getString("rant"));

                    rantTitle2.setText(response.getJSONObject(1).getString("title"));
                    rantMessage2.setText(response.getJSONObject(1).getString("rant"));

                    rantTitle3.setText(response.getJSONObject(2).getString("title"));
                    rantMessage3.setText(response.getJSONObject(2).getString("rant"));

                    rantTitle4.setText(response.getJSONObject(3).getString("title"));
                    rantMessage4.setText(response.getJSONObject(3).getString("rant"));

                    rantTitle5.setText(response.getJSONObject(4).getString("title"));
                    rantMessage5.setText(response.getJSONObject(4).getString("rant"));
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
