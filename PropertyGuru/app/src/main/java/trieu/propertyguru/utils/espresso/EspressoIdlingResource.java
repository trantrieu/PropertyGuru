package trieu.propertyguru.utils.espresso;

import android.support.test.espresso.IdlingResource;

/**
 * Created by Apple on 4/26/17.
 */

public class EspressoIdlingResource {
    private static final String RESOURCE = "GLOBAL";
    private static SimpleCountingIdlingResource mCountingIdlingResource =
            new SimpleCountingIdlingResource(RESOURCE);

    public static void increment() {
        mCountingIdlingResource.increment();
    }

    public static void decrement() {
        mCountingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return mCountingIdlingResource;
    }

    static public void resetIdling(){
        mCountingIdlingResource = new SimpleCountingIdlingResource(RESOURCE);
    }

}
