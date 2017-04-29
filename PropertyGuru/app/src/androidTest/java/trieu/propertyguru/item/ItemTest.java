package trieu.propertyguru.item;

import android.app.Activity;
import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.OkHttpClient;
import trieu.propertyguru.R;
import trieu.propertyguru.base.view.comment.CommentView;
import trieu.propertyguru.data.model.Item;
import trieu.propertyguru.data.webservices.WebServices;
import trieu.propertyguru.home.ItemAdapter;
import trieu.propertyguru.home.MainActivity;
import trieu.propertyguru.tools.client.OkHttp3IdlingResource;
import trieu.propertyguru.utils.espresso.EspressoIdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static trieu.propertyguru.tools.Utils.childAtPosition;
import static trieu.propertyguru.tools.Utils.rotateToLandscape;
import static trieu.propertyguru.tools.Utils.rotateToPortrait;
import static trieu.propertyguru.tools.Utils.withView;

/**
 * Created by Apple on 4/26/17.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ItemTest {
    OkHttp3IdlingResource okHttp3IdlingResource = null;

    @Rule
    public ActivityTestRule mainActivityRule = new ActivityTestRule<>(MainActivity.class, true, false);

    private void registerNewOkHttpClient(boolean isDisconnected, boolean isDisableCheckingInternet, boolean isBadNetwork){
        WebServices.Factory.stopService();
        if(okHttp3IdlingResource != null) {
            unregisterIdlingResources(okHttp3IdlingResource);
        }
        OkHttpClient okHttpClient = WebServices.Factory.getOkHttpClient(isDisconnected, isDisableCheckingInternet, isBadNetwork);
        okHttp3IdlingResource = OkHttp3IdlingResource.create("Okhttp", okHttpClient);
        registerIdlingResources(okHttp3IdlingResource);
        trieu.propertyguru.utils.Utils.IS_TESTING = true;
    }

    private void unRegisterOKHttpClient(){
        WebServices.Factory.stopService();
        unregisterIdlingResources(okHttp3IdlingResource);
        okHttp3IdlingResource = null;
    }

    @Before
    public void before(){
        registerNewOkHttpClient(false, false, false);
        registerIdlingResources(EspressoIdlingResource.getIdlingResource());
        mainActivityRule.launchActivity(new Intent());
    }

    @After
    public void after(){
        unRegisterOKHttpClient();
        unregisterIdlingResources(EspressoIdlingResource.getIdlingResource());
    }

    //Testing each item is showing in 2 item
    @Test
    public void testItem1(){

        Activity activity = mainActivityRule.getActivity();
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.fragment_home_rv);
        ItemAdapter itemAdapter = (ItemAdapter) recyclerView.getAdapter();

        for(int i = 2 ; i < 3 ; i ++) {
            Item item = itemAdapter.getItemAt(i);
            onView(withId(R.id.fragment_home_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));
            if(item.getKids() != null && item.getKids().size() > 0){
                onView(withId(R.id.fragment_comment_container)).check(matches(isDisplayed()));
                onView(withId(R.id.fragment_comment_info_view)).check(matches(not(isDisplayed())));
            }else{
                onView(withId(R.id.fragment_comment_info_view)).check(matches(isDisplayed()));
                onView(withId(R.id.fragment_comment_container)).check(matches(not(isDisplayed())));
            }


            Activity currentActivity = mainActivityRule.getActivity();
            String appBarTitle = currentActivity.getString(R.string.app_name);
            ViewInteraction textView = onView(allOf(withText(appBarTitle),
                    childAtPosition(allOf(withId(R.id.action_bar), childAtPosition(withId(R.id.action_bar_container), 0)), 0), isDisplayed()));
            textView.check(matches(withText(appBarTitle)));
            pressBack();
        }
    }

    //Start 3 items and check they are displaying (internet failed when go to comment)
    @Test
    public void testItem2(){
        Activity activity = mainActivityRule.getActivity();
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.fragment_home_rv);
        ItemAdapter itemAdapter = (ItemAdapter) recyclerView.getAdapter();

        registerNewOkHttpClient(true, false, false);
        for(int i = 2 ; i < 3 ; i ++) {
            Item item = itemAdapter.getItemAt(i);
            onView(withId(R.id.fragment_home_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));
            onView(withId(R.id.fragment_comment_container)).check(matches(not(isDisplayed())));
            onView(withId(R.id.fragment_comment_info_view)).check(matches(isDisplayed()));

            onView(withId(R.id.information_layout_tv_error)).check(matches(isDisplayed()));

            if(item.getKids() != null && item.getKids().size() > 0) {
                onView(withId(R.id.information_layout_tv_error)).check(matches(withText(activity.getString(R.string.error_no_internet))));
            }else{
                onView(withId(R.id.information_layout_tv_error)).check(matches(withText(activity.getString(R.string.empty_data))));
            }
            pressBack();
        }
    }

    //Pressed on Item null
    @Test
    public void testItem3(){
        registerNewOkHttpClient(true, false, false);

        int i = 7;
        onView(withId(R.id.fragment_home_rv)).perform(RecyclerViewActions.scrollToPosition(i + 1));
        onView(withId(R.id.fragment_home_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));
        onView(withId(R.id.fragment_home_rv)).check(matches(isDisplayed()));
    }

    //Rotation
    @Test
    public void testItem4(){
        Activity activity = mainActivityRule.getActivity();
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.fragment_home_rv);
        ItemAdapter itemAdapter = (ItemAdapter) recyclerView.getAdapter();

        int checkingPosition = 2;
        onView(withId(R.id.fragment_home_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(checkingPosition, click()));

        Item item = itemAdapter.getItemAt(checkingPosition);
        if(item.getKids() != null && item.getKids().size() > 0) {
            onView(withId(R.id.fragment_comment_container)).check(matches(isDisplayed()));
            rotateToLandscape(activity);
            onView(withId(R.id.fragment_comment_container)).check(matches(isDisplayed()));
            rotateToPortrait(activity);
            onView(withId(R.id.fragment_comment_container)).check(matches(isDisplayed()));
        }else{
            onView(withId(R.id.fragment_comment_info_view)).check(matches(isDisplayed()));
            rotateToLandscape(activity);
            onView(withId(R.id.fragment_comment_info_view)).check(matches(isDisplayed()));
            rotateToPortrait(activity);
            onView(withId(R.id.fragment_comment_info_view)).check(matches(isDisplayed()));
        }
    }

    //Test number of comment in detail
    @Test
    public void testItem5(){
        Activity activity = mainActivityRule.getActivity();
        int checkingPosition = 2;
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.fragment_home_rv);
        ItemAdapter itemAdapter = (ItemAdapter) recyclerView.getAdapter();
        Item rootItem = itemAdapter.getItemAt(checkingPosition);
        onView(withId(R.id.fragment_home_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(checkingPosition, click()));
        //onView(withId(R.id.fragment_comment_container)).check(matches(checkSizeCommentViewLevel0(rootItem.getKids().size())));
    }

    //Check display title comment
    @Test
    public void testItem6(){
        Activity activity = mainActivityRule.getActivity();

        int checkingPosition = 2;
        onView(withId(R.id.fragment_home_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(checkingPosition, click()));
        ScrollView scrollView = (ScrollView) activity.findViewById(R.id.fragment_comment_sv);
        LinearLayout root = (LinearLayout) activity.findViewById(R.id.fragment_comment_container);
        //checkDisplayTitleCommentView(activity, root, scrollView);
    }

    private Matcher<View> checkSizeCommentViewLevel0(final int size){
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View input) {
                if(!(input instanceof ViewGroup)){
                    return false;
                }
                ViewGroup viewGroup = (ViewGroup)input;
                int count = 0;
                for(int i = 0 ; i < viewGroup.getChildCount() ; i ++){
                    View child = viewGroup.getChildAt(i);
                    if(child instanceof CommentView){
                        count++;
                    }
                }
                return count == size;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText ("LinearLayout should have " + size + " items CommentView");
            }
        };
    }

    private void checkDisplayTitleCommentView(Activity activity, ViewGroup viewGroup, ScrollView scrollView){
        for(int i = viewGroup.getChildCount() - 1 ; i >= 0 ; i --){
            View view = viewGroup.getChildAt(i);
            if(view instanceof CommentView){
                CommentView commentView = (CommentView)view;
                Item item = commentView.getItem();

                //check display
                scrollView.scrollTo(0, commentView.getTop() - 10);
                String title = Item.getCommentTitle(activity, item);
                onView(allOf(withText(title), withParent(withParent(withView(commentView))))).check(matches(isDisplayed()));
            }
        }
    }
}
