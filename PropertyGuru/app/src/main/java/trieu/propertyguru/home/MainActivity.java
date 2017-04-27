package trieu.propertyguru.home;

import android.os.Bundle;

import trieu.propertyguru.R;
import trieu.propertyguru.base.view.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomeFragment homeFragment = HomeFragment.getInstance();
        replaceFragment(R.id.activity_main_root, homeFragment);
    }
}
