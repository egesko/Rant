package ug_4.rant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * <h1>Rant Main Activity</h1>
 * The MainActivity program implements an application that
 * acts as the main screen for the Rant project. Users can
 * navigate different parts of the app starting from this screen.
 * Implements functionality from MainActivity.xml
 *
 * Supports Volley Integration
 *
 *
 * @author  Damandeep Riat
 * @version 1.0
 * @since   2021-03-14
 */
public class MainActivity extends Activity implements OnClickListener {

    private Button btnNewAccount, btnLogin;

    /**
     *Creates and initializes the MainActivity screen.
     * All design buttons and views are initialized
     * and set appropriate listeners.
     * @param savedInstanceState Stores Dynamic data once screen is closed.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNewAccount = (Button) findViewById(R.id.newAccountButton);
        btnLogin = (Button) findViewById(R.id.loginButton);

        btnNewAccount.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    /**
     * Determines which button the user clicks on main screen
     * and directs user to the new screen. Starts the new screen.
     *
     * @param v Holds all the components of the current screen in a saved state.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newAccountButton:
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
            case R.id.loginButton:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            default:
                break;
        }
    }
}