package ug_4.rant;


import android.widget.EditText;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnterRegisterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    public void enterFullName() {
        EditText container = mock(EditText.class);
        RegisterActivity register = new RegisterActivity();

        when(container.toString()).thenReturn("John Doe");
        Assert.assertEquals(true, register.isValidFullName(container.toString(), false));

        when(container.toString()).thenReturn("John44");
        Assert.assertEquals(false, register.isValidFullName(container.toString(), false));
    }

    @Test
    public void enterEmail() {
        EditText container = mock(EditText.class);
        RegisterActivity register = new RegisterActivity();

        when(container.toString()).thenReturn("johnd@gmail.com");
        Assert.assertEquals(true, register.isValidEmail(container.toString(), false));

        when(container.toString()).thenReturn("john@gmail");
        Assert.assertEquals(false, register.isValidEmail(container.toString(), false));
    }

    @Test
    public void enterPassword() {
        EditText container = mock(EditText.class);
        RegisterActivity register = new RegisterActivity();

        when(container.toString()).thenReturn("password123");
        Assert.assertEquals(true, register.isValidPassword(container.toString(), false));

        when(container.toString()).thenReturn("pass");
        Assert.assertEquals(false, register.isValidPassword(container.toString(), false));
    }
}