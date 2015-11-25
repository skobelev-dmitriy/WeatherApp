package rf.digitworld.weatherapp.network;

import android.content.Context;
import android.database.Observable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rf.digitworld.weatherapp.models.City;
import rf.digitworld.weatherapp.models.Weather;

/**
 * Created by Дмитрий on 24.11.2015.
 */
public class BackEndCommunicator {
    private final  RequestObservable mObservable=new RequestObservable();
    private boolean mIsWorking;
    Context context;
    public static final String TAG="BackendCommunicator";
    private static  String APPID="2ba163ce87da7a7a4c31494a66103631";
    String lang="ru";
    String units="metric";
    public BackEndCommunicator(){

    }
    public BackEndCommunicator(Context context){
        this.context=context;

    }
    public void registerObserver(final Observer observer ){
        mObservable.registerObserver(observer);
        if(mIsWorking){
            observer.onRequestStarted();
        }
    }
    public void unregisterObserver(final Observer observer ){
        mObservable.unregisterObserver(observer);

    }


    public interface Observer {
        void onRequestStarted();
        void onRequestSucceded();
        void onRequestFailed();
    }

    private class RequestObservable extends Observable<Observer> {
        public void notifyStarted(){
            for (final Observer observer:mObservers){
                observer.onRequestStarted();
            }

        }
        public void notifySusseded(){
            for (final Observer observer:mObservers){
                observer.onRequestSucceded();
            }

        }
        public void notifyFailed(){
            for (final Observer observer:mObservers){
                observer.onRequestFailed();
            }

        }
    }

    public void loadWeather(long cityId){
         mObservable.notifyStarted();
        RestClient.getApiClient().getWeather(cityId, new Callback<String>() {
            @Override
            public void success(String weather, Response response) {
                Log.d(TAG, "weatherData JSON:" + weather);
                try {
                    Log.d(TAG, "factorsData JSON:" + weather);
                    JSONObject jsonObject = new JSONObject(weather);


                    JSONObject cityObject=jsonObject.getJSONObject("city");
                    JSONArray weatherArray=jsonObject.getJSONArray("list");
                    City.saveCity(cityObject);
                    long cityId=cityObject.getLong("id");
                    Weather.deleteAll(cityId);
                    for (int i=0; i<weatherArray.length()-1; i++){
                        Weather.saveWeather(cityId,weatherArray.getJSONObject(i));
                    }
                    mObservable.notifySusseded();
                } catch (JSONException e) {
                    e.printStackTrace();
                    mObservable.notifyFailed();
                }
            }

            @Override
            public void failure(RetrofitError error) {
             //   Log.d(TAG, "Retrofit error:" + error.getResponse().getReason());

                mObservable.notifyFailed();
            }
        });
    }
    public void loadWeather(double lat,double lon){
         mObservable.notifyStarted();
        RestClient.getApiClient().getWeather(lat,lon, new Callback<String>() {
            @Override
            public void success(String weather, Response response) {
                Log.d(TAG, "weatherData JSON:" + weather);
                try {
                    Log.d(TAG, "factorsData JSON:" + weather);
                    JSONObject jsonObject = new JSONObject(weather);


                    JSONObject cityObject=jsonObject.getJSONObject("city");
                    JSONArray weatherArray=jsonObject.getJSONArray("list");
                    City.saveCity(cityObject);
                    long cityId=cityObject.getLong("id");
                    Weather.deleteAll(cityId);
                    for (int i=0; i<weatherArray.length()-1; i++){
                        Weather.saveWeather(cityId,weatherArray.getJSONObject(i));
                    }
                    mObservable.notifySusseded();
                } catch (JSONException e) {
                    e.printStackTrace();
                    mObservable.notifyFailed();
                }
            }

            @Override
            public void failure(RetrofitError error) {
               // Log.d(TAG, "Retrofit error:" + error.getResponse().getReason());
                mObservable.notifyFailed();
            }
        });
    }
}
