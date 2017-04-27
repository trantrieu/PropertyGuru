package trieu.propertyguru.base.presenter;

import android.content.Intent;

/**
 * Created by Apple on 4/24/17.
 */

public interface BaseActionUserInterface {

    void deattach();
    void onActivityResult(int requestCode, int resultCode, Intent data);

}
