package ug_4.rant;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringEndsWith.endsWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {

    @Rule   // needed to launch the activity
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);



    @Test
    public void invalidEmail() {
        // Type in testString and send request
        onView(withId(R.id.loginEditText)).perform(typeText("damanr@iastate"), closeSoftKeyboard());
        onView(withId(R.id.passEditText)).perform(typeText("damanr"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.loginEditText)).check(matches(withText(endsWith(""))));
    }

    @Test
    public void invalidPassword() {
        // Type in testString and send request
        onView(withId(R.id.loginEditText)).perform(typeText("damanr@iastate.edu"), closeSoftKeyboard());
        onView(withId(R.id.passEditText)).perform(typeText("pas123"), closeSoftKeyboard());
        //onView(withId(R.id.passwordEditText)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.passEditText)).check(matches(withText(endsWith(""))));
    }

    @Test
    public void invalidEmailPassword() {
        // Type in testString and send request
        onView(withId(R.id.loginEditText)).perform(typeText("damanr"), closeSoftKeyboard());
        onView(withId(R.id.passEditText)).perform(typeText("passsss"), closeSoftKeyboard());
        //onView(withId(R.id.passwordEditText)).perform(typeText("saddf "), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.loginEditText)).check(matches(withText(endsWith(""))));
    }
}