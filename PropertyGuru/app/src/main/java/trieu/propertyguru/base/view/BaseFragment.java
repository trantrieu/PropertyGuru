package trieu.propertyguru.base.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;

import trieu.propertyguru.HackerNewsApplication;
import trieu.propertyguru.base.presenter.BaseActionUserInterface;

/**
 * Created by Apple on 4/24/17.
 */

public abstract class BaseFragment extends Fragment {
    private String tagName;
    private BaseActionUserInterface baseActionUserInterface;
    protected abstract BaseActionUserInterface createActionUserInterface();

    protected abstract int getLayoutResouce();
    protected abstract void onCreateView(View view);
    protected BaseActionUserInterface getBaseActionUserInterface(){
        return baseActionUserInterface;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        baseActionUserInterface.deattach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseActionUserInterface = createActionUserInterface();
        View view = inflater.inflate(getLayoutResouce(), container, false);
        onCreateView(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagName = setTagName();
    }

    protected abstract String setTagName();

    public String getTagName(){
        return tagName;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(baseActionUserInterface != null){
            baseActionUserInterface.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = HackerNewsApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
