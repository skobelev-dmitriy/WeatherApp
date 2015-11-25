package rf.digitworld.weatherapp;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by Дмитрий on 25.11.2015.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
