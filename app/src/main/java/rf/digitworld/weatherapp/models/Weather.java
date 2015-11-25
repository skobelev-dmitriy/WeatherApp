package rf.digitworld.weatherapp.models;

import com.activeandroid.*;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import rf.digitworld.weatherapp.Utils.Utils;

/**
 * Created by Дмитрий on 24.11.2015.
 */
@Table(name = "Weather")
public class Weather extends Model {


    @Column (name = "City", onDelete = Column.ForeignKeyAction.CASCADE)
    private City city;
    @Column (name = "temp")
    private Double temp;
    @Column (name = "tempMin")
    private Double tempMin;
    @Column (name = "tempMax")
    private Double tempMax;
    @Column (name = "pressure")
    private Double pressure;
    @Column (name = "humidity")
    private int humidity;
    @Column (name = "tempKf")
    private Integer tempKf;
    @Column (name = "windSpeed")
    private Double windSpeed;
    @Column (name = "windDegees")
    private Double windDegees;
    @Column (name = "description")
    private String description;
    @Column (name = "icon")
    private String icon;
    @Column (name="dt" ,index = true)
    private long dt;
    @Column (name="date")
    private String date;


    public Weather(){
        super();
    }
    public static void deleteAll(long cityId){
        new Select()
                .from(Weather.class)
                .where("City=?", City.getCity(cityId).getId())
                .execute();
    }
    public static void saveWeather(long cityId,JSONObject object)throws JSONException{
        Weather weather=new Weather();
        long td=object.getLong("dt");
        if (exists(cityId,td)){
            weather=getWeather(cityId,td);
        }else{
            weather=new Weather();
        }
        weather.city=City.getCity(cityId);
        JSONObject main=object.getJSONObject("main");
        JSONArray weath=object.getJSONArray("weather");
        JSONObject wind=object.getJSONObject("wind");
        weather.setDt(object.getLong("dt"));
        weather.setDate(Utils.getDate(object.getString("dt_txt")));
        weather.setTemp(main.getDouble("temp"));
        weather.setTempMin(main.getDouble("temp_min"));
        weather.setTempMax(main.getDouble("temp_max"));
        weather.setPressure(main.getDouble("pressure"));
        weather.setHumidity(main.getInt("humidity"));
        weather.setDescription(weath.getJSONObject(0).getString("description"));
        weather.setIcon(weath.getJSONObject(0).getString("icon"));
        weather.setWindSpeed(wind.getDouble("speed"));
        weather.setWindDegees(wind.getDouble("deg"));
        weather.save();
    }
    public static boolean exists(long cityId, long td)throws NullPointerException{

        return new Select()
                .from(Weather.class)
                .where("City=?", City.getCity(cityId).getId())
                .where("dt=?", td)
                .exists();
    }
    public static Weather getWeather(long cityId,long td){
        return new Select()
                .from(Weather.class)
                .where("City=?", City.getCity(cityId).getId())
                .where("dt=?", td)
                .executeSingle();
    }
    public static List<Weather> getDayWeather(long cityId){
        List<Weather>list= new Select()
                .from(Weather.class)
                .where("City=?", City.getCity(cityId).getId())
                .execute();
        List<String> dates=new ArrayList<>();
        List<Weather> weathers=new ArrayList<>();
        for (Weather weather:list){
            String date=weather.getDate();
            dates.add(date);
        }
        Set<String> hashSet=new TreeSet<>(dates);
        for(String date:hashSet){
           List<Weather> day= getHourWeather(cityId,date);
            List<Double> temp=new ArrayList<>();
            for (Weather d:day){
                temp.add(d.getTemp());
            }
            Collections.sort(temp);
            Double minTemp=temp.get(0);
            Double maxTemp=temp.get(temp.size()-1);


            Weather   dayWeather=day.get(0);
            dayWeather.setTempMin(minTemp);
            dayWeather.setTempMax(maxTemp);
            weathers.add(dayWeather);
        }
        return weathers;

    }
    public static List<Weather>getHourWeather(long cityId, String date){
       return new Select()
                .from(Weather.class)
               .where("City=?", City.getCity(cityId).getId())
                .where("date=?", date)
                .execute();
    }




    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }



    public Double getWindDegees() {
        return windDegees;
    }

    public void setWindDegees(Double windDegees) {
        this.windDegees = windDegees;
    }
    /**
     *
     * @return
     * The temp
     */
    public Double getTemp() {
        return temp;
    }

    /**
     *
     * @param temp
     * The temp
     */
    public void setTemp(Double temp) {
        this.temp = temp;
    }

    /**
     *
     * @return
     * The tempMin
     */
    public Double getTempMin() {
        return tempMin;
    }

    /**
     *
     * @param tempMin
     * The temp_min
     */
    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    /**
     *
     * @return
     * The tempMax
     */
    public Double getTempMax() {
        return tempMax;
    }

    /**
     *
     * @param tempMax
     * The temp_max
     */
    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    /**
     *
     * @return
     * The pressure
     */
    public Double getPressure() {
        return pressure;
    }

    /**
     *
     * @param pressure
     * The pressure
     */
    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }


    /**
     *
     * @return
     * The humidity
     */
    public int getHumidity() {
        return humidity;
    }

    /**
     *
     * @param humidity
     * The humidity
     */
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    /**
     *
     * @return
     * The tempKf
     */
    public Integer getTempKf() {
        return tempKf;
    }

    /**
     *
     * @param tempKf
     * The temp_kf
     */
    public void setTempKf(Integer tempKf) {
        this.tempKf = tempKf;
    }

}

