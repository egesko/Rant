package ug_4.rant;

import android.widget.EditText;
import android.widget.ScrollView;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HomeFunctionalityTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void searchTags() {
        EditText searchBar = mock(EditText.class);
        HomeActivity home = new HomeActivity();

        List<String> list1 = new LinkedList<String>();
        list1.add("Politics");
        list1.add("Education");
        list1.add("Global");
        list1.add("History");

        when(searchBar.toString()).thenReturn("Politics,Education,Global,History");
        Assert.assertEquals(list1, home.getTags(searchBar.toString()));

        List<String> list2 = new LinkedList<String>();
        list2.add("Politics");
        list2.add(" Education");
        list2.add(" Global");
        list2.add(" History");

        when(searchBar.toString()).thenReturn("Politics, Education, Global, History");
        Assert.assertEquals(list2, home.getTags(searchBar.toString()));
    }

    @Test
    public void swipe() {
        ScrollView layout = mock(ScrollView.class);
        HomeActivity home = new HomeActivity();

        when(layout.arrowScroll(1)).thenReturn(true);
        Assert.assertEquals(true, layout.arrowScroll(1));

        when(layout.arrowScroll(2)).thenReturn(true);
        Assert.assertEquals(true, layout.arrowScroll(2));

        when(layout.arrowScroll(3)).thenReturn(true);
        Assert.assertEquals(true, layout.arrowScroll(3));
    }
}
