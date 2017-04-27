package trieu.propertyguru.home;

import android.content.Intent;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import trieu.propertyguru.R;
import trieu.propertyguru.base.view.BaseActivity;
import trieu.propertyguru.data.model.Item;
import trieu.propertyguru.data.webservices.WebServices;
import trieu.propertyguru.item.ItemFragment;
import trieu.propertyguru.utils.ItemUtils;

import static trieu.propertyguru.utils.httpclient.CustomOkHttp.ERROR_NO_WIFI;

/**
 * Created by Apple on 4/24/17.
 */

public class HomePresenter implements HomeContract.UserAction{

    private HomeContract.View view;
    private CompositeSubscription compositeSubscription;
    public HomePresenter(HomeContract.View view) {
        this.view = view;
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void deattach() {
        view = null;
        if(compositeSubscription != null){
            compositeSubscription.unsubscribe();
            compositeSubscription = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void getListHackerNews() {
        view.showLoading();
        Subscription subscription = WebServices.Factory.getInstance().listItemIndex()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        String error = e.getMessage();
                        if(e instanceof HttpException){
                            error = error.replace("HTTP "+ERROR_NO_WIFI+" ", "");
                        }
                        viewShowError(error);
                    }

                    @Override
                    public void onNext(String responseBody) {
                        if(view != null) {
                            String[] arrayItem = ItemUtils.getListIndex(responseBody);
                            view.setData(arrayItem);
                        }
                    }
                });
        compositeSubscription.add(subscription);
    }

    @Override
    public void gotoPageDetail(Item item) {
        if(item != null) {
            ItemFragment itemFragment = ItemFragment.getInstance(item);
            BaseActivity baseActivity = (BaseActivity) view.getCContext();
            baseActivity.replaceFragment(R.id.activity_main_root, itemFragment);
        }
    }

    private void viewShowError(String error){
        if(view != null){
            view.showError(error);
        }
    }

}
