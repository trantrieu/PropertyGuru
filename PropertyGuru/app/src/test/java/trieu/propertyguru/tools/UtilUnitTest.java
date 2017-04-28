package trieu.propertyguru.tools;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import trieu.propertyguru.HackerNewsApplication;
import trieu.propertyguru.R;
import trieu.propertyguru.data.model.Item;
import trieu.propertyguru.utils.ItemUtils;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static trieu.propertyguru.tools.Utils.getItemCommentDefault;
import static trieu.propertyguru.tools.Utils.getItemStoryDefault;

/**
 * Created by Apple on 4/27/17.
 */
@RunWith(JUnit4.class)
public class UtilUnitTest {

    @Mock
    HackerNewsApplication ticketingApplication;

    @Before
    public void before(){
        ticketingApplication = mock(HackerNewsApplication.class);
        when(ticketingApplication.getString(R.string.points)).thenReturn("points");
        when(ticketingApplication.getString(R.string.by)).thenReturn("by");
        when(ticketingApplication.getString(R.string.comments)).thenReturn("comment(s)");
        when(ticketingApplication.getString(R.string.day)).thenReturn("day(s)");
        when(ticketingApplication.getString(R.string.hour)).thenReturn("hour(s)");
        when(ticketingApplication.getString(R.string.minute)).thenReturn("minute(s)");
        when(ticketingApplication.getString(R.string.second)).thenReturn("second(s)");
        when(ticketingApplication.getString(R.string.ago)).thenReturn("ago");
        trieu.propertyguru.utils.Utils.IS_TESTING = true;
    }

    @After
    public void after(){

    }

    @Test
    public void testTime(){
        //        Item item = getItemStoryDefault();
        //        String time = ItemUtils.getByTime(item, ticketingApplication);
        //        String expected = "2 hour(s) ago";
        //        assertEquals("", time, expected);
    }

    @Test
    public void testDescription(){
        Item item = getItemStoryDefault();
        String time = ItemUtils.getByTime(item, ticketingApplication);
        String expected = "45 points by wtracy "+time+" | 10 comment(s)";
        String actual = Item.getDescription(ticketingApplication, item);
        assertEquals("", expected, actual);
    }

    @Test
    public void testCommentTitle(){
        Item item = getItemCommentDefault();
        String time = ItemUtils.getByTime(item, ticketingApplication);
        String expected = "ewanm89 "+time;
        String actual = Item.getCommentTitle(ticketingApplication, item);
        assertEquals("", expected, actual);
    }
}
