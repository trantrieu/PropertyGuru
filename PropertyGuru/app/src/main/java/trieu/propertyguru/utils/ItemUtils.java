package trieu.propertyguru.utils;

import android.content.Context;

import trieu.propertyguru.R;
import trieu.propertyguru.data.model.Item;

/**
 * Created by Apple on 4/25/17.
 */

public class ItemUtils {

    static public String[] getListIndex(String str){
        str = str.replace("[", "").replace("]", "");
        return str.split(",");
    }

    static public String getByTime(Item item, Context context){
        long currentTime = System.currentTimeMillis();
        long d = currentTime - item.getTime() * 1000;
        long seconds = d/1000;
        int days = (int)(seconds/(24*3600));
        String str = "";
        if(days > 0){
            str = days +" "+context.getString(R.string.day)+" "+context.getString(R.string.ago);
        }else{
            int hours = (int)(seconds/3600);
            if(hours > 0){
                str = hours +" "+context.getString(R.string.hour)+" "+context.getString(R.string.ago);
            }else{
                int minutes = (int)(seconds/60);
                if(minutes > 0){
                    str = minutes + " "+context.getString(R.string.minute)+" "+context.getString(R.string.ago);
                }else{
                    str = seconds + " "+context.getString(R.string.second) +" "+context.getString(R.string.ago);
                }
            }
        }
        return str;
    }
}
