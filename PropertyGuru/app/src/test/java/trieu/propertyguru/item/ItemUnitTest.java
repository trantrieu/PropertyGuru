package trieu.propertyguru.item;

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
import retrofit2.Call;
import retrofit2.Response;
import trieu.propertyguru.HackerNewsApplication;
import trieu.propertyguru.R;
import trieu.propertyguru.data.model.Item;
import trieu.propertyguru.data.webservices.WebServices;
import trieu.propertyguru.home.MainActivity;
import trieu.propertyguru.tools.RxSchedulersOverrideRule;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static trieu.propertyguru.tools.Utils.REPLY_STR_1;
import static trieu.propertyguru.tools.Utils.REPLY_STR_2;
import static trieu.propertyguru.tools.Utils.getItemCommentDefault;
import static trieu.propertyguru.tools.Utils.getItemReplyDefault1;
import static trieu.propertyguru.tools.Utils.getItemReplyDefault2;

/**
 * Created by Apple on 4/27/17.
 */

@RunWith(JUnit4.class)
public class ItemUnitTest {

    //https://github.com/ReactiveX/RxAndroid/issues/238
    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    HackerNewsApplication ticketingApplication;

    @Mock
    MainActivity mainActivity;

    @Mock
    ItemFragment itemFragment;

    ItemPresenter itemPresenter;

    @Before
    public void before(){
        ticketingApplication = mock(HackerNewsApplication.class);
        HackerNewsApplication.setApplication(ticketingApplication);
        when(ticketingApplication.getString(R.string.points)).thenReturn("points");
        when(ticketingApplication.getString(R.string.by)).thenReturn("by");
        when(ticketingApplication.getString(R.string.comments)).thenReturn("comment(s)");
        when(ticketingApplication.getString(R.string.day)).thenReturn("day(s)");
        when(ticketingApplication.getString(R.string.hour)).thenReturn("hour(s)");
        when(ticketingApplication.getString(R.string.minute)).thenReturn("minute(s)");
        when(ticketingApplication.getString(R.string.second)).thenReturn("second(s)");
        when(ticketingApplication.getString(R.string.ago)).thenReturn("ago");
        when(ticketingApplication.getString(R.string.error_no_internet)).thenReturn("No internet connection, please check your netword connection");
        mainActivity = mock(MainActivity.class);
        itemFragment = mock(ItemFragment.class);
        itemPresenter = new ItemPresenter(itemFragment);

        trieu.propertyguru.utils.Utils.IS_TESTING = true;
    }

    @After
    public void after(){
        WebServices.Factory.stopService();
    }

    //testing loading comment successful
    @Test
    public void testItem1() throws IOException{
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setBody(REPLY_STR_1));
        mockWebServer.enqueue(new MockResponse().setBody(REPLY_STR_2));
        mockWebServer.start();

        trieu.propertyguru.utils.Utils.BASE_URL = mockWebServer.url("").toString();
        WebServices.Factory.stopService();
        WebServices.Factory.getOkHttpClient(false, true, false);
        Item itemRoot = getItemCommentDefault();
        itemPresenter.getDetailItems(itemRoot);

        Item itemReplay1 = getItemReplyDefault1();
        itemFragment.showData(itemRoot, itemReplay1);

        Item itemReplay2 = getItemReplyDefault2();
        itemFragment.showData(itemRoot, itemReplay2);

        mockWebServer.shutdown();
    }

    //testing loading comment error
    @Test
    public void testItem2() throws IOException{
        MockWebServer mockWebServer = new MockWebServer();
            mockWebServer.enqueue(new MockResponse().setResponseCode(400));
        mockWebServer.start();

        trieu.propertyguru.utils.Utils.BASE_URL = mockWebServer.url("").toString();
        WebServices.Factory.stopService();
        WebServices.Factory.getOkHttpClient(false, true, false);

        Item itemRoot = getItemCommentDefault();
        itemPresenter.getDetailItems(itemRoot);

        itemFragment.showError("Client Error");
        mockWebServer.shutdown();
    }

    //Test response data successful
    @Test
    public void testItem3() throws IOException{
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setBody(REPLY_STR_1));
        mockWebServer.enqueue(new MockResponse().setBody(REPLY_STR_2));
        mockWebServer.start();

        trieu.propertyguru.utils.Utils.BASE_URL = mockWebServer.url("").toString();
        WebServices.Factory.stopService();
        WebServices.Factory.getOkHttpClient(false, true, false);

        Call call = WebServices.Factory.getInstance().getItem("1");
        Response<Item> response = call.execute();
        Item item1 = response.body();

        assertEquals("", getItemReplyDefault1(), item1);

        call = WebServices.Factory.getInstance().getItem("2");
        response = call.execute();
        Item item2 = response.body();

        assertEquals("", getItemReplyDefault2(), item2);
        mockWebServer.shutdown();
    }

    //Testing one item is failed, one is successful
    @Test
    public void testItem4() throws IOException{
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setResponseCode(400));
        mockWebServer.enqueue(new MockResponse().setBody(REPLY_STR_2));
        mockWebServer.start();

        trieu.propertyguru.utils.Utils.BASE_URL = mockWebServer.url("").toString();
        WebServices.Factory.stopService();
        WebServices.Factory.getOkHttpClient(false, true, false);

        Call call = WebServices.Factory.getInstance().getItem("1");
        Response<Item> response = call.execute();
        int code = response.code();
        assertEquals(code, 400);

        call = WebServices.Factory.getInstance().getItem("2");
        response = call.execute();
        Item item2 = response.body();

        assertEquals("", getItemReplyDefault2(), item2);
        mockWebServer.shutdown();
    }

    //Testing loading one successful, one failed
    @Test
    public void testItem5() throws IOException{
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.enqueue(new MockResponse().setBody(REPLY_STR_1));
        mockWebServer.enqueue(new MockResponse().setResponseCode(400));
        mockWebServer.start();

        trieu.propertyguru.utils.Utils.BASE_URL = mockWebServer.url("").toString();
        WebServices.Factory.stopService();
        WebServices.Factory.getOkHttpClient(false, true, false);

        Item itemRoot = getItemCommentDefault();
        itemPresenter.getDetailItems(itemRoot);

        Item item1 = getItemReplyDefault1();
        itemFragment.showData(itemRoot, item1);

        itemFragment.showError("Client Error");
        mockWebServer.shutdown();
    }
}
