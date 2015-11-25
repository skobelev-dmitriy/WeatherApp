package rf.digitworld.weatherapp.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Дмитрий on 24.11.2015.
 */
@Table(name="City")
public class City extends Model {
    @Column (name = "cityId")
    private Long cityId;
    @Column(name = "name")
    private String name;
    @Column(name = "lon")
    private Double lon;
    @Column(name = "lat")
    private Double lat;


    public City(){
        super();
    }
    public City (long id, String name){
        cityId=id;
        this.name=name;
    }
    public static void saveCity(JSONObject data)throws JSONException{
        City city;


        if (City.exists(data.getLong("id"))){
            Log.d("","City exists");
           city=getCity(data.getLong("id"));
          }else{
           city =new City();
            city.setCityId(data.getLong("id"));
            city.setName(data.getString("name"));
            city.save();
          }

    }
    public static City getCity(long cityId)throws NullPointerException{
        return new Select()
                .from(City.class)
                .where("cityId=?", cityId)
                .executeSingle();
    }
    public static boolean exists(long cityId)throws NullPointerException{

        return new Select()
                .from(City.class)
                .where("cityId=?", cityId)
                .exists();
    }
    public Long getCityId() {
        return cityId;
    }


    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
