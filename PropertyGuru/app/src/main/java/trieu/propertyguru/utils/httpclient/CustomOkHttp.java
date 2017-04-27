package trieu.propertyguru.utils.httpclient;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import trieu.propertyguru.BuildConfig;
import trieu.propertyguru.HackerNewsApplication;
import trieu.propertyguru.R;
import trieu.propertyguru.utils.Utils;

import static trieu.propertyguru.utils.httpclient.BadNetwork.DELAY;
import static trieu.propertyguru.utils.httpclient.BadNetwork.PERCENT_FAILURE;

/**
 * Created by Apple on 2/6/17.
 */

public class CustomOkHttp {
    static public final int ERROR_NO_WIFI = 1999;
    static public final int ERROR_BAD_NETWORK = ERROR_NO_WIFI + 1;

    static private CustomOkHttp instance;

    static public CustomOkHttp getInstance(){
        if(instance == null){
            instance = new CustomOkHttp();
        }
        return instance;
    }

    private boolean badNetwork;
    private boolean disconnected;
    private boolean checkingInternet = true;
    public OkHttpClient getCustomOkHttp(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        final Context context = HackerNewsApplication.getApplication();
        if(disconnected){
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    String msg = context.getString(R.string.error_no_internet);
                    return new Response.Builder()
                            .code(ERROR_NO_WIFI)
                            .message(msg)
                            .protocol(Protocol.HTTP_1_1)
                            .request(chain.request())
                            .body(ResponseBody.create(MediaType.parse("text/plain"), msg))
                            .build();
                }
            });
        }

        if(badNetwork && BuildConfig.DEBUG){
            builder.addInterceptor(new Interceptor() {
                @Override public Response intercept(Chain chain) throws IOException {
                    BadNetwork badNetwork = BadNetwork.create(PERCENT_FAILURE, DELAY);
                    try {
                        Thread.sleep(badNetwork.getDelay());
                    } catch (InterruptedException e) {
                        Utils.printException(e);
                    }
                    if (badNetwork.isFailure()) {
                        String msg = context.getString(R.string.error_bad_network);
                        return new Response.Builder()
                                .code(ERROR_BAD_NETWORK)
                                .message(msg)
                                .protocol(Protocol.HTTP_1_1)
                                .request(chain.request())
                                .body(ResponseBody.create(MediaType.parse("text/plain"), msg))
                                .build();
                    }
                    return chain.proceed(chain.request());
                }
            });
        }

        //Handle when no internet connection
        if(checkingInternet) {
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    if (!Utils.isWifiConnected()) {
                        String msg = context.getString(R.string.error_no_internet);
                        return new Response.Builder()
                                .code(ERROR_NO_WIFI)
                                .message(msg)
                                .protocol(Protocol.HTTP_1_1)
                                .request(chain.request())
                                .body(ResponseBody.create(MediaType.parse("text/plain"), msg))
                                .build();
                    }
                    return chain.proceed(chain.request());
                }
            });
        }

        return builder.build();
    }

    public CustomOkHttp disconnected(){
        disconnected = true;
        return this;
    }

    public CustomOkHttp badNetwork(){
        badNetwork = true;
        return this;
    }

    public CustomOkHttp disableCheckingInternet(){
        checkingInternet = false;
        return this;
    }

    static public void stop(){
        instance = null;
    }
}
