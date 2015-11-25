package rf.digitworld.weatherapp.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Query;
import rf.digitworld.weatherapp.Utils.StringConverter;

/**
 * Created by Дмитрий on 24.11.2015.
 */
public class RestClient {
    private static RestAPI restAPI;

    public static RestAPI getApiClient() {
        if (restAPI == null) {


            OkHttpClient client = new OkHttpClient();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(RestAPI.BASE_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient(client))
                    .setConverter(new StringConverter())
                    .build();
            restAPI = restAdapter.create(RestAPI.class);
        }
        return restAPI;
    }

    public interface RestAPI {
        String BASE_URL = "http://api.openweathermap.org/data/2.5";
        String FORECAST = "/forecast";
        String PATH="?APPID=2ba163ce87da7a7a4c31494a66103631&lang=ru&units=metric";


        @GET(FORECAST+PATH)
        void getWeather(@Query("id") long cityId, Callback<String> weather);

        @GET(FORECAST+PATH)
        void getWeather(@Query("lat") double lat,@Query("lon") double lon, Callback<String> weather);
    }
}
