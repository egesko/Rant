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
public class RegisterTest {

    @Rule   // needed to launch the activity
    public ActivityTestRule<RegisterActivity> activityRule = new ActivityTestRule<>(RegisterActivity.class);

    @Test
    public void emailAlreadyExists() {
        // Type in testString and send request
        onView(withId(R.id.fullNameEditText)).perform(typeText("John Doe"), closeSoftKeyboard());
        onView(withId(R.id.emailEditText)).perform(typeText("johnd@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.signupButton)).perform(click());

        onView(withId(R.id.emailEditText)).check(matches(withText(endsWith(""))));
    }

    @Test
    public void invalidName() {
        // Type in testString and send request
        onView(withId(R.id.fullNameEditText)).perform(typeText("John23 Doe"), closeSoftKeyboard());
        onView(withId(R.id.emailEditText)).perform(typeText("johnd@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.signupButton)).perform(click());

        onView(withId(R.id.fullNameEditText)).check(matches(withText(endsWith(""))));
    }

    @Test
    public void invalidEmail() {
        // Type in testString and send request
        onView(withId(R.id.fullNameEditText)).perform(typeText("John Doe"), closeSoftKeyboard());
        onView(withId(R.id.emailEditText)).perform(typeText("johnd@gmail"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.signupButton)).perform(click());

        onView(withId(R.id.emailEditText)).check(matches(withText(endsWith(""))));
    }

    @Test
    public void invalidPassword() {
        // Type in testString and send request
        onView(withId(R.id.fullNameEditText)).perform(typeText("John Doe"), closeSoftKeyboard());
        onView(withId(R.id.emailEditText)).perform(typeText("johnd@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(typeText("saddf dsf"), closeSoftKeyboard());
        onView(withId(R.id.signupButton)).perform(click());

        onView(withId(R.id.passwordEditText)).check(matches(withText(endsWith(""))));
    }
}