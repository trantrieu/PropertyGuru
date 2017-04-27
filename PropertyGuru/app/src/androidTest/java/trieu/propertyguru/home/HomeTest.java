package trieu.propertyguru.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.OkHttpClient;
import trieu.propertyguru.R;
import trieu.propertyguru.data.model.Item;
import trieu.propertyguru.data.webservices.WebServices;
import trieu.propertyguru.tools.Utils;
import trieu.propertyguru.tools.client.OkHttp3IdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static trieu.propertyguru.tools.Utils.childAtPosition;
import static trieu.propertyguru.tools.Utils.getCurrentActivity;
import static trieu.propertyguru.tools.Utils.rotateToLandscape;
import static trieu.propertyguru.tools.Utils.rotateToPortrait;
import static trieu.propertyguru.tools.Utils.withBold;
import static trieu.propertyguru.tools.Utils.withTextColor;
import static trieu.propertyguru.tools.recycleview.RecyclerViewMatcher.withRecyclerView;

/**
 * Created by Apple on 4/26/17.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeTest {

    OkHttp3IdlingResource okHttp3IdlingResource = null;
    @Rule
    public ActivityTestRule mainActivityRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Before
    public void before(){
        registerNewOkHttpClient(false, false, false);
    }

    @After
    public void after(){
        unRegisterOKHttpClient();
    }

    private void unRegisterOKHttpClient(){
        WebServices.Factory.stopService();
        unregisterIdlingResources(okHttp3IdlingResource);
        okHttp3IdlingResource = null;
    }

    private void registerNewOkHttpClient(boolean isDisconnected, boolean isDisableCheckingInternet, boolean isBadNetwork){
        WebServices.Factory.stopService();
        if(okHttp3IdlingResource != null) {
            unregisterIdlingResources(okHttp3IdlingResource);
        }
        OkHttpClient okHttpClient = WebServices.Factory.getOkHttpClient(isDisconnected, isDisableCheckingInternet, isBadNetwork);
        okHttp3IdlingResource = OkHttp3IdlingResource.create("Okhttp", okHttpClient);
        registerIdlingResources(okHttp3IdlingResource);
    }

    //Show mainview after loading data successful
    @Test
    public void testHome1(){
        mainActivityRule.launchActivity(new Intent());
        onView(withId(R.id.fragment_home_rv)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_home_info_view)).check(matches(not(isDisplayed())));

        Activity currentActivity = mainActivityRule.getActivity();
        String appBarTitle = currentActivity.getString(R.string.app_name);
        ViewInteraction textView = onView(allOf(withText(appBarTitle),
                        childAtPosition(allOf(withId(R.id.action_bar), childAtPosition(withId(R.id.action_bar_container), 0)), 0), isDisplayed()));
        textView.check(matches(withText(appBarTitle)));
    }

    //Show infoview error when no internet
    @Test
    public void testHome2(){
        registerNewOkHttpClient(true, false, false);

        mainActivityRule.launchActivity(new Intent());

        onView(withId(R.id.fragment_home_info_view)).check(matches(isDisplayed()));
        onView(withId(R.id.information_layout_tv_error)).check(matches(isDisplayed()));
        onView(withId(R.id.information_layout_tv_error)).check(matches(withText(R.string.error_no_internet)));
    }

    //Show each item and testing 10 items
    @Test
    public void testHome3(){
        mainActivityRule.launchActivity(new Intent());

        Activity currentActivity = mainActivityRule.getActivity();
        RecyclerView recyclerView = (RecyclerView) currentActivity.findViewById(R.id.fragment_home_rv);
        ItemAdapter itemAdapter = (ItemAdapter) recyclerView.getAdapter();

        for(int i = 0; i < 10; i ++) {
            int checkingPosition = i;

            //scroll to position {firstPosition}
            onView(withId(R.id.fragment_home_rv)).perform(RecyclerViewActions.scrollToPosition(checkingPosition + 1));
            onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_info_view)).check(matches(not(isDisplayed())));
            onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_index)).check(matches(isDisplayed()));
            onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_title)).check(matches(isDisplayed()));
            onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_url)).check(matches(isDisplayed()));
            onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_description)).check(matches(isDisplayed()));
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_title))
                        .check(matches(withTextColor(currentActivity.getColor(R.color.black))));
            }else{
                onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_title))
                        .check(matches(withTextColor(currentActivity.getResources().getColor(R.color.black))));
            }

            onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_title)).check(matches(withBold()));

            Item item = itemAdapter.getItemAt(checkingPosition);

            onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_index)).check(matches(withText((checkingPosition + 1)+"")));
            onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_title)).check(matches(withText(item.getTitle())));
            if(item.getUrl() != null) {
                onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_url)).check(matches(withText(item.getUrl())));
            }else{
                onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_url)).check(matches(withText("")));
            }
            String description = Item.getDescription(currentActivity, item);
            onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_description)).check(matches(withText(description)));

        }
    }

    //Testing error loading total item then loading successful again
    @Test
    public void testHome4(){
        registerNewOkHttpClient(true, false, false);
        mainActivityRule.launchActivity(new Intent());

        onView(withId(R.id.fragment_home_info_view)).check(matches(isDisplayed()));
        onView(withId(R.id.information_layout_tv_error)).check(matches(isDisplayed()));
        onView(withId(R.id.information_layout_tv_error)).check(matches(withText(R.string.error_no_internet)));

        registerNewOkHttpClient(false, false, false);
        onView(withId(R.id.information_layout_button_retry)).perform(click());

        onView(withId(R.id.fragment_home_rv)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_home_info_view)).check(matches(not(isDisplayed())));
    }

    //Checking no internet and showing info view
    @Test
    public void testHome5(){
        mainActivityRule.launchActivity(new Intent());

        int checkingPosition = 8;
        registerNewOkHttpClient(true, false, false);

        onView(withId(R.id.fragment_home_rv)).perform(RecyclerViewActions.scrollToPosition(checkingPosition + 1));
        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_info_view)).check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.information_layout_tv_msg)).check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.information_layout_tv_msg)).check(matches(withText(R.string.error_no_internet)));
    }


    @Test
    public void testHome6(){
        mainActivityRule.launchActivity(new Intent());

        int checkingPosition = 8;
        registerNewOkHttpClient(true, false, false);

        onView(withId(R.id.fragment_home_rv)).perform(RecyclerViewActions.scrollToPosition(checkingPosition + 1));
        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_info_view)).check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.information_layout_tv_msg)).check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.information_layout_tv_msg)).check(matches(withText(R.string.error_no_internet)));

        onView(withId(R.id.fragment_home_rv)).perform(RecyclerViewActions.scrollToPosition(0));
        registerNewOkHttpClient(false, false, false);

        scrollToPosition(checkingPosition + 1);

        Activity currentActivity = getCurrentActivity();
        RecyclerView recyclerView = (RecyclerView) currentActivity.findViewById(R.id.fragment_home_rv);
        ItemAdapter itemAdapter = (ItemAdapter) recyclerView.getAdapter();
        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_info_view)).check(matches(not(isDisplayed())));

        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_index)).check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_title)).check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_url)).check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_description)).check(matches(isDisplayed()));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_title))
                    .check(matches(withTextColor(currentActivity.getColor(R.color.black))));
        }else{
            onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_title))
                    .check(matches(withTextColor(currentActivity.getResources().getColor(R.color.black))));
        }
        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_title)).check(matches(withBold()));
        Item item = itemAdapter.getItemAt(checkingPosition);
        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_index)).check(matches(withText((checkingPosition + 1)+"")));
        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_title)).check(matches(withText(item.getTitle())));
        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_url)).check(matches(withText(item.getUrl())));
        String description = Item.getDescription(currentActivity, item);
        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_description)).check(matches(withText(description)));
    }

    //Testing rotation at position 0
    @Test
    public void testHome7(){
        mainActivityRule.launchActivity(new Intent());
        Activity activity = mainActivityRule.getActivity();

        int checkingPosition = 0;
        onView(withId(R.id.fragment_home_rv)).perform(RecyclerViewActions.scrollToPosition(checkingPosition));
        rotateToLandscape(activity);

        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_index)).check(matches(isDisplayed()));
        rotateToPortrait(activity);
        onView(withRecyclerView(R.id.fragment_home_rv).atPositionOnView(checkingPosition, R.id.layout_item_index)).check(matches(isDisplayed()));

        registerNewOkHttpClient(true, false, false);
        onView(withId(R.id.fragment_home_swipe))
                .perform(Utils.swipeDown(swipeDown(), isDisplayingAtLeast(85)));

        onView(withId(R.id.fragment_home_info_view)).check(matches(isDisplayed()));
        rotateToLandscape(activity);
        onView(withId(R.id.fragment_home_info_view)).check(matches(isDisplayed()));
    }

    //Testing error loading total item then loading successful again by swipedown
    @Test
    public void testHome8(){
        registerNewOkHttpClient(true, false, false);
        mainActivityRule.launchActivity(new Intent());

        onView(withId(R.id.fragment_home_info_view)).check(matches(isDisplayed()));
        onView(withId(R.id.information_layout_tv_error)).check(matches(isDisplayed()));
        onView(withId(R.id.information_layout_tv_error)).check(matches(withText(R.string.error_no_internet)));

        registerNewOkHttpClient(false, false, false);
        onView(withId(R.id.fragment_home_swipe))
                .perform(Utils.swipeDown(swipeDown(), isDisplayingAtLeast(85)));

        onView(withId(R.id.fragment_home_rv)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_home_info_view)).check(matches(not(isDisplayed())));
    }

    /**
     * Need to re scrool to that position again because it will change the size of item
     * -> the position will change
     */
    private void scrollToPosition(int position){
        onView(withId(R.id.fragment_home_rv)).perform(RecyclerViewActions.scrollToPosition(position));
        onView(withId(R.id.fragment_home_rv)).perform(RecyclerViewActions.scrollToPosition(position));
    }


}
