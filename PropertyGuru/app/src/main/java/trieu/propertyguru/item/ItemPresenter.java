package trieu.propertyguru.item;

import android.content.Intent;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import trieu.propertyguru.BuildConfig;
import trieu.propertyguru.data.model.Item;
import trieu.propertyguru.data.webservices.WebServices;
import trieu.propertyguru.utils.Utils;
import trieu.propertyguru.utils.espresso.EspressoIdlingResource;

/**
 * Created by Apple on 4/24/17.
 */

public class ItemPresenter implements ItemContract.UserAction {
    private ItemContract.View view;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    public ItemPresenter(ItemContract.View view) {
        this.view = view;
    }

    @Override
    public void deattach() {
        view = null;
        compositeSubscription.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void getDetailItems(final Item parentItem) {
        if(parentItem.getKids() == null || parentItem.getKids().isEmpty()){
            return;
        }

        if(BuildConfig.DEBUG && Utils.IS_TESTING){
            EspressoIdlingResource.increment();
        }

        Subscription subscription = Observable.defer(new Func0<Observable<Item>>() {
            @Override
            public Observable<Item> call() {
                return Observable.create(new Observable.OnSubscribe<Item>() {
                    @Override
                    public void call(Subscriber<? super Item> subscriber) {
                        for(int i = 0 ; i < parentItem.getKids().size() ; i++){
                            Call<Item> call = WebServices.Factory.getInstance().getItem(parentItem.getKids().get(i)+"");
                            if(!subscriber.isUnsubscribed()){
                                try {
                                    Response<Item> response = call.execute();
                                    if(!subscriber.isUnsubscribed() && response.code() == 200) {
                                        Item item = response.body();
                                        if(item != null) {
                                            item.setLevel(parentItem.getLevel() + 1);
                                            subscriber.onNext(item);
                                        }else{
                                            break;
                                        }
                                    }else if(response.code() != 200){
                                        String message = response.raw().message();
                                        subscriber.onError(new IOException(message));
                                        break;
                                    }
                                } catch (Exception e) {
                                    if(!subscriber.isUnsubscribed()) {
                                        Utils.printException(e);
                                    }else {
                                        subscriber.onError(e);
                                        break;
                                    }
                                }
                            }
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<Item>() {
            @Override
            public void onCompleted() {
                if(BuildConfig.DEBUG && Utils.IS_TESTING) {
                    EspressoIdlingResource.decrement();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if(view != null) {
                    view.showError(e.getMessage());
                }

                if(BuildConfig.DEBUG && Utils.IS_TESTING) {
                    EspressoIdlingResource.decrement();
                }
            }

            @Override
            public void onNext(Item item) {
                if(view != null){
                    if(!item.isDeleted()) {
                        view.showData(parentItem, item);
                    }
                }
            }
        });
        
        compositeSubscription.add(subscription);
    }

}
