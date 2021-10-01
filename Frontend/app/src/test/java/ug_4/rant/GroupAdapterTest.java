package ug_4.rant;


import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class GroupAdapterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    public void getListSizeTest() {
        GroupContainer container = mock(GroupContainer.class);


        ArrayList<GroupContainer> list = new ArrayList<>();
        Random r = new Random();
        int num = r.nextInt(20);
        if (num == 0) {
            num = 1;
        }
        for (int i = 1; i < num; i++) {
            list.add(container);
        }

        GroupAdapter adapter = new GroupAdapter(list); // this is the class that is tested
        Assert.assertEquals(num - 1, adapter.getItemCount());

    }

    @Test
    public void getGroupIdTest() {
        GroupContainer container = mock(GroupContainer.class);
        int rand = new Random().nextInt(20);
        when(container.getGroupID()).thenReturn(rand);


        ArrayList<GroupContainer> list = new ArrayList<>();
        Random r = new Random();
        int num = r.nextInt(20);
        if (num == 0) {
            num = 1;
        }
        for (int i = 1; i < num; i++) {
            list.add(container);
        }

        GroupAdapter adapter = new GroupAdapter(list); // class that im testing

        for (int i = 0; i < num - 1; i++) {
            Assert.assertEquals(list.get(i).getGroupID(), adapter.getGroupIdInList(i));
        }

    }

    @Test
    public void getGroupNameTest() {
        GroupContainer container = mock(GroupContainer.class);
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        when(container.getGroupName()).thenReturn(saltStr);


        ArrayList<GroupContainer> list = new ArrayList<>();
        Random r = new Random();
        int num = r.nextInt(20);
        if (num == 0) {
            num = 1;
        }
        for (int i = 1; i < num; i++) {
            list.add(container);
        }

        GroupAdapter adapter = new GroupAdapter(list); // class that im testing

        for (int i = 0; i < num - 1; i++) {
            Assert.assertEquals(list.get(i).getGroupName(), adapter.getGroupNameInList(i));
        }

    }

    @Test/*DEMO 4*/
    public void getMemberIDTest() {
        MemberContainer container = mock(MemberContainer.class);
        int rand = new Random().nextInt(20);
        when(container.getMemberID()).thenReturn(rand);


        ArrayList<MemberContainer> list = new ArrayList<>();
        Random r = new Random();
        int num = r.nextInt(20);
        if (num == 0) {
            num = 1;
        }
        for (int i = 1; i < num; i++) {
            list.add(container);
        }

        MemberAdapter adapter = new MemberAdapter(list); // class that im testing

        for (int i = 0; i < num - 1; i++) {
            Assert.assertEquals(list.get(i).getMemberID(), adapter.getMemberIdInList(i));
        }

    }

    @Test/*DEMO 4*/
    public void getMemberEmailTest() {
       MemberContainer container = mock(MemberContainer.class);
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        when(container.getMemberEmail()).thenReturn(saltStr);


        ArrayList<MemberContainer> list = new ArrayList<>();
        Random r = new Random();
        int num = r.nextInt(20);
        if (num == 0) {
            num = 1;
        }
        for (int i = 1; i < num; i++) {
            list.add(container);
        }

        MemberAdapter adapter = new MemberAdapter(list); // class that im testing

        for (int i = 0; i < num - 1; i++) {
            Assert.assertEquals(list.get(i).getMemberEmail(), adapter.getMemberEmailInList(i));
        }

    }




}