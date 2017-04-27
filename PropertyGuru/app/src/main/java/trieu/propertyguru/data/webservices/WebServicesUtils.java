package trieu.propertyguru.data.webservices;

import android.view.View;

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
import trieu.propertyguru.data.model.Item;
import trieu.propertyguru.utils.Utils;

/**
 * Created by Apple on 4/25/17.
 */

public class WebServicesUtils {
    static private CompositeSubscription compositeSubscription = new CompositeSubscription();
    static public void getDetailItem(final String itemId, Subscriber<Item> subscriber, View view){
        if(view != null && view.getTag() != null){
            Subscription subscription = (Subscription) view.getTag();
            compositeSubscription.remove(subscription);
        }
        Subscription subscription = Observable.defer(new Func0<Observable<Item>>() {
            @Override
            public Observable<Item> call() {
                return Observable.create(new Observable.OnSubscribe<Item>() {
                    @Override
                    public void call(Subscriber<? super Item> subscriber) {
                        Call<Item> call = WebServices.Factory.getInstance().getItem(itemId);
                        try {
                            if(!subscriber.isUnsubscribed()) {
                                Response<Item> response = call.execute();
                                if(!subscriber.isUnsubscribed() && response.code() == 200) {
                                    Item item = response.body();
                                    subscriber.onNext(item);
                                }else if(response.code() != 200){
                                    String message = response.raw().message();
                                    throw new IOException(message);
                                }
                            }
                        } catch (IOException e) {
                            if(!subscriber.isUnsubscribed()) {
                                Utils.printException(e);
                            }
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        if(view != null) {
            view.setTag(subscription);
        }
        compositeSubscription.add(subscription);
    }

    static public void clearAll(){
        compositeSubscription.clear();
    }

}
