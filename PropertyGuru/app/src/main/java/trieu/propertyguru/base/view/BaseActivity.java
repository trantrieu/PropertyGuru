package trieu.propertyguru.base.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Apple on 4/24/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public void addFragment(int containerViewId, BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment, fragment.getTagName());
        fragmentTransaction.addToBackStack("add " + fragment.getTagName());
        fragmentTransaction.commit();
    }

    public void replaceFragment(int containerViewId, BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, fragment.getTagName());
        fragmentTransaction.addToBackStack("replace " + fragment.getTagName());
        fragmentTransaction.commit();
    }

    public void removeFragment(BaseFragment fragment){
        getSupportFragmentManager().popBackStack();
    }

    public void backToFragment(String name){
        getSupportFragmentManager().popBackStack("replace "+name, 0);
    }

    public void backToFragmentInclusive(String name){
        getSupportFragmentManager().popBackStack("replace "+name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public Fragment getLastedFragment(){
        int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backEntry = (FragmentManager.BackStackEntry) getSupportFragmentManager().getBackStackEntryAt(index);
        String tag = backEntry.getName();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        return fragment;
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if(count == 1){
            finish();
        }else {
            super.onBackPressed();
        }
    }
}
