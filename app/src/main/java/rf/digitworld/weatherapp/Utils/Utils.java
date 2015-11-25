package rf.digitworld.weatherapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import rf.digitworld.weatherapp.models.City;

/**
 * Created by Дмитрий on 25.11.2015.
 */
public class Utils {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_FORMAT_OUT = "d MMMMMM";
    private static final String HOUR_FORMAT_OUT = "HH:mm d MMMMMM ";

    public static String getDate(String dt_txt){
        long now=new Date().getTime();
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        SimpleDateFormat format_date = new SimpleDateFormat(DATE_FORMAT_OUT);

        TimeZone tz= TimeZone.getTimeZone("GMT+00");
        format.setTimeZone(tz);
        Date result = null;
        try {
            result = format.parse(dt_txt);

        } catch (ParseException e) {
            // Log.e(LOG_TAG, e.getClass().getSimpleName() + ": " + e.getMessage() + "\n" + datetimeLoaded);

        }

        String outDate=format_date.format(result);

        return outDate;
    }

    public static String getDateTime(long dt){

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        SimpleDateFormat format_hour = new SimpleDateFormat(HOUR_FORMAT_OUT);
        TimeZone tz= TimeZone.getTimeZone("GMT+00");
        format.setTimeZone(tz);
        Date result = new Date(dt*1000);
        String outDate=format_hour.format(result);
        return outDate;
    }

    public static void setCurrentCity(Context context, long cityId, String cityName){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString("cityName",cityName);
        editor.putLong("cityId",cityId).commit();
    }
    public static City getCurrentCity(Context context) {
        City city=new City();
        city.setName(getPrefs(context).getString("cityName",null));
        city.setCityId(getPrefs(context).getLong("cityId",0));
        return city;
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getPrefs(context).edit();
    }

    private static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
