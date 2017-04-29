package trieu.propertyguru.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import com.orhanobut.logger.Logger;

import java.util.Random;

import trieu.propertyguru.BuildConfig;
import trieu.propertyguru.HackerNewsApplication;
import trieu.propertyguru.data.model.Item;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

/**
 * Created by Apple on 4/24/17.
 */

public class Utils {
    static public String BASE_URL = BuildConfig.BASE_URL;
    static public final Random RANDOM = new Random();
    static public boolean IS_TESTING = false;
    static public void printException(Exception e){
        if(BuildConfig.DEBUG && IS_TESTING == false){
            Logger.e(e, "Error");
        }
    }

    static public void printLog(String message){
        if(BuildConfig.DEBUG){
            Logger.e(message);
        }
    }

    static public Spanned getTextCommentHtml(Item item){
        if(item.getText() == null || item.getText().isEmpty()){
            return null;
        }
        String text = item.getText();
        //Work around fixed there is automatic add new line at the end
        text = text.replace("<p>", "<br><br>");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, FROM_HTML_MODE_LEGACY);
        }else{
            return Html.fromHtml(text);
        }
    }


    static public boolean isWifiConnected(){
        ConnectivityManager cm = (ConnectivityManager) HackerNewsApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    return true;
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return true;
                }
            }
        }
        return false;
    }

}
