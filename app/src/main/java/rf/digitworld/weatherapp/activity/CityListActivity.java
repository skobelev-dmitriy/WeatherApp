package rf.digitworld.weatherapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import rf.digitworld.weatherapp.R;
import rf.digitworld.weatherapp.models.City;
import rf.digitworld.weatherapp.models.Weather;

public class CityListActivity extends AppCompatActivity {
    ListView listView;
    Toolbar toolbar;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        final ArrayList<City> cityArrayList=new ArrayList<>();

        cityArrayList.add(new City(510291,"Петергоф"));
        cityArrayList.add(new City(523523,"Нальчик"));
        cityArrayList.add(new City(546448,"Kolganov"));
        cityArrayList.add(new City(498817,"Saint Petersburg"));


        ListAdapter adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return cityArrayList.size();
            }

            @Override
            public Object getItem(int position) {
                return cityArrayList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_city, null);
                City item = cityArrayList.get(position);
                TextView name = (TextView) convertView.findViewById(R.id.text_city);
                name.setText(item.getName());
                return convertView;
            }
        };
        listView=(ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City city=cityArrayList.get(position);
                Intent data=new Intent();
                data.putExtra("id",city.getCityId());
                data.putExtra("name",city.getName());
                setResult(RESULT_OK, data);
                finish();

            }
        });

    }
    @Override
        public boolean onOptionsItemSelected(MenuItem item){
            switch (item.getItemId()) {
                case android.R.id.home:
                    // drawerLayout.openDrawer(GravityCompat.START);
                   // onBackPressed();
                    setResult(RESULT_CANCELED);
                    finish();
                    return true;
            }
        return super.onOptionsItemSelected(item);
        }

}
