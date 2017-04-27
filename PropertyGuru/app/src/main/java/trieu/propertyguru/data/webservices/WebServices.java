package trieu.propertyguru.data.webservices;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import trieu.propertyguru.data.model.Item;
import trieu.propertyguru.utils.Utils;
import trieu.propertyguru.utils.httpclient.CustomOkHttp;

/**
 * Created by Apple on 4/25/17.
 */

public interface WebServices {

    @GET("topstories.json")
    Observable<String> listItemIndex();

    @GET("item/{id}.json")
    Call<Item> getItem(@Path("id") String itemId);

    class Factory {
        static private WebServices instance;
        static private Retrofit retrofit;
        static private OkHttpClient okHttpClient;

        static public OkHttpClient getOkHttpClient(boolean isDisconnected, boolean isDisableCheckingInternet, boolean isBadNetwork){
            if(okHttpClient == null){
                CustomOkHttp customOkHttp = CustomOkHttp.getInstance();
                if(isDisconnected){
                    customOkHttp.disconnected();
                }
                if(isDisableCheckingInternet){
                    customOkHttp.disableCheckingInternet();
                }
                if(isBadNetwork){
                    customOkHttp.badNetwork();
                }
                okHttpClient = customOkHttp.getCustomOkHttp();
            }
            return okHttpClient;
        }

        static public OkHttpClient getOkHttpClient(){
            if(okHttpClient == null){
                okHttpClient = CustomOkHttp.getInstance().getCustomOkHttp();
            }
            return okHttpClient;
        }

        static private WebServices create() {
            retrofit = new Retrofit.Builder()
                    .client(getOkHttpClient())
                    .baseUrl(Utils.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(WebServices.class);
        }

        static public WebServices getInstance(){
            if(instance == null){
                instance = create();
            }
            return instance;
        }

        static public void stopService(){
            okHttpClient = null;
            instance = null;
            retrofit = null;
            CustomOkHttp.stop();
        }
    }
}
