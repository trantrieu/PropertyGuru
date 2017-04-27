package trieu.propertyguru.home;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import trieu.propertyguru.HackerNewsApplication;
import trieu.propertyguru.R;
import trieu.propertyguru.data.webservices.WebServices;
import trieu.propertyguru.tools.RxSchedulersOverrideRule;
import trieu.propertyguru.tools.Utils;
import trieu.propertyguru.utils.ItemUtils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Apple on 4/27/17.
 */
@RunWith(JUnit4.class)
public class HomeUnitTest {

    //https://github.com/ReactiveX/RxAndroid/issues/238
    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    HackerNewsApplication ticketingApplication;

    @Mock
    MainActivity mainActivity;

    @Mock
    HomeFragment homeFragment;

    HomePresenter homePresenter;

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

        mainActivity = mock(MainActivity.class);
        homeFragment = mock(HomeFragment.class);
        homePresenter = new HomePresenter(homeFragment);

        trieu.propertyguru.utils.Utils.IS_TESTING = true;
    }

    @After
    public void after(){
        WebServices.Factory.stopService();
    }

    //Testing loading list item successful
    @Test
    public void testHome1() throws IOException{
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setBody(Utils.LIST_STR));
        mockWebServer.start();

        trieu.propertyguru.utils.Utils.BASE_URL = mockWebServer.url("").toString();
        WebServices.Factory.stopService();
        homePresenter.getListHackerNews();
        verify(homeFragment).showLoading();
        mockWebServer.shutdown();
        verify(homeFragment).setData(ItemUtils.getListIndex(Utils.LIST_STR));
    }

    //Testing loading list item failed
    @Test
    public void testHome2() throws IOException{
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setResponseCode(400).setBody("Error"));
        mockWebServer.start();

        trieu.propertyguru.utils.Utils.BASE_URL = mockWebServer.url("").toString();
        WebServices.Factory.stopService();
        homePresenter.getListHackerNews();
        verify(homeFragment).showLoading();
        verify(homeFragment).showError("Client Error");

        mockWebServer.shutdown();
    }
}
