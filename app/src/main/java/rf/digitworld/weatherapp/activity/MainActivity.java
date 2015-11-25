package rf.digitworld.weatherapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rf.digitworld.weatherapp.R;
import rf.digitworld.weatherapp.Utils.Utils;
import rf.digitworld.weatherapp.models.City;
import rf.digitworld.weatherapp.models.Weather;
import rf.digitworld.weatherapp.network.BackEndCommunicator;

public class MainActivity extends AppCompatActivity implements BackEndCommunicator.Observer {
    public static final int MODE_DAY=0;
    public static final int MODE_HOUR=1;
    public static final int REQUEST_CITY=1;
    public static final int REQUEST_MAPS=2;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    ActionBar actionBar;
    ListView listView;
    BackEndCommunicator backEndCommunicator;
    Context context;
    WeatherAdapter adapter;
    List<Weather> weather=new ArrayList<>();
    long cityId;
    String cityName;
    int mode=MODE_DAY;
    private static final String TAG="MainActivity";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CITY&&resultCode==RESULT_OK){
            cityName=data.getStringExtra("name");
            cityId=data.getLongExtra("id", 0);
            backEndCommunicator.loadWeather(cityId);

        }
        if (requestCode==REQUEST_MAPS&&resultCode==RESULT_OK){
           double lat=data.getDoubleExtra("lat", 0);
            double lon=data.getDoubleExtra("lon",0);
            backEndCommunicator.loadWeather(lat, lon);
          //  Utils.setCurrentCity(context,cityId,cityName);
        }
    }

    @Override
    public void onBackPressed() {
       if (mode==MODE_HOUR){
           mode=MODE_DAY;
           weather=Weather.getDayWeather(cityId);
           cityName=weather.get(0).getCity().getName();
           actionBar.setTitle(cityName);
           actionBar.setDisplayHomeAsUpEnabled(false);
           adapter.notifyDataSetChanged();
       }else{
           super.onBackPressed();
       }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        initView();
        setListeners();
        initContent();
        backEndCommunicator.registerObserver(this);
    }

    public void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);

        }
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        listView=(ListView)findViewById(R.id.listView);
    }

    public void setListeners(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"onItemClick");
                mode=MODE_HOUR;
                actionBar.setDisplayHomeAsUpEnabled(true);
                String date=weather.get(position).getDate();
                weather=Weather.getHourWeather(cityId,date);
                actionBar.setTitle(date);
                adapter.notifyDataSetChanged();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                backEndCommunicator.loadWeather(cityId);
                Log.d(TAG, "onRefresh");
            }
        });
    }
    public void startCityPickerActivity(){
        Intent intent=new Intent(context,CityListActivity.class);
        startActivityForResult(intent, REQUEST_CITY);
    }

    public void initContent(){
        backEndCommunicator=new BackEndCommunicator();
        adapter=new WeatherAdapter();
        listView.setAdapter(adapter);
        City city=Utils.getCurrentCity(context);
        if (city.getCityId()!=0) {
            cityId=city.getCityId();
            weather = Weather.getDayWeather(cityId);
            if (weather.size() != 0) {
                cityName = weather.get(0).getCity().getName();
                actionBar.setTitle(cityName);
            } else {
                backEndCommunicator.loadWeather(cityId);
            }
        }else{
            startCityPickerActivity();
        }

}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                // drawerLayout.openDrawer(GravityCompat.START);
                onBackPressed();
                return true;
            case R.id.action_list:
                // drawerLayout.openDrawer(GravityCompat.START);
                startCityPickerActivity();
                return true;



        }




        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backEndCommunicator.unregisterObserver(this);
    }

    @Override
    public void onRequestStarted() {
        Log.d(TAG,"onRequestStarted");
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onRequestSucceded() {
        Log.d(TAG, "onRequestSucceded");
        swipeRefreshLayout.setRefreshing(false);
        weather=Weather.getDayWeather(cityId);
        actionBar.setTitle(cityName);
        adapter.notifyDataSetChanged();
        Utils.setCurrentCity(context, cityId, cityName);
    }

    @Override
    public void onRequestFailed() {
        swipeRefreshLayout.setRefreshing(false);
        Log.d(TAG, "onRequestFailed()");
        Toast.makeText(context,"Невозможно загрузить данные", Toast.LENGTH_LONG);
    }

    class WeatherAdapter    extends BaseAdapter {

        public WeatherAdapter(){

        }
        @Override
        public int getCount() {
            return weather.size();
        }

        @Override
        public Object getItem(int position) {
            return weather.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (getCount()!=0) {


                final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_list, null);
                Weather item = weather.get(position);


                TextView date = (TextView) convertView.findViewById(R.id.text_date);
                TextView temp = (TextView) convertView.findViewById(R.id.temp);
                TextView hum = (TextView) convertView.findViewById(R.id.hum);
                TextView press = (TextView) convertView.findViewById(R.id.press);
                TextView wind = (TextView) convertView.findViewById(R.id.windSpeed);
                TextView description=(TextView)convertView.findViewById(R.id.weather_description);
                ImageView image = (ImageView) convertView.findViewById(R.id.image);



                String dateStr;
                if(mode==MODE_DAY) {
                     dateStr = item.getDate();
                    String min=String.format("%+2.0f", item.getTempMin());
                    String max=String.format("%+2.0f", item.getTempMax());
                    temp.setText(Html.fromHtml(min + "..." + max+"&#8451"));
                }else{
                    dateStr=Utils.getDateTime(item.getDt());
                    temp.setText(Html.fromHtml(String.format("%+2.0f", item.getTemp())+"&#8451"));
                }
                date.setText(dateStr);
                description.setText(item.getDescription());
                hum.setText(item.getHumidity() + "%");
                wind.setText(item.getWindSpeed().toString());
                Double pressInMM=item.getPressure()* 0.750061561303;
                String mmText=String.format("%3.0f", pressInMM);
                press.setText(mmText);
                int iconid = R.drawable.icon_01d;
                switch (item.getIcon()) {
                    case "01d":
                        iconid = R.drawable.icon_01d;
                        break;
                    case "02d":
                        iconid = R.drawable.icon_02d;
                        break;
                    case "03d":
                        iconid = R.drawable.icon_03d;
                        break;
                    case "04d":
                        iconid = R.drawable.icon_04d;
                        break;
                    case "09d":
                        iconid = R.drawable.icon_09d;
                        break;
                    case "10d":
                        iconid = R.drawable.icon_10d;
                        break;
                    case "11d":
                        iconid = R.drawable.icon_11d;
                        break;
                    case "13d":
                        iconid = R.drawable.icon_13d;
                        break;
                    case "50d":
                        iconid = R.drawable.icon_50d;
                        break;
                    case "01n":
                        iconid = R.drawable.icon_01n;
                        break;
                    case "02n":
                        iconid = R.drawable.icon_02n;
                        break;
                    case "03n":
                        iconid = R.drawable.icon_03n;
                        break;
                    case "04n":
                        iconid = R.drawable.icon_04n;
                        break;
                    case "09n":
                        iconid = R.drawable.icon_09n;
                        break;
                    case "10n":
                        iconid = R.drawable.icon_10n;
                        break;
                    case "11n":
                        iconid = R.drawable.icon_11n;
                        break;
                    case "13n":
                        iconid = R.drawable.icon_13n;
                        break;
                    case "50n":
                        iconid = R.drawable.icon_50n;
                        break;
                }
                image.setImageResource(iconid);
            }
            return convertView;

        }
    }
}
