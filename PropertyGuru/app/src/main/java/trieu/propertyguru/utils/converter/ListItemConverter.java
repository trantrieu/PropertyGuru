package trieu.propertyguru.utils.converter;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Apple on 4/27/17.
 */

public class ListItemConverter extends Converter.Factory{

    public ListItemConverter() {
        super();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return CustomConverter.INSTANCE;
    }

    final static class CustomConverter implements Converter<ResponseBody, JSONArray> {
        static final CustomConverter INSTANCE = new CustomConverter();

        @Override
        public JSONArray convert(ResponseBody responseBody) throws IOException {
            try {
                return new JSONArray(responseBody.string());
            } catch (JSONException e) {
                throw new IOException("Failed to parse JSON", e);
            }
        }
    }
}
