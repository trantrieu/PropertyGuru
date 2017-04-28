package trieu.propertyguru;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import static trieu.propertyguru.utils.Utils.IS_TESTING;

/**
 * Created by Apple on 4/25/17.
 */

public class HackerNewsApplication extends Application {
    static private HackerNewsApplication application;
    private RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);
        application = this;
    }

    @Override
    public void onTerminate() {
        application = null;
        super.onTerminate();
    }

    static public void setApplication(HackerNewsApplication application){
        if(BuildConfig.DEBUG && IS_TESTING){
            HackerNewsApplication.application = application;
        }
    }

    static public HackerNewsApplication getApplication(){
        return application;
    }

    static public RefWatcher getRefWatcher(Context context) {
        HackerNewsApplication application = (HackerNewsApplication) context.getApplicationContext();
        return application.refWatcher;
    }


}
